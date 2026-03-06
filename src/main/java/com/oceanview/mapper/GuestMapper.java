package com.oceanview.mapper;

import com.oceanview.dto.GuestDTO;
import com.oceanview.entity.Guest;

import java.time.LocalDate;

public class GuestMapper {

    public static Guest toEntity(GuestDTO dto) {
        if (dto == null) return null;
        Guest g = new Guest();
        g.setGuestId(dto.getGuestId());
        g.setFullName(dto.getFullName());
        g.setGender(dto.getGender());
        g.setAddress(dto.getAddress());
        g.setContactNo(dto.getContactNo());
        g.setEmail(dto.getEmail());
        g.setIdentificationNo(dto.getIdentificationNo());
        g.setIdentificationType(dto.getIdentificationType());

        if (dto.getDateOfBirth() != null && !dto.getDateOfBirth().isBlank()) {
            g.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));
        }
        return g;
    }

    public static GuestDTO toDTO(Guest g) {
        if (g == null) return null;
        GuestDTO dto = new GuestDTO();
        dto.setGuestId(g.getGuestId());
        dto.setFullName(g.getFullName());
        dto.setGender(g.getGender());
        dto.setAddress(g.getAddress());
        dto.setContactNo(g.getContactNo());
        dto.setEmail(g.getEmail());
        dto.setIdentificationNo(g.getIdentificationNo());
        dto.setIdentificationType(g.getIdentificationType());

        if (g.getDateOfBirth() != null) dto.setDateOfBirth(g.getDateOfBirth().toString());
        return dto;
    }
}