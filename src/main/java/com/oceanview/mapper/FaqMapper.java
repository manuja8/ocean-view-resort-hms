package com.oceanview.mapper;

import com.oceanview.dto.FaqDTO;
import com.oceanview.entity.Faq;

public class FaqMapper {

    public static FaqDTO toDTO(Faq f) {
        if (f == null) return null;
        FaqDTO dto = new FaqDTO();
        dto.setFaqId(f.getFaqId());
        dto.setQuestion(f.getQuestion());
        dto.setAnswer(f.getAnswer());
        dto.setActive(f.isActive());
        return dto;
    }

    public static Faq toEntity(FaqDTO dto) {
        if (dto == null) return null;
        Faq f = new Faq();
        f.setFaqId(dto.getFaqId());
        f.setQuestion(dto.getQuestion());
        f.setAnswer(dto.getAnswer());
        f.setActive(dto.isActive());
        return f;
    }
}