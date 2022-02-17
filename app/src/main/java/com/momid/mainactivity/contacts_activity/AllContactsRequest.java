package com.momid.mainactivity.contacts_activity;

public class AllContactsRequest {

    private int page = 1;
    private int pageSize = 35;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
