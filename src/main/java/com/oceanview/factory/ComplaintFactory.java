package com.oceanview.factory;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.entity.Complaint;

public class ComplaintFactory {

    public static Complaint create(ComplaintDTO dto, int createdByUserId) {

        Complaint c = new Complaint();
        c.setGuestId(dto.getGuestId());

        // reservation
        Integer rid = dto.getReservationId();
        c.setReservationId((rid != null && rid > 0) ? rid : null);

        c.setSubject(trim(dto.getSubject()));
        c.setDescription(trim(dto.getDescription()));

        String status = trim(dto.getStatus());
        if (status == null || status.isEmpty()) status = "open";

        String priority = trim(dto.getPriority());
        if (priority == null || priority.isEmpty()) priority = "medium";

        c.setStatus(status.toLowerCase());
        c.setPriority(priority.toLowerCase());

        c.setCreatedByUserId(createdByUserId);

        return c;
    }

    public static Complaint createForUpdate(ComplaintDTO dto, int updatedByUserId) {
        Complaint c = create(dto, 0);
        c.setComplaintId(dto.getComplaintId());
        c.setUpdatedByUserId(updatedByUserId);
        return c;
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}