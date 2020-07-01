package com.lessons.models;

import java.util.List;

public class FilterPopsDTO {

    private Integer pagesize;
    private List<FilterChildDTO> filters;
    private Integer offset;

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public List<FilterChildDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterChildDTO> filters) {
        this.filters = filters;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
