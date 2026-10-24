package com.pradnya.smartparking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import jakarta.persistence.*;

@Entity
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;
    private String spotNumber;
    @Enumerated(EnumType.STRING)
    private SpotStatus spotStatus = SpotStatus.AVAILABLE;
    @Enumerated(EnumType.STRING)
    private SpotSize spotSize;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    @JsonIgnore
    private ParkingFloor parkingFloor;

    protected ParkingSpot() {

    }

    public ParkingSpot(String spotNumber, SpotSize spotSize) {
        this.spotNumber = spotNumber;
        this.spotSize = spotSize;

    }


    public void setParkingFloor(ParkingFloor parkingFloor) {
        this.parkingFloor = parkingFloor;
    }

    public SpotStatus getSpotStatus() {
        return spotStatus;
    }

    public ParkingFloor getParkingFloor() {
        return parkingFloor;
    }

    public SpotSize getSpotSize() {
        return spotSize;
    }

    public void setSpotSize(SpotSize spotSize) {
        this.spotSize = spotSize;
    }

    public void setSpotStatus(SpotStatus spotStatus) {
        this.spotStatus = spotStatus;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }
}
