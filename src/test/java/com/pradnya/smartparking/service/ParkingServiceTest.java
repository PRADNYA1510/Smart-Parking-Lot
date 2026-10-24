
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.enums.ParkingTicketStatus;
import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import com.pradnya.smartparking.enums.VehicleType;
import com.pradnya.smartparking.model.*;
import com.pradnya.smartparking.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ParkingTicketRepository parkingTicketRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private FareCalculatorService fareCalculatorService;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    void shouldParkVehicleSuccessfully() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);

        ParkingFloor floor = new ParkingFloor(1);
        ParkingSpot spot = new ParkingSpot("A-101", SpotSize.CAR);

        spot.setSpotStatus(SpotStatus.AVAILABLE);
        spot.setSpotSize(SpotSize.CAR);
        spot.setParkingFloor(floor);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(parkingTicketRepository
                .existsByVehicle_VehicleIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE))
                .thenReturn(false);

        when(parkingSpotRepository.findAvailableSpotsForUpdate(
                SpotStatus.AVAILABLE, SpotSize.CAR))
                .thenReturn(List.of(spot));

        when(parkingTicketRepository.save(any(ParkingTicket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ParkingTicket ticket = parkingService.parkVehicle(1L, null);

        assertNotNull(ticket);
        assertEquals(SpotStatus.OCCUPIED, spot.getSpotStatus());
        assertEquals(vehicle, ticket.getVehicle());
        assertEquals(spot, ticket.getParkingSpot());
        assertNotNull(ticket.getEntryTime());

        verify(parkingTicketRepository).save(any(ParkingTicket.class));
    }

    @Test
    void shouldRejectVehicleIfAlreadyParked() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(parkingTicketRepository
                .existsByVehicle_VehicleIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> parkingService.parkVehicle(1L, null)
        );

        verify(parkingSpotRepository, never())
                .findAvailableSpotsForUpdate(
                        SpotStatus.AVAILABLE, SpotSize.CAR);

        verify(parkingTicketRepository, never())
                .save(any(ParkingTicket.class));
    }

    @Test
    void shouldThrowExceptionWhenNoSpotIsAvailable() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(parkingTicketRepository
                .existsByVehicle_VehicleIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE))
                .thenReturn(false);

        when(parkingSpotRepository.findAvailableSpotsForUpdate(
                SpotStatus.AVAILABLE, SpotSize.CAR))
                .thenReturn(List.of());

        assertThrows(
                EntityNotFoundException.class,
                () -> parkingService.parkVehicle(1L, null)
        );

        verify(parkingTicketRepository, never())
                .save(any(ParkingTicket.class));
    }

    @Test
    void shouldExitVehicleAndCalculateFare() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);

        ParkingFloor floor = new ParkingFloor(1);
        ParkingSpot spot = new ParkingSpot("A-101", SpotSize.CAR);
        spot.setSpotStatus(SpotStatus.OCCUPIED);
        spot.setParkingFloor(floor);

        ParkingTicket ticket = new ParkingTicket(
                vehicle, spot, LocalDateTime.now().minusHours(2));

        when(parkingTicketRepository
                .findByTicketIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE))
                .thenReturn(Optional.of(ticket));

        when(fareCalculatorService.calculateBillableHours(
                any(), any(), any()))
                .thenReturn(200L);

        when(parkingTicketRepository.save(any(ParkingTicket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ParkingTicket result = parkingService.exitVehicle(1L);

        assertNotNull(result.getExitTime());
        assertEquals(200L, result.getFare());
        assertEquals(SpotStatus.AVAILABLE, spot.getSpotStatus());

        verify(parkingSpotRepository).save(spot);
        verify(parkingTicketRepository).save(ticket);
    }

    @Test
    void shouldThrowExceptionWhenVehicleDoesNotExist() {
        when(vehicleRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> parkingService.parkVehicle(999L, null)
        );

        verify(parkingTicketRepository, never())
                .save(any(ParkingTicket.class));
    }

    @Test
    void shouldReturnAllActiveParkingTickets() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);
        ParkingFloor floor = new ParkingFloor(1);
        ParkingSpot spot = new ParkingSpot("A1", SpotSize.CAR);
        spot.setParkingFloor(floor);

        ParkingTicket ticket1 = new ParkingTicket(
                vehicle, spot, LocalDateTime.now().minusHours(2));
        ParkingTicket ticket2 = new ParkingTicket(
                vehicle, spot, LocalDateTime.now().minusHours(1));

        when(parkingTicketRepository.findByStatus(
                ParkingTicketStatus.ACTIVE))
                .thenReturn(List.of(ticket1, ticket2));

        List<ParkingTicket> result = parkingService.getParkingTicket();

        assertEquals(2, result.size());
        assertTrue(result.contains(ticket1));
        assertTrue(result.contains(ticket2));

        verify(parkingTicketRepository)
                .findByStatus(ParkingTicketStatus.ACTIVE);
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveParkingTicketsExist() {
        when(parkingTicketRepository.findByStatus(
                ParkingTicketStatus.ACTIVE))
                .thenReturn(List.of());

        List<ParkingTicket> result = parkingService.getParkingTicket();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(parkingTicketRepository)
                .findByStatus(ParkingTicketStatus.ACTIVE);
    }

    @Test
    void shouldReturnActiveParkingTicketById() {
        Vehicle vehicle = new Vehicle("KA01AB1234", VehicleType.CAR);
        ParkingFloor floor = new ParkingFloor(1);
        ParkingSpot spot = new ParkingSpot("A1", SpotSize.CAR);
        spot.setParkingFloor(floor);

        ParkingTicket ticket = new ParkingTicket(
                vehicle, spot, LocalDateTime.now().minusHours(2));

        when(parkingTicketRepository
                .findByTicketIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE))
                .thenReturn(Optional.of(ticket));

        ParkingTicket result =
                parkingService.getParkingTicketByTicketId(1L);

        assertNotNull(result);
        assertSame(ticket, result);

        verify(parkingTicketRepository)
                .findByTicketIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE);
    }

    @Test
    void shouldThrowExceptionWhenActiveTicketIsNotFound() {
        when(parkingTicketRepository
                .findByTicketIdAndStatus(
                        999L, ParkingTicketStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> parkingService.getParkingTicketByTicketId(999L)
        );

        verify(parkingTicketRepository)
                .findByTicketIdAndStatus(
                        999L, ParkingTicketStatus.ACTIVE);
    }

    @Test
    void shouldThrowExceptionWhenExitingNonExistentTicket() {
        when(parkingTicketRepository
                .findByTicketIdAndStatus(
                        999L, ParkingTicketStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> parkingService.exitVehicle(999L)
        );

        verify(fareCalculatorService, never())
                .calculateBillableHours(any(), any(), any());

        verify(parkingSpotRepository, never())
                .save(any(ParkingSpot.class));

        verify(parkingTicketRepository, never())
                .save(any(ParkingTicket.class));
    }

    @Test
    void shouldNotCalculateFareWhenTicketIsAlreadyExited() {
        when(parkingTicketRepository
                .findByTicketIdAndStatus(
                        1L, ParkingTicketStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> parkingService.exitVehicle(1L)
        );

        verify(fareCalculatorService, never())
                .calculateBillableHours(any(), any(), any());

        verify(parkingSpotRepository, never())
                .save(any(ParkingSpot.class));
    }
}
