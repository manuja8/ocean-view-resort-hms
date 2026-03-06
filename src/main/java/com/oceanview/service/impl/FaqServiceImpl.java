package com.oceanview.service.impl;

import com.oceanview.dao.FaqDAO;
import com.oceanview.dao.impl.FaqDAOImpl;
import com.oceanview.dto.FaqDTO;
import com.oceanview.entity.Faq;
import com.oceanview.mapper.FaqMapper;
import com.oceanview.service.FaqService;

import java.util.ArrayList;
import java.util.List;

public class FaqServiceImpl implements FaqService {

    private final FaqDAO faqDAO = new FaqDAOImpl();

    @Override
    public List<FaqDTO> getAll() {
        List<Faq> list = faqDAO.findAll();
        List<FaqDTO> out = new ArrayList<>();
        for (Faq f : list) out.add(FaqMapper.toDTO(f));
        return out;
    }

    @Override
    public List<FaqDTO> getActiveFaqs() {
        List<Faq> list = faqDAO.findActiveOnly();
        List<FaqDTO> out = new ArrayList<>();

        for (Faq f : list) {
            out.add(FaqMapper.toDTO(f));
        }

        return out;
    }

    @Override
    public FaqDTO getById(int id) {
        return FaqMapper.toDTO(faqDAO.findById(id));
    }

    @Override
    public int create(FaqDTO dto, int createdByUserId) {
        validate(dto);
        return faqDAO.save(FaqMapper.toEntity(dto), createdByUserId);
    }

    @Override
    public void update(FaqDTO dto, int updatedByUserId) {
        validate(dto);
        if (dto.getFaqId() <= 0) throw new IllegalArgumentException("Invalid FAQ id");
        boolean ok = faqDAO.update(FaqMapper.toEntity(dto), updatedByUserId);
        if (!ok) throw new IllegalStateException("FAQ update failed (not found?)");
    }

    @Override
    public void delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid FAQ id");
        boolean ok = faqDAO.delete(id);
        if (!ok) throw new IllegalStateException("FAQ delete failed (not found?)");
    }

    private void validate(FaqDTO dto) {
        if (dto.getQuestion() == null || dto.getQuestion().trim().isEmpty())
            throw new IllegalArgumentException("Question is required");
        if (dto.getAnswer() == null || dto.getAnswer().trim().isEmpty())
            throw new IllegalArgumentException("Answer is required");

        dto.setQuestion(dto.getQuestion().trim());
        dto.setAnswer(dto.getAnswer().trim());
    }


}