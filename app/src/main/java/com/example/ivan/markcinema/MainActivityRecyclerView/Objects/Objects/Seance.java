package com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects;

import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Film;
import com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects.Hall;

/**
 * Created by Ivan on 27.12.2017.
 */

public class Seance {
    private int id;
    private String time;
    private int cost;
    private String hall;
    private String film;

    public Seance(int id, String time, int cost, String hall, String film) {
        this.id = id;
        this.time = time;
        this.cost = cost;
        this.hall = hall;
        this.film = film;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }
}
