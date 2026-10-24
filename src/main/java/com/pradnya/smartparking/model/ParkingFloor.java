package com.pradnya.smartparking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "floor_number")
        }
)
public class ParkingFloor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long floorId;
    @Column(name = "floor_number", nullable = false)
    private int floorNumber;
    @OneToMany(mappedBy = "parkingFloor")
    private List<ParkingSpot> parkingSpots = new ArrayList<>();

    protected ParkingFloor() {
    }

    public ParkingFloor( int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void addParkingSpot(ParkingSpot spot) {
        if(!parkingSpots.contains(spot)) {
            parkingSpots.add(spot);
        }
        spot.setParkingFloor(this);
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
}
