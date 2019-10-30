package com.example.aes_pos_wallet.vo;

import java.util.ArrayList;

public class TransactionPage {
    private int pageSize;
    private int totalCount;
    private int totalPageCount;
    private int pageNo;
    private int previousPage;
    private ArrayList<Page> pageItems;
    private ArrayList<TransactionVo> resultItems;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public ArrayList<Page> getPageItems() {
        return pageItems;
    }

    public void setPageItems(ArrayList<Page> pageItems) {
        this.pageItems = pageItems;
    }

    public ArrayList<TransactionVo> getResultItems() {
        return resultItems;
    }

    public void setResultItems(ArrayList<TransactionVo> resultItems) {
        this.resultItems = resultItems;
    }

    class Page {
        String pageLabel;
        String pageFlag;
    }

}
