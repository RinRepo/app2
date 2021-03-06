package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EsSearchDTO {
    @JsonProperty("index_name")
    private String indexName;

    @JsonProperty("raw_query")
    private String rawQuery;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("starting_record_number")
    private int startingRecordNumber;

    private List<SortDTO> sorts;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartingRecordNumber() {
        return startingRecordNumber;
    }

    public void setStartingRecordNumber(int startingRecordNumber) {
        this.startingRecordNumber = startingRecordNumber;
    }

    public List<SortDTO> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortDTO> sorts) {
        this.sorts = sorts;
    }
}
