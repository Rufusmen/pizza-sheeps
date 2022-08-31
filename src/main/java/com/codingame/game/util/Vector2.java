package com.codingame.game.util;

import static java.lang.Math.sqrt;

import java.util.Random;

public class Vector2 implements Cloneable {

    private double x;
    private double y;

    public Vector2() {
        this.x = -1;
        this.y = -1;
    }

    @Override
    public Vector2 clone() {
        try {
            return (Vector2) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Vector2(x, y);
        }
    }

    @Override
    public String toString() {
        return String.format("%f %f", x, y);
    }

    public void checkInBound(double boundX, double boundY) {
        if (x < 0) {
            x = 1e-6;
        }
        if (y < 0) {
            y = 1e-6;
        }
        if (x > boundX) {
            x = boundX - 1e-6;
        }
        if (y > boundY) {
            y = boundY - 1e-6;
        }
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void normalize() {
        double length = sqrt(x * x + y * y);
        if(Math.abs(length)< 1e-5)return;
        x = x / length;
        y = y / length;
    }

    public void negateY() {
        y = -y;
    }

    public void negateX() {
        x = -x;
    }

    public double dist(Vector2 v) {
        double deltaX = this.x - v.x;
        double deltaY = this.y - v.y;
        return sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public Vector2 away(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    public boolean inRadius(Vector2 center, double rad) {
        return dist(center) < rad;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void sub(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void sub(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void abs() {
        this.x = Math.abs(x);
        this.y = Math.abs(y);
    }

    public Vector2 addRand(Random random) {
        x += random.nextDouble() * 0.2 - 0.1;
        y += random.nextDouble() * 0.2 - 0.1;
        return this;
    }
}