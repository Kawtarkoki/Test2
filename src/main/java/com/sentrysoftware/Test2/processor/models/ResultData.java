package com.sentrysoftware.Test2.processor.models;

public record ResultData(String name, double value){
    @Override
    public String name() {
        return name;
    }
    @Override
    public double value() {
        return value;
    }
}