package com.aver.notetaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private Long id;
    private String joke;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getJoke() {
        return this.joke;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    @Override
    public String toString() {
        return "Value{" + "id=" + id + ", joke='" + joke + '\'' + '}';
    }
}