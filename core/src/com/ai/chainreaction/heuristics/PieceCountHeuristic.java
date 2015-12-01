package com.ai.chainreaction.heuristics;

import com.ai.chainreaction.Utilities;

/**
 * Created by mg on 1/12/15.
 */
public class PieceCountHeuristic implements IHeuristic {

    public PieceCountHeuristic() {
    }

    @Override
    public int getHeuristicValue(int[][] grid, int color) {
        boolean includeAndSubtractEnemy = true;
        boolean useTileCountNotOrbCount = false;
        return Utilities.getOrbCount(grid,color, includeAndSubtractEnemy, useTileCountNotOrbCount);
    }

}
