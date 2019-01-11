package com.mark.seabattle.presenter;

import com.mark.seabattle.R;
import com.mark.seabattle.model.ShipFactory;
import com.mark.seabattle.view.SeaField;
import com.mark.seabattle.io.FileIO;
import com.mark.seabattle.io.StoreObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SeaBattlePresenter {
     private FileIO fileIO = new FileIO();
     private SeaField field;
     private ShipFactory factory;
     private byte boardSize;
     private int numberShips;
     private int maxShipSize;
     private List<Ship> ships;

    public SeaBattlePresenter(SeaField field) {
        this.field = field;
        this.factory = new ShipFactory();
        numberShips = Integer.parseInt(factory.getNumShipsArray()[factory.getNumShipsArray().length-1]);
        maxShipSize = Integer.parseInt(factory.getMaxShipsArray()[factory.getMaxShipsArray().length-1]);
        boardSize = factory.getBoardSize();
        ships = factory.createFleet(numberShips, maxShipSize);
    }

    public Byte getBoardSize() { return boardSize; }

    public void newGame(){
        ships = factory.createFleet(numberShips, maxShipSize);
        field.newGame();
    }

    public void shot(int cellInt){
        String result;
        field.miss(cellInt);

        for (Ship ship: ships) {
            result = ship.shot(cellInt);
            if (result.equals("HIT")) {
                field.hit(cellInt);
                return;
            }
            if (result.equals("KILLED")) {
                shipKilled(ship);
                return;
            }
        }
     }

    private void shipKilled(Ship ship) {
        field.shipKilled(ship.getArea(),ship.getNumber());
        ships.remove(ship);
        if (ships.isEmpty()) {
            field.gameOver();
        }
    }

     public void saveFile(StoreObject storeObj){
        storeObj.setShips(ships);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String filename = sdf.format(new Date());
        try {
            fileIO.save(field, storeObj, filename);
        }
        catch (IOException e) {
            field.message("OШИБКА СОХРАНЕНИЯ!");
        }
        field.message("File " +filename + " saved");
     }

     public StoreObject loadFile(String name){
        StoreObject loadObj = null;
        try {
            loadObj = fileIO.load(field, name);
        } catch (IOException | ClassNotFoundException e) {
            field.message("OШИБКА ЗАГРУЗКИ ФАЙЛА!");
        }
        if (loadObj != null) {
            ships = loadObj.getShips();
        }
        return loadObj;
     }


     public void loadGame() {
        File directory = new File(String.valueOf(field.getFilesDir()));
        File[] files = (directory.listFiles());
        if (files.length == 0) {
            field.message("No Saved Files");
        }
        else if (files.length == 1) {
            field.loadGame(files[0].getName());
        }
        else {
            String[] str_files = new String[files.length];
            for (int i = 0; i < files.length; i++)
                str_files[i]  = files[i].getName();
            field.dialog(str_files);
        }
     }

     void setNumberShips(int number){numberShips = Integer.parseInt(factory.getNumShipsArray()[number]);}

     void setMaxShipSize(int number){maxShipSize = Integer.parseInt(factory.getMaxShipsArray()[number]);}

     public void menu_ships(){
        field.alertDialog(field.getString(R.string.select_number),numberShips, factory.getNumShipsArray(),this::setNumberShips);
     }

     public void menu_size(){
        field.alertDialog(field.getString(R.string.select_size),maxShipSize, factory.getMaxShipsArray(),this::setMaxShipSize);
     }
}
