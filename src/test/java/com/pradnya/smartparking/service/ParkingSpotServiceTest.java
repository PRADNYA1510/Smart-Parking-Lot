
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import com.pradnya.smartparking.exception.DuplicateParkingSpotException;
import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.model.ParkingSpot;
import com.pradnya.smartparking.repository.ParkingFloorRepository;
import com.pradnya.smartparking.repository.ParkingSpotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private ParkingFloorRepository parkingFloorRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;


    @Test
    void shouldAddParkingSpotSuccessfully() {
        ParkingFloor floor = new ParkingFloor(1);
        ParkingSpot spot = new ParkingSpot("A1", SpotSize.CAR);

        when(parkingFloorRepository.findById(1L))
                .thenReturn(Optional.of(floor));

        when(parkingSpotRepository
                .existsByParkingFloor_FloorIdAndSpotNumber(1L, "A1"))
                .thenReturn(false);

        when(parkingSpotRepository.save(any(ParkingSpot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ParkingSpot result =
                parkingSpotService.addSpot(1L, "A1", SpotSize.CAR);

        assertNotNull(result);
        assertEquals("A1", result.getSpotNumber());
        assertEquals(SpotSize.CAR, result.getSpotSize());
        assertEquals(floor, result.getParkingFloor());

        verify(parkingFloorRepository).findById(1L);
        verify(parkingSpotRepository)
                .existsByParkingFloor_FloorIdAndSpotNumber(1L, "A1");
        verify(parkingSpotRepository).save(any(ParkingSpot.class));
    }

    @Test
    void shouldThrowExceptionWhenFloorDoesNotExist() {
        when(parkingFloorRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> parkingSpotService.addSpot(
                        999L, "A1", SpotSize.CAR)
        );

        verify(parkingSpotRepository, never())
                .save(any(ParkingSpot.class));

        verify(parkingSpotRepository, never())
                .existsByParkingFloor_FloorIdAndSpotNumber(
                        anyLong(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenSpotNumberAlreadyExistsOnFloor() {
        ParkingFloor floor = new ParkingFloor(1);

        when(parkingFloorRepository.findById(1L))
                .thenReturn(Optional.of(floor));

        when(parkingSpotRepository
                .existsByParkingFloor_FloorIdAndSpotNumber(1L, "A1"))
                .thenReturn(true);

        assertThrows(
                DuplicateParkingSpotException.class,
                () -> parkingSpotService.addSpot(
                        1L, "A1", SpotSize.CAR)
        );

        verify(parkingSpotRepository, never())
                .save(any(ParkingSpot.class));
    }

    @Test
    void shouldReturnAvailableSpotsBySizeAcrossAllFloors() {
        ParkingSpot spot1 = new ParkingSpot("A1", SpotSize.CAR);
        ParkingSpot spot2 = new ParkingSpot("B1", SpotSize.CAR);

        when(parkingSpotRepository.findBySpotStatusAndSpotSize(
                SpotStatus.AVAILABLE, SpotSize.CAR))
                .thenReturn(List.of(spot1, spot2));

        List<ParkingSpot> result =
                parkingSpotService.getAvailableSpots(SpotSize.CAR);

        assertEquals(2, result.size());
        assertTrue(result.contains(spot1));
        assertTrue(result.contains(spot2));

        verify(parkingSpotRepository).findBySpotStatusAndSpotSize(
                SpotStatus.AVAILABLE, SpotSize.CAR);
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableSpotsMatchSize() {
        when(parkingSpotRepository.findBySpotStatusAndSpotSize(
                SpotStatus.AVAILABLE, SpotSize.BUS))
                .thenReturn(List.of());

        List<ParkingSpot> result =
                parkingSpotService.getAvailableSpots(SpotSize.BUS);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAvailableSpotsForSpecificFloor() {
        ParkingSpot spot = new ParkingSpot("A1", SpotSize.CAR);

        when(parkingSpotRepository
                .findByParkingFloor_FloorIdAndSpotStatusAndSpotSize(
                        1L, SpotStatus.AVAILABLE, SpotSize.CAR))
                .thenReturn(List.of(spot));

        List<ParkingSpot> result =
                parkingSpotService.getAvailableSpots(1L, SpotSize.CAR);

        assertEquals(1, result.size());
        assertSame(spot, result.get(0));

        verify(parkingSpotRepository)
                .findByParkingFloor_FloorIdAndSpotStatusAndSpotSize(
                        1L, SpotStatus.AVAILABLE, SpotSize.CAR);
    }

}
