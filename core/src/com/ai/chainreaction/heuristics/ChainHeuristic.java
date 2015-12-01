package com.ai.chainreaction.heuristics;

import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities;

import java.util.Map;

/**
 * Created by mg on 1/12/15.
 */
public class ChainHeuristic implements IHeuristic {

    boolean includeEnemyOrbsInChain;

    public ChainHeuristic(boolean includeEnemyOrbsInChain) {
        this.includeEnemyOrbsInChain=includeEnemyOrbsInChain;
    }

    /**
     * Doesn't include enemy orbs in chain by default
     */
    public ChainHeuristic() {
        this.includeEnemyOrbsInChain = false;
    }

    @Override
    public int getHeuristicValue(int[][] grid, int color) {

        int myOrbs = 0;
        int enemyOrbs = 0;
        int score = 0;
        boolean vulnerable = true;
        int numRows = grid.length;
        int numColumns = grid[0].length;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int curColor = (int) Math.signum(grid[row][col]);
                if (curColor == color) {
                    myOrbs++;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (i == j)
                                continue;
                            if (i != 0 && j != 0) //TODO check it once
                                continue;
                            if (Tile.checkExistance(grid.length, grid[0].length, row + i, col + j)) {
                                int tile = grid[row + i][col + j];
                                if (Math.signum(tile) == -color)   //surrounding enemy neighbours
                                {
                                    if (Math.abs(tile) == Tile.getThreshold(numRows, numColumns, row + i, col + j) - 1) {
                                        score -= 5 - Tile.getThreshold(numRows, numColumns, row + i, col + j);
                                        vulnerable = false;
                                    }
                                }
                            }
                        }
                    }

                    if (vulnerable) {
                        int currentTile = grid[row][col];
                        int threshold = Tile.getThreshold(numRows, numColumns, row, col);
                        if (threshold == 3) {
                            score += 2;
                        } else if (threshold == 2) {
                            score += 3;
                        } else if (Math.abs(currentTile) == threshold - 1) {
                            score += 2;
                        }
                    }
                } else if (curColor == -color) {
                    enemyOrbs++;
                }

            }
        }

        //add chain sizes
        Map<Utilities.Pos, Integer> chains = Utilities.getChains(grid, color, includeEnemyOrbsInChain);
        for (Integer eachValue : chains.values()) {
            if(eachValue>1)
                score += eachValue;
        }

        if (myOrbs > 1 && enemyOrbs == 0) {
            score += 1000;    //win case
        } else if (myOrbs == 0 && enemyOrbs > 1) {
            score -= 1000;    //lose case
        }
        return score;
    }

}
