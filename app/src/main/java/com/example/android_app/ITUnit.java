package com.example.android_app;

public class ITUnit extends Unit {
    public String software;

    public ITUnit(String name, String id, String software) {
        super(name, id);
        this.software = software;
    }

    public String getSoftware() {
        return this.software;
    }

    public void setSoftware() {
        this.software = software;
    }
}
