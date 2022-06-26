package com.fornary4.exercise.entity;

public class Judge {
    public String problem;
    public String answer;
    public String type;

    public Judge() {
    }

    public Judge(String problem, String answer, String type) {
        this.problem = problem;
        this.answer = answer;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Judge{" +
                "problem='" + problem + '\'' +
                ", answer='" + answer + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
