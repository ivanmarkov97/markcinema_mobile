package com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Seance;

/**
 * Created by Ivan on 27.12.2017.
 */

public class Ticket {

    private Seance seance;
    private int place;
    private int row;
    private boolean isFree;

    public Ticket(Seance seance, int place, int row, boolean isFree) {
        this.seance = seance;
        this.place = place;
        this.row = row;
        this.isFree = isFree;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
