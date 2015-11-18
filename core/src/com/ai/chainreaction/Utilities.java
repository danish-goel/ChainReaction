package com.ai.chainreaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manan on 18-11-2015.
 */
public class Utilities {

    public static class Pos {
        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        int row;
        int col;
    }

    public static List<Pos> getPlayablePositions(Tile[][] grid, int turn) {
        List<Pos> listPlayablePositions = new ArrayList<Pos>();
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = grid[i][j];
                if(tile.color==turn || tile.color==Tile.EMPTY)
                    listPlayablePositions.add(new Pos(i,j));
            }
        }
        return listPlayablePositions;
    }

}
