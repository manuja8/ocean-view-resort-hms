package com.oceanview.service;

import com.oceanview.dto.GuestDTO;

import java.util.List;

public interface GuestService {
    int addGuest(GuestDTO dto, int createdByUserId);

    boolean updateGuest(GuestDTO dto, int updatedByUserId);

    GuestDTO getGuestById(int guestId);

    List<GuestDTO> getAllGuests(String q);

    boolean deleteGuest(int guestId); // admin-only
}