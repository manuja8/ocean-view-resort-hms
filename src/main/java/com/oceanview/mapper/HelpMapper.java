package com.oceanview.mapper;

import com.oceanview.dto.HelpDTO;
import com.oceanview.entity.Help;

public class HelpMapper {

    public static HelpDTO toDTO(Help help) {
        if (help == null) return null;

        HelpDTO dto = new HelpDTO();
        dto.setId(help.getId());
        dto.setQuestion(help.getQuestion());
        dto.setAnswer(help.getAnswer());
        dto.setCategory(help.getCategory());

        return dto;
    }

    public static Help toEntity(HelpDTO dto) {
        if (dto == null) return null;

        Help help = new Help();
        help.setId(dto.getId());
        help.setQuestion(dto.getQuestion());
        help.setAnswer(dto.getAnswer());
        help.setCategory(dto.getCategory());

        return help;
    }
}