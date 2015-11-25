package com.ai.chainreaction.android;

import android.os.Bundle;
import android.os.Handler;
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

    //    int grid[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            programaticallyMoveMiniMax(turn);
        }
        else {
            programaticallyMoveRandom(turn);
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
}
