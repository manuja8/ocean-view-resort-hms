package com.oceanview.mapper;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.entity.Complaint;

public class ComplaintMapper {

    public static ComplaintDTO toDTO(Complaint complaint) {
        if (complaint == null) return null;

        ComplaintDTO dto = new ComplaintDTO();
        dto.setComplaintId(complaint.getComplaintId());
        dto.setGuest(GuestMapper.toDTO(complaint.getGuest()));
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus());

        return dto;
    }

    public static Complaint toEntity(ComplaintDTO dto) {
        if (dto == null) return null;

        Complaint complaint = new Complaint();
        complaint.setComplaintId(dto.getComplaintId());
        complaint.setGuest(GuestMapper.toEntity(dto.getGuest()));
        complaint.setDescription(dto.getDescription());
        complaint.setStatus(dto.getStatus());

        return complaint;
    }
}