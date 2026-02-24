package com.oceanview.service;

import com.oceanview.dto.GuestDTO;

import java.util.List;

public interface GuestService {

    int addGuest(GuestDTO guestDTO);

    GuestDTO getGuestById(int guestId);

    List<GuestDTO> getAllGuests();

    boolean updateGuest(GuestDTO guestDTO);

    boolean deleteGuest(int guestId);
}