package com.codingame.game.entity;

import com.codingame.game.Cell;
import com.codingame.game.CellType;

public class Shed extends Cell {

    private boolean initiallyOwned;
    public int wool;
    public int dogs1;
    public int dogs2;

    public int x;
    public int y;

    public Shed(int x, int y, int owner) {
        initiallyOwned = owner != -1;
        this.x = x;
        this.y = y;
        this.owner = owner;
        type = CellType.SHED;
    }

    public void clearDogs() {
        dogs1 = 0;
        dogs2 = 0;
    }

    public void addDog(int owner) {
        if (owner == 0) {
            dogs1++;
        } else {
            dogs2++;
        }
    }

    public void updateOwnership() {
        if (!initiallyOwned) {
            if (dogs1 > dogs2) {
                owner = 0;
            } else if (dogs2 > dogs1) {
                owner = 1;
            } else if(dogs2 == 0){
                owner = -1;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %d %d %d", x, y, owner, wool, dogs1, dogs2);
    }


}
