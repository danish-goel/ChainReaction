package com.ai.chainreaction.Simulations;

import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities;
import com.ai.chainreaction.heuristics.ChainHeuristic;
import com.ai.chainreaction.heuristics.IHeuristic;
import com.ai.chainreaction.stats.IStats;

import sun.nio.cs.ext.ISCII91;

/**
 * Created by Pranav on 02-12-2015.
 */
public class Simulate implements IStats{

    @Override
    public void pushStats(String algo, int turn, long timeTaken, int numStatesExpanded, int numMaxStatesInMemory) {
//        System.out.println(algo+" " + timeTaken + " " + numStatesExpanded);
    }


    public static void randomvsrandom(int num_iter) {
        int win_Blue = 0;
        int win_Red = 0;
        for (int i = 0; i < num_iter; i++) {

            int[][] grid = new int[5][5];
            int winner = 0;
            while (winner == 0) {
                Utilities.programaticallyMoveRandom(grid, Tile.RED);
                winner = Utilities.checkWinnerIfExists(grid);
                if (winner != 0)
                    break;
                Utilities.programaticallyMoveRandom(grid, Tile.BLUE);
                winner = Utilities.checkWinnerIfExists(grid);
            }
            if (winner == Tile.RED) {
                win_Red++;
                System.out.println("Iter " + i + " : Winner RED");
            } else {
                win_Blue++;
                System.out.println("Iter " + i + " : Winner BLUE");
            }
        }

        System.out.println("Won by Blue: " + win_Blue);
        System.out.println("Won by Red: "+win_Red);
    }
    public static void minimaxvsminimax(int num_iter, int depth1, int depth2, int h1, int h2, boolean prune1, boolean prune2, IStats stats1, IStats stats2)
    {
        int win_Blue=0;
        int win_Red=0;
        for (int i=0; i<num_iter; i++)
        {

            int[][] grid = new int[5][5];
            int ctr=0;
            int winner=0;
            while (true)
            {
                Utilities.programaticallyMoveMiniMax(grid, Tile.RED, depth1, prune1, h1, stats1);
                winner = Utilities.checkWinnerIfExists(grid);
                if (winner!=0)
                    break;
                Utilities.programaticallyMoveMiniMax(grid, Tile.BLUE, depth2, prune2, h2, stats2);
                winner = Utilities.checkWinnerIfExists(grid);
                if (winner!=0)
                    break;
                ctr++;
            }

            if (winner==Tile.RED) {
                win_Red++;
                System.out.println("ctr " + ctr + " Iter " + i + " : Winner RED");
            }
            else {
                win_Blue++;
                System.out.println("Iter " + i + " : Winner BLUE");
            }
        }

        System.out.println("Won by Blue: " + win_Blue);
        System.out.println("Won by Red: "+win_Red);
    }

    public static void minimaxvsrandom(int num_iter, int depth, int h, boolean prune, IStats stats)
    {
        int win_Blue=0;
        int win_Red=0;
        for (int i=0; i<num_iter; i++)
        {

            int[][] grid = new int[5][5];
            int ctr=0;
            int winner=0;
            while (true)
            {
                Utilities.programaticallyMoveMiniMax(grid, Tile.RED, depth, prune, h, stats);
                winner = Utilities.checkWinnerIfExists(grid);
                if (winner!=0)
                    break;
                Utilities.programaticallyMoveRandom(grid, Tile.BLUE);
                winner = Utilities.checkWinnerIfExists(grid);
                if (winner!=0)
                    break;
                ctr++;
            }

            if (winner==Tile.RED) {
                win_Red++;
                System.out.println(" Iter " + i + " : Winner RED");
            }
            else {
                win_Blue++;
                System.out.println("Iter " + i + " : Winner BLUE");
            }
        }

        System.out.println("Won by Blue: " + win_Blue);
        System.out.println("Won by Red: "+win_Red);
    }



    public static void main(String[] args) {

        Simulate s = new Simulate();
        minimaxvsrandom(100, 4, 0, true, s);
        minimaxvsrandom(100, 4, 0, false, s);
        minimaxvsrandom(100, 4, 1, true, s);
        minimaxvsrandom(100, 4, 1, false, s);

        minimaxvsrandom(100, 3, 0, true, s);
        minimaxvsrandom(100, 3, 0, false, s);
        minimaxvsrandom(100, 3, 1, true, s);
        minimaxvsrandom(100, 3, 1, false, s);



//        minimaxvsminimax(100, 3, 3, 0, 1, true, true, s, s);
//        randomvsrandom(100);






    }
}
