package com.ai.chainreaction.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.ai.chainreaction.ChainReaction;
import com.ai.chainreaction.Greedy;
import com.ai.chainreaction.MiniMax;
import com.ai.chainreaction.RandomTemp;
import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameScreen extends AndroidApplication implements ChainReaction.GameCallback {

    ChainReaction chainReaction;
    public final String PREFS_NAME = "MyPrefsFile";


    int firstAlgo;
    int secondAlgo;

    //    int grid[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAlgoChoice();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        chainReaction = new com.ai.chainreaction.ChainReaction(this);
        initialize(chainReaction, config);

//        grid=new int[chainReaction.boardRows][chainReaction.boardCols];
//        initalizeGrid(chainReaction.tiles);
        //Toast.makeText(GameScreen.this, "abcd", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSimulating();
            }
        }, 1000);
    }

    int numMoves;
    int turn;
    static int myTurn = 1;

    public void recur() {
        numMoves++;
        ChainReaction.debug++;
        turn *= -1;
        if (turn > 0) {
//            programaticallyGreedy(turn);
            RunAlgo(firstAlgo,turn);
        }
        else {
            RunAlgo(secondAlgo,turn);
        }
        if (!chainReaction.gameOver) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recur();
                }
            }, 500);
        }
    }

    public void startSimulating() {
        numMoves = 0;
        turn = Tile.RED;
        chainReaction.turn = turn;
        recur();
    }

    public void programaticallyMoveMiniMax(int player) {
        MiniMax miniMax = new MiniMax(chainReaction, chainReaction.tiles, 3);
        Utilities.Pos pos = miniMax.getBestMove(player);
        int moveRow=pos.row;
        int moveColumn=pos.col;
        programaticallyMove(player, pos.row, pos.col);
    }

    public void programaticallyGreedy(int player) {
        Utilities.Pos pos = Greedy.getNextCoord(Utilities.initalizeGrid(chainReaction.tiles),player);
        int moveRow=pos.row;
        int moveColumn=pos.col;
        programaticallyMove(player, pos.row, pos.col);
    }

    public void programaticallyMoveRandom(int player) {
        Utilities.Pos pos = RandomTemp.getNextCoord(chainReaction.tiles, player);
        int randomMoveRow=pos.row;
        int randomMoveColumn=pos.col;
        programaticallyMove(player, pos.row, pos.col);
    }


    public void programaticallyMove(int player, int x, int y) {
        Tile tile = chainReaction.tiles[x][y];
        tile.player = player;
        chainReaction.inputListener.tileClicked(player, x, y);//touchDown((int) tile.x, (int) tile.y, 0, 0);
    }

    @Override
    public void gameOver() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "loser loser chicken dinner :P ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RunAlgo(int choice,int turn)
    {
        switch (choice)
        {
            case 0:
                programaticallyMoveRandom(turn);
                break;
            case 1:
                programaticallyMoveMiniMax(turn);
                break;
            case 2:
                programaticallyGreedy(turn);
                break;
            default:
                break;
        }
    }

    public void getAlgoChoice() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        String first=prefs.getString("firstAlgoName","");
        String second=prefs.getString("secondAlgoName", "");
        setAlgoChoice(first, firstAlgo);
        setAlgoChoice(second,secondAlgo);

    }

    public void setAlgoChoice(String algoName,int choice)
    {
        if(algoName.equalsIgnoreCase("random"))
        {
            choice=0;
        }
        else if(algoName.equalsIgnoreCase("minimax"))
        {
            choice=1;
        }
        else if (algoName.equalsIgnoreCase("greedy"))
        {
            choice=2;
        }
    }

}
