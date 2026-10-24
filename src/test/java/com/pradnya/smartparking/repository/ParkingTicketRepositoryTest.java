package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.enums.ParkingTicketStatus;
import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.VehicleType;
import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.model.ParkingSpot;
import com.pradnya.smartparking.model.ParkingTicket;
import com.pradnya.smartparking.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingTicketRepositoryTest {

    @Autowired
    private ParkingTicketRepository parkingTicketRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    void shouldSaveAndFindTicketByIdAndStatus() {
        ParkingTicket ticket = createTicket("KA01TICKET901", 921, "T-921");
        ParkingTicket saved = parkingTicketRepository.saveAndFlush(ticket);

        Optional<ParkingTicket> found =
                parkingTicketRepository.findByTicketIdAndStatus(
                        saved.getTicketId(), ParkingTicketStatus.ACTIVE);

        assertTrue(found.isPresent());
        assertEquals(ParkingTicketStatus.ACTIVE, found.get().getStatus());
    }

    @Test
    void shouldFindTicketsByVehicleOrderedByEntryTime() {
        ParkingTicket ticket = createTicket("KA01TICKET902", 922, "T-922");
        ParkingTicket saved = parkingTicketRepository.saveAndFlush(ticket);

        List<ParkingTicket> found =
                parkingTicketRepository.findByVehicle_VehicleIdOrderByEntryTimeDesc(
                        saved.getVehicle().getVehicleId());

        assertFalse(found.isEmpty());
        assertEquals(saved.getTicketId(), found.get(0).getTicketId());
    }

    @Test
    void shouldCountActiveTickets() {
        createTicketAndSave("KA01TICKET903", 923, "T-923");

        assertTrue(parkingTicketRepository.countByStatus(ParkingTicketStatus.ACTIVE) >= 1);
    }

    private ParkingTicket createTicket(String registration, int floorNumber, String spotNumber) {
        Vehicle vehicle = vehicleRepository.saveAndFlush(
                new Vehicle(registration, VehicleType.CAR));

        ParkingFloor floor = parkingFloorRepository.saveAndFlush(
                new ParkingFloor(floorNumber));

        ParkingSpot spot = new ParkingSpot(spotNumber, SpotSize.CAR);
        floor.addParkingSpot(spot);
        parkingSpotRepository.saveAndFlush(spot);

        ParkingTicket ticket = new ParkingTicket(vehicle,spot,LocalDateTime.now());
        ticket.setVehicle(vehicle);
        ticket.setParkingSpot(spot);
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setStatus(ParkingTicketStatus.ACTIVE);
        return ticket;
    }

    private void createTicketAndSave(String registration, int floorNumber, String spotNumber) {
        parkingTicketRepository.saveAndFlush(createTicket(registration, floorNumber, spotNumber));
    }
}
