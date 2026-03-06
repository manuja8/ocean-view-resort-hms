package com.oceanview.service;

import com.oceanview.dto.ComplaintDTO;

import java.util.List;

public interface ComplaintService {
    int create(ComplaintDTO dto, int userId);

    boolean update(ComplaintDTO dto, int userId);

    ComplaintDTO getById(int id);

    List<ComplaintDTO> search(String q, String status, String priority);

    boolean delete(int id); // admin-only
}