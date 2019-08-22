package edu.aku.hassannaqvi.drig_survey.other;

import java.io.Serializable;

public class ChildrenCounter implements Serializable {

    private int total = 0, totalCount = 0, totalBoy = 0, totalGirl = 0, totalBCount = 0, totalGCount = 0;

    public ChildrenCounter(int total, int totalBoy, int totalGirl) {
        this.total = total;
        this.totalBoy = totalBoy;
        this.totalGirl = totalGirl;
    }

    public ChildrenCounter(int total, int totalCount, int totalBoy, int totalGirl, int totalBCount, int totalGCount) {
        this.total = total;
        this.totalCount = totalCount;
        this.totalBoy = totalBoy;
        this.totalGirl = totalGirl;
        this.totalBCount = totalBCount;
        this.totalGCount = totalGCount;
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

    public int getTotalBoy() {
        return totalBoy;
    }

    public void setTotalBoy(int totalBoy) {
        this.totalBoy = totalBoy;
    }

    public int getTotalGirl() {
        return totalGirl;
    }

    public void setTotalGirl(int totalGirl) {
        this.totalGirl = totalGirl;
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
