package com.mark.seabattle.model;

import com.mark.seabattle.presenter.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipFactory {

    private static final int[]adjacentCell = {-11, -10, -9, -1, 0, 1, 9, 10, 11};
    private static final int maxBoard = 9;
    private static final int minBoard = 5;
    private Byte boardSize = 8;
    private String[] numShipsArray = new String[] {"2","3","4","5"};
    private String[] maxShipsArray = new String[] {"2","4","5"};
    private Random rand = new Random();
    private List<Integer> fieldArray = new ArrayList<>();

    public String[] getNumShipsArray() { return numShipsArray; }
    public String[] getMaxShipsArray() { return maxShipsArray; }
    public Byte getBoardSize() { return boardSize; }

    public List<Ship> createFleet(int numberShips, int shipSizeMax)  {
        if(boardSize> maxBoard) {
          throw new IllegalArgumentException("BoardSize can't be bigger than " + maxBoard + " (received " + boardSize + ")");
        }
        if(boardSize< minBoard) {
          throw new IllegalArgumentException("BoardSize can't be smaller than " + minBoard + " (received " + boardSize + ")");
        }

        List<Ship> ships = new ArrayList<>();
        for (int i = 1; i <=boardSize; i++)
            for (int j = 1; j <=boardSize; j++)
                fieldArray.add(10 * i + j);

        for (int n = 0; n < numberShips; n++) {
            ships.add(createShip(rand.nextInt(shipSizeMax)+1, n+1));
        }
        return ships;
    }

    private Ship createShip(int shipLength, int number) {
        byte step;
        boolean shipCreated = false;
        Ship ship = null;
        while (!shipCreated) {
            List<Integer> shipArea = new ArrayList<>();
            int shipCell= fieldArray.get(rand.nextInt(fieldArray.size()));
            shipArea.add(shipCell);
            if (Math.random() < 0.5) {
                step = 1;
            } else {
                step = 10;
            }
            for (int i = 0; i < shipLength - 1; i++) {
              shipCell = shipCell + step;
                shipArea.add(shipCell);
            }
            if (fieldArray.containsAll(shipArea)) {
                shipCreated = true;
                for (int i : adjacentCell) {
                    for (int cell: shipArea) {
                        fieldArray.remove(Integer.valueOf(cell + i));
                    }
                }
                for (int cell: shipArea) {
                    System.out.print(cell + " ");
                }
                System.out.println();
                ship = new Ship(shipArea, number);
            }
        }
        return ship;
    }
}

