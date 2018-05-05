package com.takwira.hamza.takwira.listview;

/**
 * Created by hamza on 21/08/17.
 */

public class Card {
    private String title;
    private String text;

    public Card(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getLine1() {
        return title;
    }

    public String getLine2() {
        return text;
    }

}