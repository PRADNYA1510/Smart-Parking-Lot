
package com.pradnya.smartparking.model;

import com.pradnya.smartparking.enums.ParkingTicketStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ParkingTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private Long fare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingTicketStatus status = ParkingTicketStatus.ACTIVE;

    protected ParkingTicket() {
    }

    public ParkingTicket(
            Vehicle vehicle,
            ParkingSpot parkingSpot,
            LocalDateTime entryTime) {
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = entryTime;
        this.status = ParkingTicketStatus.ACTIVE;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Long getFare() {
        return fare;
    }

    public void setFare(Long fare) {
        this.fare = fare;
    }

    public ParkingTicketStatus getStatus() {
        return status;
    }
    public void setStatus(ParkingTicketStatus status){
        this.status = status;
    }
    public void complete(LocalDateTime exitTime, Long fare) {
        this.exitTime = exitTime;
        this.fare = fare;
        this.status = ParkingTicketStatus.COMPLETED;
    }
}
