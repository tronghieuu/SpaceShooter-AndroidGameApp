package com.hieulam.spaceshooter;

public class Rank {

    private String id, name, highScore, image, age, gender;
    private int rank;

    public Rank(String id, String name, String highScore, String image, String age, String gender, int rank) {
        this.id = id;
        this.name = name;
        this.highScore = highScore;
        this.image = image;
        this.age = age;
        this.gender = gender;
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHighScore() {
        return highScore;
    }

    public void setHighScore(String highScore) {
        this.highScore = highScore;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
