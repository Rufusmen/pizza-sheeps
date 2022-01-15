package com.codingame.game;

import static java.lang.Math.sqrt;

public class Vector2 implements Cloneable{
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
            return new Vector2(x,y);
        }
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public void normalize(){
        double length = sqrt(x*x+y*y);
        x = x/length;
        y = y/length;
    }

    public void add(double x, double y) { this.x += x; this.y += y;}
    public void add(Vector2 v) { this.x += v.x; this.y += v.y;}
    public void sub(double x, double y) { this.x -= x; this.y -= y;}
    public void sub(Vector2 v) { this.x -= v.x; this.y -= v.y;}
    public void abs() {this.x = Math.abs(x); this.y = Math.abs(y);}
}