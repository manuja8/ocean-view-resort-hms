package com.oceanview.mapper;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.entity.Complaint;

public class ComplaintMapper {

    public static Complaint toEntity(ComplaintDTO dto) {
        if (dto == null) return null;

        Complaint c = new Complaint();
        c.setComplaintId(dto.getComplaintId());
        c.setGuestId(dto.getGuestId());
        c.setReservationId(dto.getReservationId());
        c.setSubject(dto.getSubject());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());
        c.setPriority(dto.getPriority());
        return c;
    }

    public static ComplaintDTO toDTO(Complaint c) {
        if (c == null) return null;

        ComplaintDTO dto = new ComplaintDTO();
        dto.setComplaintId(c.getComplaintId());
        dto.setGuestId(c.getGuestId());
        dto.setReservationId(c.getReservationId());
        dto.setSubject(c.getSubject());
        dto.setDescription(c.getDescription());
        dto.setStatus(c.getStatus());
        dto.setPriority(c.getPriority());
        if (c.getCreatedAt() != null) dto.setCreatedAt(c.getCreatedAt().toString());
        return dto;
    }
}