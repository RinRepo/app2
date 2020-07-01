package com.lessons.sync.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "took", "items" })   // Tell Jackson to ignore the "took" and "items" fields
public class ErrorsDTO {

    private boolean errors;

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }
}