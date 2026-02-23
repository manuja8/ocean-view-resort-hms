package com.oceanview.entity;

public class Report {

    private int reportId;
    private String type;
    private String dateRange;
    private String data;

    public Report() {
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}