package com.ai.chainreaction.algorithms;

import com.ai.chainreaction.Tile;
import com.ai.chainreaction.Utilities.Pos;

/**
 * Created by mg on 1/12/15.
 */
public interface IAlgorithm {

    public Pos getNextMove(int[][] grid, int color);

}
