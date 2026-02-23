package com.oceanview.mapper;

import com.oceanview.dto.ReportDTO;
import com.oceanview.entity.Report;

public class ReportMapper {

    public static ReportDTO toDTO(Report report) {
        if (report == null) return null;

        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setType(report.getType());
        dto.setDateRange(report.getDateRange());
        dto.setData(report.getData());

        return dto;
    }

    public static Report toEntity(ReportDTO dto) {
        if (dto == null) return null;

        Report report = new Report();
        report.setReportId(dto.getReportId());
        report.setType(dto.getType());
        report.setDateRange(dto.getDateRange());
        report.setData(dto.getData());

        return report;
    }
}