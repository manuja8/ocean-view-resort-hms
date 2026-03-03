package com.oceanview.dao;

import com.oceanview.entity.Faq;

import java.util.List;

public interface FaqDAO {
    List<Faq> findAll();

    List<Faq> findActiveOnly();

    Faq findById(int id);

    int save(Faq faq, int createdByUserId);

    boolean update(Faq faq, int updatedByUserId);

    boolean delete(int id);
}