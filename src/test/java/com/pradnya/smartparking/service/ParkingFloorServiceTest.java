
package com.pradnya.smartparking.service;

import com.pradnya.smartparking.model.ParkingFloor;
import com.pradnya.smartparking.repository.ParkingFloorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingFloorServiceTest {

    @Mock
    private ParkingFloorRepository parkingFloorRepository;

    @InjectMocks
    private ParkingFloorService parkingFloorService;

    @Test
    void shouldCreateFloorSuccessfully() {
        ParkingFloor floor = new ParkingFloor(1);

        when(parkingFloorRepository.save(any(ParkingFloor.class)))
                .thenReturn(floor);

        ParkingFloor result = parkingFloorService.createFloor(1);

        assertNotNull(result);
        assertEquals(1, result.getFloorNumber());

        verify(parkingFloorRepository).save(any(ParkingFloor.class));
    }

    @Test
    void shouldReturnAllFloors() {
        List<ParkingFloor> floors = List.of(
                new ParkingFloor(1),
                new ParkingFloor(2)
        );

        when(parkingFloorRepository.findAll()).thenReturn(floors);

        List<ParkingFloor> result = parkingFloorService.getAllFloors();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getFloorNumber());
        assertEquals(2, result.get(1).getFloorNumber());

        verify(parkingFloorRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoFloorsExist() {
        when(parkingFloorRepository.findAll()).thenReturn(List.of());

        List<ParkingFloor> result = parkingFloorService.getAllFloors();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(parkingFloorRepository).findAll();
    }
}
