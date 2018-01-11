package com.example.ivan.markcinema.MainActivityRecyclerView.Objects.Objects;

/**
 * Created by Ivan on 27.12.2017.
 */

public class Film {
    private int id;
    private String name;
    private String description;
    private int age;
    private String country;
    private String producer;
    private int duration;
    private String genre;
    private String image;

    public Film(int id, String name, String description, int age, String country, String producer, int duration, String genre, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.age = age;
        this.country = country;
        this.producer = producer;
        this.duration = duration;
        this.genre = genre;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
