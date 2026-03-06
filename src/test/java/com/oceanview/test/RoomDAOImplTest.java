package com.oceanview.test;

import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dto.RoomDTO; // Use RoomDTO since findDTOById returns RoomDTO
import com.oceanview.entity.Room; // Ensure Room entity is used for room creation
import com.oceanview.test.BaseH2Test;
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomDAOImplTest extends BaseH2Test {

    private RoomDAOImpl roomDAO;

    @BeforeEach
    void setup() {
        TestDb.clean();               // Clean DB before each test
        TestDb.seedBasicData();       // Seed basic data (including room types)
        roomDAO = new RoomDAOImpl();  // Instantiate DAO implementation
    }

    @Test
    void testSaveRoom() {
        Room room = new Room(); // Use the entity (not DTO)
        room.setRoomNumber("A102");
        room.setRoomTypeId(1);
        room.setStatus("available");

        int id = roomDAO.save(room, 1); // Save room to database
        assertTrue(id > 0);

        RoomDTO savedRoomDTO = roomDAO.findDTOById(id); // Find room as DTO by ID
        assertNotNull(savedRoomDTO);
        assertEquals("A102", savedRoomDTO.getRoomNumber()); // Compare with DTO field
    }

    @Test
    void testUpdateRoom() {
        Room room = new Room();
        room.setRoomNumber("A102");
        room.setRoomTypeId(1);
        room.setStatus("available");

        int id = roomDAO.save(room, 1);
        room.setRoomId(id);  // Set the room ID to update it
        room.setStatus("maintenance");

        boolean updated = roomDAO.update(room, 1);  // Update the room status
        assertTrue(updated);

        RoomDTO updatedRoomDTO = roomDAO.findDTOById(id); // Fetch updated room as DTO
        assertEquals("maintenance", updatedRoomDTO.getStatus());
    }

    @Test
    void testDeleteRoom() {
        Room room = new Room();
        room.setRoomNumber("A103");
        room.setRoomTypeId(1);
        room.setStatus("available");

        int id = roomDAO.save(room, 1);
        boolean deleted = roomDAO.delete(id);  // Delete room by ID
        assertTrue(deleted);

        RoomDTO deletedRoomDTO = roomDAO.findDTOById(id); // Check if room is deleted
        assertNull(deletedRoomDTO);  // Room should not be found
    }
}