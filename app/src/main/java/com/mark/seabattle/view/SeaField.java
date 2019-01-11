package com.mark.seabattle.view;

import com.mark.seabattle.R;
import com.mark.seabattle.io.StoreObject;
import com.mark.seabattle.presenter.SeaBattlePresenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class  SeaField extends Activity {

    private byte boardSize;
    //private int colorStart =  getResources().getColor(R.color.colorStart);
    //private int colorMiss = getResources().getColor(R.color.colorMiss);
    //private int colorHit = getResources().getColor(R.color.colorHit);

    private int colorStart =  Color.LTGRAY;
    private Button[][] buttonGrid;
    private int [][] buttonColor;
    private static final int MENU_SHIPS = 1;
    private static final int MENU_SIZE = 2;

    TextView numberShips;
    SeaBattlePresenter presenter = new SeaBattlePresenter(this);
    //Expression expr = presenter::selectNumberShips;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardSize = presenter.getBoardSize();
        buttonGrid = new Button[boardSize][boardSize];
        buttonColor = new int[boardSize][boardSize];

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        int pixeldpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        int width_dp = (width_px/pixeldpi)*170;
        final float scale = this.getResources().getDisplayMetrics().density;
        width_dp = (int) (width_dp*scale);
        GridLayout gl = findViewById(R.id.field);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);
        params.width = width_dp/boardSize;
        params.height = params.width;

        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Button button = new Button(this);
                button.setLayoutParams(params);
                button.setId(10*(x+1) + (y+1));
                buttonGrid[x][y] = button;
                fieldUpdate(x, y, colorStart, true);
                gl.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        presenter.shot(v.getId());
                    }
                });
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem menuItem = menu.findItem(R.id.action_new);
        menu.add(Menu.NONE, MENU_SHIPS, Menu.NONE, " "+this.getString(R.string.number_ships)+" ").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, MENU_SIZE, Menu.NONE, " "+this.getString(R.string.ship_size)+" ").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                presenter.newGame();
                return true;

            case R.id.action_save:
                StoreObject storeObj = new StoreObject(buttonColor);
                presenter.saveFile(storeObj);
                return true;

            case R.id.action_load:
                presenter.loadGame();
                return true;

            case MENU_SHIPS:
                presenter.menu_ships();
                return true;

            case MENU_SIZE:
                presenter.menu_size();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dialog(final String[]str_files) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SeaField.this,  R.style.MyDialogTheme);
        builder.setTitle(this.getString(R.string.select_file));
        builder.setItems (str_files, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                loadGame(str_files[item]);
            }
        });
        builder.show();
    }

    public void alertDialog(String title,  int selected_item, final String[] array_select, ParamInterface setParam){
        AlertDialog.Builder builder = new AlertDialog.Builder(SeaField.this,  R.style.MyDialogTheme);
        builder.setTitle(title);
        builder.setNeutralButton(this.getString(R.string.back),
                (dialog, id) ->  dialog.cancel());
        String checkeditem = String.valueOf(selected_item);
        builder.setSingleChoiceItems(array_select , Arrays.asList(array_select).indexOf(checkeditem),
                new DialogInterface.OnClickListener() {
                    //@TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog,  int item) {
                        setParam.run(item);
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public interface ParamInterface {
        void run(int t);
    }

    public void loadGame(String filename){
        StoreObject loadObj = presenter.loadFile(filename);
        message(filename+ " "+ this.getString(R.string.loaded));
        buttonColor = loadObj.getButtonColor();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                fieldUpdate(x,y, buttonColor[x][y], false);
                if (buttonColor[x][y] == colorStart)
                    buttonGrid[x][y].setEnabled(true);
                else buttonGrid[x][y].setEnabled(false);
            }
        }
    }

    public void fieldUpdate(int x, int y, int color, boolean update_buttonColor){
        GradientDrawable dr = new GradientDrawable();
        dr.setCornerRadius(5);
        dr.setStroke(1, 0xFF000000);
        dr.setColor(color);
        buttonGrid[x][y].setBackground(dr);
        if (update_buttonColor)
            buttonColor[x][y]= color;
    }

    public void newGame() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                fieldUpdate(x,y, colorStart, true);
                buttonGrid[x][y].setEnabled(true);
            }
        }
        message(this.getString(R.string.new_game));
    }

    public void gameOver () {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                buttonGrid[x][y].setEnabled(false);
            }
        }
        message(this.getString(R.string.game_over));
    }

    public void shipKilled (List < Integer > area,int shipNumber){
        for (int cell : area) {
            //fieldUpdate(cell / 10 - 1, cell % 10 - 1, getResources().getColor(R.color.colorKill), true);
            fieldUpdate(cell / 10 - 1, cell % 10 - 1, ContextCompat.getColor(this, R.color.colorKill), true);
        }
        message("SHIP " + shipNumber + " KILLED");
    }

    public void hit ( int cell){
        fieldUpdate(cell / 10 - 1, cell % 10 - 1, ContextCompat.getColor(this, R.color.colorHIT), true);
    }

    public void miss ( int cell){
        fieldUpdate(cell / 10 - 1, cell % 10 - 1, ContextCompat.getColor(this, R.color.colorMiss), true);
        buttonGrid[cell / 10 - 1][cell % 10 - 1].setEnabled(false);
    }

    public void message(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
