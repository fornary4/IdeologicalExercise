package com.fornary4.exercise.entity;

public class Choose {
    public String problem;
    public String A;
    public String B;
    public String C;
    public String D;
    public String answer;
    public String type;


    public Choose(String problem, String a, String b, String c, String d, String answer, String type) {
        this.problem = problem;
        A = a;
        B = b;
        C = c;
        D = d;
        this.answer = answer;
        this.type = type;
    }

    public Choose() {
    }

    @Override
    public String toString() {
        return "Choose{" +
                "problem='" + problem + '\'' +
                ", A='" + A + '\'' +
                ", B='" + B + '\'' +
                ", C='" + C + '\'' +
                ", D='" + D + '\'' +
                ", answer='" + answer + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
