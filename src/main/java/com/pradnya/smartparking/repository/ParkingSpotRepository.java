package com.pradnya.smartparking.repository;

import com.pradnya.smartparking.enums.SpotSize;
import com.pradnya.smartparking.enums.SpotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pradnya.smartparking.model.ParkingSpot;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot,Long> {
    List<ParkingSpot> findBySpotStatusAndSpotSize(
            SpotStatus spotStatus, SpotSize spotSize);

    List<ParkingSpot> findByParkingFloor_FloorIdAndSpotStatusAndSpotSize(
            Long floorId,
            SpotStatus spotStatus,
            SpotSize spotSize
    );

    boolean existsByParkingFloor_FloorIdAndSpotNumber(
            Long floorId,
            String spotNumber
    );

    List<ParkingSpot> findBySpotStatusAndSpotSizeOrderByParkingFloor_FloorNumberAscSpotNumberAsc(
            SpotStatus status,
            SpotSize size
    );

    List<ParkingSpot> findByParkingFloor_FloorIdAndSpotStatusAndSpotSizeOrderBySpotNumberAsc(
            Long floorId,
            SpotStatus status,
            SpotSize size
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT s FROM ParkingSpot s
    WHERE s.parkingFloor.floorId = :floorId
      AND s.spotStatus = :status
      AND s.spotSize = :size
    ORDER BY s.spotNumber ASC
    """)
    List<ParkingSpot> findAvailableSpotsByFloorForUpdate(
            @Param("floorId") Long floorId,
            @Param("status") SpotStatus status,
            @Param("size") SpotSize size);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT s FROM ParkingSpot s
    WHERE s.spotStatus = :status
      AND s.spotSize = :size
    ORDER BY s.parkingFloor.floorNumber ASC, s.spotNumber ASC
    """)
    List<ParkingSpot> findAvailableSpotsForUpdate(
            @Param("status") SpotStatus status,
            @Param("size") SpotSize size);

    List<ParkingSpot> findByParkingFloor_FloorId(Long floorId);

    @Query("""
    SELECT COUNT(s)
    FROM ParkingSpot s
    WHERE s.parkingFloor.floorId = :floorId
      AND s.spotStatus = :status
    """)
    long countByFloorIdAndStatus(
            @Param("floorId") Long floorId,
            @Param("status") SpotStatus status
    );

    long countBySpotStatus(SpotStatus spotStatus);
}
