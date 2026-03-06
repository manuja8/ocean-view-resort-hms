package com.oceanview.test;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.service.impl.ComplaintServiceImpl;
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ComplaintServiceIT extends BaseH2Test {

    private ComplaintServiceImpl service;

    @BeforeEach
    void setup() {
        TestDb.clean();
        TestDb.seedBasicData();
        TestDb.seedReservation(
                1, 1, 1,
                LocalDate.parse("2026-03-10"),
                LocalDate.parse("2026-03-13"),
                "booked", 1
        );
        service = new ComplaintServiceImpl();
    }

    @Test
    void createComplaint_shouldRejectMissingSubject() {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setGuestId(1);
        dto.setDescription("desc");
        dto.setStatus("open");
        dto.setPriority("medium");

        assertThrows(IllegalArgumentException.class, () -> service.create(dto, 1));
    }

    @Test
    void createComplaint_shouldAllowWithReservationId() {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setGuestId(1);
        dto.setReservationId(1);
        dto.setSubject("Noise");
        dto.setDescription("Very noisy");
        dto.setStatus("open");
        dto.setPriority("high");

        int id = service.create(dto, 1);
        assertTrue(id > 0);

        assertNotNull(service.getById(id));
    }

    @Test
    void updateComplaint_shouldWork() {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setGuestId(1);
        dto.setSubject("AC");
        dto.setDescription("Not working");
        dto.setStatus("open");
        dto.setPriority("medium");

        int id = service.create(dto, 1);

        ComplaintDTO upd = service.getById(id);
        upd.setComplaintId(id);
        upd.setStatus("resolved");

        boolean ok = service.update(upd, 1);
        assertTrue(ok);

        ComplaintDTO after = service.getById(id);
        assertEquals("resolved", after.getStatus());
    }
}