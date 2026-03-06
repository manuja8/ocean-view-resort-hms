package com.oceanview.test;

import com.oceanview.dto.GuestDTO;
import com.oceanview.service.impl.GuestServiceImpl;
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestServiceIT extends BaseH2Test {

    private GuestServiceImpl service;

    @BeforeEach
    void setup() {
        TestDb.clean();  // Clean DB before each test
        TestDb.seedBasicData();  // Seed basic data, including Guest
        service = new GuestServiceImpl();  // Instantiate the service
    }

    @Test
    void addGuest_shouldInsert() {
        GuestDTO dto = new GuestDTO();
        dto.setFullName("Nimal Silva");
        dto.setAddress("Colombo");
        dto.setContactNo("0772222222");
        dto.setIdentificationNo("200055555V");
        dto.setIdentificationType("NIC");
        dto.setEmail("nimal@test.com");

        int id = service.addGuest(dto, 1);
        assertTrue(id > 0);

        GuestDTO saved = service.getGuestById(id);
        assertEquals("Nimal Silva", saved.getFullName());
    }

    @Test
    void updateGuest_shouldChangeContact() {
        // Ensure guest data exists before trying to update it
        GuestDTO g = service.getGuestById(1);  // Assuming guestId 1 exists from seeding
        assertNotNull(g);

        g.setContactNo("0779999999");

        boolean ok = service.updateGuest(g, 1);
        assertTrue(ok);

        GuestDTO after = service.getGuestById(1);
        assertEquals("0779999999", after.getContactNo());
    }

    @Test
    void deleteGuest_shouldRemove() {
        // Ensure guest data exists before trying to delete it
        GuestDTO g = service.getGuestById(1);  // Make sure guest 1 exists
        assertNotNull(g);

        boolean ok = service.deleteGuest(1);  // Deleting guest with ID = 1
        assertTrue(ok);

        // Verify the guest is removed and no longer exists in the DB
        GuestDTO deletedGuest = service.getGuestById(1);
        assertNull(deletedGuest);  // Guest should be null after deletion
    }
}