package com.ai.chainreaction.android;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ai.chainreaction.ChainReaction;
import com.ai.chainreaction.RandomTemp;
import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Timer;

public class GameScreen extends AndroidApplication implements ChainReaction.GameCallback {

    ChainReaction cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        cr = new com.ai.chainreaction.ChainReaction(this);
        initialize(cr, config);
        //Toast.makeText(GameScreen.this, "abcd", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSimulating();
            }
        },1000);
    }

    int numMoves;
    int turn;

    public void recur()
    {
        //TODO shift to activity gameover from class gameover
        numMoves++;
        ChainReaction.debug++;
        programaticallyMove(turn);
        if(!cr.gameOver)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recur();
                }
            },200);
    }

    public void startSimulating() {
        numMoves = 0;
        turn = Tile.RED;
        cr.turn = turn;
        recur();
    }

    public void programaticallyMove(int player) {
        Utilities.Pos pos = RandomTemp.getNextCoord(cr.tiles, player);
        programaticallyMove(player,pos.row,pos.col);
    }

    public void programaticallyMove(int player, int x, int y) {
        Tile tile = cr.tiles[x][y];
        tile.player = player;
        cr.inputListener.touchDown((int) tile.x, (int) tile.y, 0, 0);
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
