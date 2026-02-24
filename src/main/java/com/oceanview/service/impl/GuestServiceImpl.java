package com.oceanview.service.impl;

import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dto.GuestDTO;
import com.oceanview.entity.Guest;
import com.oceanview.mapper.GuestMapper;
import com.oceanview.service.GuestService;

import java.util.List;
import java.util.stream.Collectors;

public class GuestServiceImpl implements GuestService {

    private GuestDAO guestDAO = new GuestDAOImpl();

    @Override
    public int addGuest(GuestDTO guestDTO) {
        Guest guest = GuestMapper.toEntity(guestDTO);
        return guestDAO.save(guest);
    }

    @Override
    public GuestDTO getGuestById(int guestId) {
        Guest guest = guestDAO.findById(guestId);
        if (guest == null) return null;
        return GuestMapper.toDTO(guest);
    }

    @Override
    public List<GuestDTO> getAllGuests() {
        return guestDAO.findAll()
                .stream()
                .map(GuestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateGuest(GuestDTO guestDTO) {
        try {

            Guest guest = GuestMapper.toEntity(guestDTO);

            guestDAO.update(guest);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean deleteGuest(int guestId) {
        return guestDAO.delete(guestId);
    }
}