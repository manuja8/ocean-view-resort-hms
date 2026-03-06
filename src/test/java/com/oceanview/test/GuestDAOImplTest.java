package com.oceanview.test;

import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.entity.Guest; // Use the entity
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestDAOImplTest extends BaseH2Test {

    private GuestDAOImpl guestDAO;

    @BeforeEach
    void setup() {
        TestDb.clean();               // Clean DB before each test
        TestDb.seedBasicData();       // Seed basic data (including guests)
        guestDAO = new GuestDAOImpl(); // Instantiate DAO implementation
    }

    @Test
    void testSaveGuest() {
        Guest guest = new Guest();
        guest.setFullName("Kasun Perera");
        guest.setAddress("Colombo");
        guest.setContactNo("0771234567");
        guest.setIdentificationNo("12345");
        guest.setIdentificationType("NIC");

        int id = guestDAO.save(guest, 1);  // Save guest to database
        assertTrue(id > 0);

        Guest savedGuest = guestDAO.findById(id); // Find guest by ID using the entity
        assertNotNull(savedGuest);
        assertEquals("Kasun Perera", savedGuest.getFullName());
    }

    @Test
    void testUpdateGuest() {
        Guest guest = guestDAO.findById(1); // Assuming guest with ID 1 exists
        assertNotNull(guest);

        guest.setContactNo("0779999999");
        boolean updated = guestDAO.update(guest, 1);  // Update guest contact number
        assertTrue(updated);

        Guest updatedGuest = guestDAO.findById(1);
        assertEquals("0779999999", updatedGuest.getContactNo());
    }

    @Test
    void testDeleteGuest() {
        boolean deleted = guestDAO.delete(1);  // Delete guest with ID = 1
        assertTrue(deleted);

        Guest deletedGuest = guestDAO.findById(1);
        assertNull(deletedGuest);  // Ensure guest is deleted and no longer exists
    }
}