package com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects;

/**
 * Created by Ivan on 27.12.2017.
 */

public class Hall {
    private String name;
    private int numRows;
    private int numColums;
    private boolean is_valid;

    public Hall(String name, int numRows, int numColums, boolean is_valid) {
        this.name = name;
        this.numRows = numRows;
        this.numColums = numColums;
        this.is_valid = is_valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumColums() {
        return numColums;
    }

    public void setNumColums(int numColums) {
        this.numColums = numColums;
    }

    public boolean is_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }
}
