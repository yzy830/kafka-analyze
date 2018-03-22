package com.gerald.kafka;

public enum Topics {
    TEST("test");
    
    public final String name;
    
    private Topics(String name) {
        this.name = name;
    }
}
