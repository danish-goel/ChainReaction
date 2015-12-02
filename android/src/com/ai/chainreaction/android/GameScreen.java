package com.ai.chainreaction.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.chainreaction.ChainReaction;
import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities;
import com.ai.chainreaction.algorithms.GreedyAlgorithm;
import com.ai.chainreaction.algorithms.MCTS.ChainReactionPlayer;
import com.ai.chainreaction.algorithms.MCTS.ChainReactionState;
import com.ai.chainreaction.algorithms.MCTS.Mcts;
import com.ai.chainreaction.algorithms.MiniMax;
import com.ai.chainreaction.algorithms.RandomAlgorithm;
import com.ai.chainreaction.heuristics.ChainHeuristic;
import com.ai.chainreaction.stats.IStats;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameScreen extends AndroidApplication implements ChainReaction.GameCallback, IStats {

    ChainReaction chainReaction;
    public final String PREFS_NAME = "MyPrefsFile";

    private final Mcts<ChainReactionState, Utilities.Pos, ChainReactionPlayer> mcts = Mcts.initializeIterations(500);
    static boolean FIRSTHUMAN;
    static boolean SECONDHUMAN;
    static boolean BOTS;
    static boolean BOTHHUMAN;
    int firstAlgo;
    int secondAlgo;
    TextView player1_stats,player2_stats;

    //    int grid[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        getAlgoChoice();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        chainReaction = new com.ai.chainreaction.ChainReaction(this);
        FrameLayout fl=((FrameLayout) findViewById(R.id.fl));
                fl.addView(initializeForView(chainReaction, config));

        player1_stats=(TextView)findViewById(R.id.player1stats);
       player2_stats=(TextView)findViewById(R.id.player2stats);

//        grid=new int[chainReaction.boardRows][chainReaction.boardCols];
//        initalizeGrid(chainReaction.tiles);
        //Toast.makeText(GameScreen.this, "abcd", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSimulating();
            }
        }, 500);
    }

    int numMoves;
    static int turn;

    public void recur() {
        if (chainReaction.gameOver) return;
        ChainReaction.debug++;
        if (turn > 0) {
            RunAlgo(firstAlgo, turn);
        } else {
            RunAlgo(secondAlgo, turn);
        }
    }

    public void startSimulating() {
        numMoves = 0;
        turn = Tile.RED;
        chainReaction.turn = turn;
        nextMove();
    }

    public void programaticallyMoveMiniMax(int player) {
        Log.d("abcd", "minimax");
        MiniMax miniMax = new MiniMax(chainReaction, chainReaction.tiles, 4, new ChainHeuristic(true), this);
        Utilities.Pos pos = miniMax.getNextMove(player);
        int moveRow = pos.row;
        int moveColumn = pos.col;
        programaticallyMove(player, pos.row, pos.col);
    }

    public void programaticallyMoveMCTS(int player) {
        Log.d("abcd","mcts");
        Utilities.Pos pos = mcts.uctSearchWithExploration(new ChainReactionState(Utilities.initalizeGrid(chainReaction.tiles), (player + 1) / 2), 0.2);
        int moveRow=pos.row;
        int moveColumn=pos.col;
        programaticallyMove(player, pos.row, pos.col);
    }

    public void programaticallyGreedy(int player) {
        Log.d("abcd", "greedy");
        Utilities.Pos pos = new GreedyAlgorithm().getNextMove(Utilities.initalizeGrid(chainReaction.tiles), player);
        int moveRow = pos.row;
        int moveColumn = pos.col;
        programaticallyMove(player, pos.row, pos.col);
    }

    public void programaticallyMoveRandom(int player) {
        Log.d("abcd", "random");
        Utilities.Pos pos = new RandomAlgorithm().getNextMove(Utilities.initalizeGrid(chainReaction.tiles), player);
        int randomMoveRow = pos.row;
        int randomMoveColumn = pos.col;
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

    @Override
    public void nextMove() {
        numMoves++;
        turn *= -1;
        chainReaction.turn = turn;
        if (!chainReaction.gameOver) {
            if (turn > 0 && firstAlgo == 0)
                return;
            if (turn < 0 && secondAlgo == 0)
                return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recur();
                        }
                    }, 200);
                }
            });

        }
    }

    public void RunAlgo(int choice, int turn) {
        switch (choice) {
            case 1:
                programaticallyMoveRandom(turn);
                break;
            case 2:
                programaticallyMoveMiniMax(turn);
                break;
            case 3:
                programaticallyGreedy(turn);
                break;
            case 4:
                programaticallyMoveMCTS(turn);
                break;
        }
    }

    public void getAlgoChoice() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        String first = prefs.getString("firstAlgoName", "");
        String second = prefs.getString("secondAlgoName", "");
        firstAlgo = setAlgoChoice(first);
        secondAlgo = setAlgoChoice(second);
        if (firstAlgo == 0 && secondAlgo == 0) {
            BOTHHUMAN = true;
        } else if (firstAlgo == 0) {
            FIRSTHUMAN = true;
        } else if (secondAlgo == 0) {
            SECONDHUMAN = true;
        } else {
            BOTS = true;
        }

    }

    int setAlgoChoice(String algoName) {
        if (algoName.equalsIgnoreCase("human")) {
            return 0;
        }
        if (algoName.equalsIgnoreCase("random")) {
            return 1;
        } else if (algoName.equalsIgnoreCase("minimax")) {
            return 2;
        } else if (algoName.equalsIgnoreCase("greedy")) {
            return 3;
        }
        else if(algoName.equalsIgnoreCase("mcts"))
        {
            return 4;
        }
        return 0;
    }

    @Override
    public void pushStats(long timeTaken, int numStatesExpanded, int numMaxStatesInMemory) {
        int turn=-1;
        if(turn==-1) {
            StringBuilder sb = new StringBuilder();
            sb.append(" Player1");
            sb.append(" Time: " + timeTaken/1000+"s" + "\t");
            sb.append(" States: " + numStatesExpanded + "\t");
            player1_stats.setText(sb.toString());
        }
        else if(turn==1)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(" Player2");
            sb.append(" Time: " + timeTaken/1000+"s" + "\t");
            sb.append(" States: " + numStatesExpanded + "\t");
            player2_stats.setText(sb.toString());
        }

        Log.d("stats", "t:" + timeTaken + " sE:" + numStatesExpanded + " sM:" + numMaxStatesInMemory);
    }
}
