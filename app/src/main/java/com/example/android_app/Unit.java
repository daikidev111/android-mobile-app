package com.example.android_app;

public class Unit implements UnitInterface {
    private String Name;
    private String ID;

    public Unit(String name, String ID) {
        this.Name = name;
        this.ID = ID;
    }

    public String getName() {
        return this.Name;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public void setName(String name) {
        this.Name = name;
    }

    @Override
    public void showCode() {
        System.out.println("Unit Code: " + this.getID());
    }

    @Override
    public void showName() {
        System.out.println("Unit Name: " + this.getName());
    }
}
