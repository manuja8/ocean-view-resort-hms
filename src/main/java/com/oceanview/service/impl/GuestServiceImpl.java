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

    private final GuestDAO guestDAO = new GuestDAOImpl();

    @Override
    public int addGuest(GuestDTO dto, int createdByUserId) {
        validate(dto);
        Guest g = GuestMapper.toEntity(dto);
        return guestDAO.save(g, createdByUserId);
    }

    @Override
    public boolean updateGuest(GuestDTO dto, int updatedByUserId) {
        validate(dto);
        Guest g = GuestMapper.toEntity(dto);
        return guestDAO.update(g, updatedByUserId);
    }

    @Override
    public GuestDTO getGuestById(int guestId) {
        Guest g = guestDAO.findById(guestId);
        return GuestMapper.toDTO(g);
    }

    @Override
    public List<GuestDTO> getAllGuests(String q) {
        List<Guest> list = (q == null || q.trim().isEmpty()) ? guestDAO.findAll() : guestDAO.search(q);
        return list.stream().map(GuestMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean deleteGuest(int guestId) {
        return guestDAO.delete(guestId);
    }

    private void validate(GuestDTO dto) {
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty())
            throw new IllegalArgumentException("Full name is required");
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty())
            throw new IllegalArgumentException("Address is required");
        if (dto.getContactNo() == null || dto.getContactNo().trim().isEmpty())
            throw new IllegalArgumentException("Contact number is required");
        if (dto.getIdentificationType() == null || dto.getIdentificationType().trim().isEmpty())
            throw new IllegalArgumentException("Identification type is required");
        if (dto.getIdentificationNo() == null || dto.getIdentificationNo().trim().isEmpty())
            throw new IllegalArgumentException("Identification number is required");

        dto.setFullName(dto.getFullName().trim());
        dto.setAddress(dto.getAddress().trim());
        dto.setContactNo(dto.getContactNo().trim());
        dto.setIdentificationType(dto.getIdentificationType().trim());
        dto.setIdentificationNo(dto.getIdentificationNo().trim());
        if (dto.getEmail() != null) dto.setEmail(dto.getEmail().trim());
        if (dto.getGender() != null) dto.setGender(dto.getGender().trim());
    }
}