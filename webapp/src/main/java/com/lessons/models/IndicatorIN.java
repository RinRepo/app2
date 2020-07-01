package com.lessons.models;

import java.util.List;

public class IndicatorIN {

    private int pagesize;
    private int startingRecordNumber;
    private List<SortDTO> sorting;

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getStartingRecordNumber() {
        return startingRecordNumber;
    }

    public void setStartingRecordNumber(int startingRecordNumber) {
        this.startingRecordNumber = startingRecordNumber;
    }

    public List<SortDTO> getSorting() {
        return sorting;
    }

    public void setSorting(List<SortDTO> sorting) {
        this.sorting = sorting;
    }
}
