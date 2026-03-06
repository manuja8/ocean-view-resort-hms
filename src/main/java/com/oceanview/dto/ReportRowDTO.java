package com.oceanview.dto;

public class ReportRowDTO {
    private String metric;
    private String value;

    public ReportRowDTO() {
    }

    public ReportRowDTO(String metric, String value) {
        this.metric = metric;
        this.value = value;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}