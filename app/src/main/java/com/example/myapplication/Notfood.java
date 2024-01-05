package com.example.myapplication;

public class Notfood {

    String name;
    Notfood()
    {

    }

    public Notfood(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Notfood{" +
                "name='" + name + '\'' +
                '}';
    }
}
