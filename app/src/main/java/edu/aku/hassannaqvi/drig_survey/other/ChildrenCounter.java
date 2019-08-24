package edu.aku.hassannaqvi.drig_survey.other;

import java.io.Serializable;

public class ChildrenCounter implements Serializable {

    private int total = 0, totalCount = 0, totalBCount = 0, totalGCount = 0;

    public ChildrenCounter(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalBCount() {
        return totalBCount;
    }

    public void setTotalBCount(int totalBCount) {
        this.totalBCount = totalBCount;
    }

    public int getTotalGCount() {
        return totalGCount;
    }

    public void setTotalGCount(int totalGCount) {
        this.totalGCount = totalGCount;
    }
}
