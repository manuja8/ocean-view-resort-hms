package com.oceanview.service;

import com.oceanview.dto.FaqDTO;

import java.util.List;

public interface FaqService {
    List<FaqDTO> getAll();

    List<FaqDTO> getActiveFaqs();

    FaqDTO getById(int id);

    int create(FaqDTO dto, int createdByUserId);

    void update(FaqDTO dto, int updatedByUserId);

    void delete(int id);


}