
package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.enums.ParkingTicketStatus;
import com.pradnya.smartparking.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingTicketRepository
        extends JpaRepository<ParkingTicket, Long> {


    List<ParkingTicket> findByStatus(ParkingTicketStatus status);


    Optional<ParkingTicket> findByTicketIdAndStatus(
            Long ticketId, ParkingTicketStatus status);


    boolean existsByVehicle_VehicleIdAndStatus(
            Long vehicleId, ParkingTicketStatus status);


    List<ParkingTicket> findByVehicle_VehicleIdOrderByEntryTimeDesc(
            Long vehicleId);

    long countByStatus(ParkingTicketStatus status);

    @Query("""
    SELECT COALESCE(SUM(t.fare), 0)
    FROM ParkingTicket t
    WHERE t.status = :status
    """)
    Long sumFareByStatus(
            @Param("status") ParkingTicketStatus status
    );

    List<ParkingTicket> findByVehicle_RegistrationNumberOrderByEntryTimeDesc(
            String registrationNumber);
}
