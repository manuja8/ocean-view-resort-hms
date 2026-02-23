package com.oceanview.mapper;

import com.oceanview.dto.GuestDTO;
import com.oceanview.entity.Guest;

public class GuestMapper {

    public static GuestDTO toDTO(Guest guest) {
        if (guest == null) return null;

        GuestDTO dto = new GuestDTO();
        dto.setGuestId(guest.getGuestId());
        dto.setName(guest.getName());
        dto.setAddress(guest.getAddress());
        dto.setContactNumber(guest.getContactNumber());
        dto.setEmail(guest.getEmail());

        return dto;
    }

    public static Guest toEntity(GuestDTO dto) {
        if (dto == null) return null;

        Guest guest = new Guest();
        guest.setGuestId(dto.getGuestId());
        guest.setName(dto.getName());
        guest.setAddress(dto.getAddress());
        guest.setContactNumber(dto.getContactNumber());
        guest.setEmail(dto.getEmail());

        return guest;
    }
}