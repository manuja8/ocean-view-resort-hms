package com.oceanview.test;

import com.oceanview.dto.RoomDTO;
import com.oceanview.dto.RoomTypeDTO;
import com.oceanview.service.impl.RoomServiceImpl;
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomServiceIT extends BaseH2Test {

    private RoomServiceImpl roomService;

    @BeforeEach
    void setup() {
        TestDb.clean();
        TestDb.seedBasicData();
        roomService = new RoomServiceImpl();
    }

    @Test
    void createRoom_shouldInsertAndReturnId() {
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber("A101"); // IMPORTANT: not D01 (already seeded)
        dto.setRoomTypeId(1);
        dto.setStatus("available");

        int id = roomService.createRoom(dto, 1);
        assertTrue(id > 0);

        RoomDTO saved = roomService.getRoomById(id);
        assertNotNull(saved);
        assertEquals("A101", saved.getRoomNumber());
        assertEquals("available", saved.getStatus());
    }

    @Test
    void updateRoom_shouldChangeStatus() {
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber("A102");
        dto.setRoomTypeId(1);
        dto.setStatus("available");
        int id = roomService.createRoom(dto, 1);

        RoomDTO upd = roomService.getRoomById(id);
        upd.setStatus("maintenance");
        roomService.updateRoom(upd, 1);

        RoomDTO after = roomService.getRoomById(id);
        assertEquals("maintenance", after.getStatus());
    }

    @Test
    void searchRooms_shouldFilterByStatus() {
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber("A103");
        dto.setRoomTypeId(1);
        dto.setStatus("maintenance");
        roomService.createRoom(dto, 1);

        List<RoomDTO> maintenanceRooms = roomService.searchRooms(null, "maintenance");
        assertTrue(maintenanceRooms.stream().anyMatch(r -> "A103".equals(r.getRoomNumber())));
    }

    @Test
    void getRoomTypes_shouldReturnSeededTypes() {
        List<RoomTypeDTO> types = roomService.getRoomTypes();
        assertNotNull(types);
        assertTrue(types.size() >= 1);
        assertEquals("Deluxe", types.get(0).getTypeName());
    }
}