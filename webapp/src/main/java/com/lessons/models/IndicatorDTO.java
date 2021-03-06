package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndicatorDTO {
    private Integer id;

    @JsonProperty("indicator_types")
    private Integer type;

    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
