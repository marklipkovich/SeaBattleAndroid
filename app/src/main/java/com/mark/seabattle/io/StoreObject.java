package com.mark.seabattle.io;

import com.mark.seabattle.presenter.Ship;
import java.io.Serializable;
import java.util.List;

public class StoreObject implements Serializable {
    private  List<Ship> ships;
    private  int[][] buttonColor;

    public StoreObject(List<Ship> ships, int[][] buttonColor) {
        this.ships = ships;
        this.buttonColor = buttonColor;
    }

    public StoreObject(int[][] buttonColor) {
        this.buttonColor = buttonColor;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int[][] getButtonColor() {
        return buttonColor;
    }

}

