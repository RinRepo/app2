package com.lessons.models;

import java.util.List;

public class AddReportDTO {
    private String name;
    private Integer priority;
    private Integer reportType;
    private List<Integer> reportSources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public List<Integer> getReportSources() {
        return reportSources;
    }

    public void setReportSources(List<Integer> reportSources) {
        this.reportSources = reportSources;
    }
}