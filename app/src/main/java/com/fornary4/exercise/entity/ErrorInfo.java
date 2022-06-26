package com.fornary4.exercise.entity;

public class ErrorInfo {
    public int id;
    public int type;
    public int position;
    public int count;

    public ErrorInfo() {

    }

    public ErrorInfo(int type, int position) {
        this.type = type;
        this.position = position;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "id=" + id +
                ", type=" + type +
                ", position=" + position +
                ", count=" + count +
                '}';
    }
}
