package com.ai.chainreaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Danish on 17-Nov-15.
 */
public class MiniMax {

    Tile[][] tiles;
    int depthLimit;
    int numRows;
    int numColumns;

    public MiniMax(Tile[][] tiles, int depthLimit) {
        this.tiles = tiles;
        this.depthLimit = depthLimit;
        numRows = tiles.length;
        numColumns = tiles[0].length;
    }


    int getBestMove(Tile[][] tiles, int currentDepth, boolean Max, int color) {

        List<TileData> filledTiles = storeGrid(tiles);

        //check all the places where you can place the Orb
        Map<Tile, TileCoordinate> orbPlacements = new HashMap<Tile, TileCoordinate>();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                Tile til = tiles[row][col];
                if (til.color == Tile.EMPTY || til.color == color) {
                    TileCoordinate c = new TileCoordinate(row, col);
                    orbPlacements.put(til, c);
                }
            }
        }

        int min = 0;
        int max = 0;
        if (Max) {
            max = -100000;    //set to -infinity
        } else {
            min = 100000; //set to +infinity
        }
        for (Tile tile : orbPlacements.keySet()) {

            //base case when the depth is reached
            if (currentDepth == depthLimit) {
                TileCoordinate c = orbPlacements.get(tile);
                return getHeuristic(tiles, c.row, c.col);
            }

            //-------------------- revert grid
            //at position of tile put the orb
            revertGrid(tiles, filledTiles);


            int ret = getBestMove(tiles, currentDepth + 1, !Max, -color); //invert the color and minMax node and call for next depth
            if (Max) {
                if (ret > max) {
                    max = ret;
                }
            } else {
                if (ret < min) {
                    min = ret;
                }
            }


            //--------------------
            //reset the grid
        }

        return 0;
    }

    private void revertGrid(Tile[][] grid, List<TileData> filledTiles) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = grid[i][j];
                tile.color = Tile.EMPTY;
                tile.numOrbs = 0;
            }
        }
        for (TileData each : filledTiles) {
            Tile tile = grid[each.row][each.col];
            tile.color = each.color;
            tile.numOrbs = each.numOrbs;
        }
    }

    int getHeuristic(Tile[][] tiles, int color) {

        //bbvfdvdf
        int myOrbs = 0;
        int enemyOrbs = 0;
        int score = 0;
        boolean vurnerable=true;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                if (color == Tile.BLUE) {
                    myOrbs++;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1;j<=1;j++ )
                        {
                            if(i==j)
                                continue;
                            if(i!=0 || j!=0)
                                continue;
                            if(checkExistance(tiles.length,tiles[0].length,row+i,col+j))
                            {
                                Tile tile = tiles[row+i][col+j];
                                if(tile.color!=color)   //surrounding enemy neighbours
                                {
                                    if(tile.numOrbs==tile.threshold-1)
                                    {
                                        score-=5-tile.threshold;
                                        vurnerable=false;
                                    }
                                }
                            }
                        }
                    }

                    if(vurnerable)
                    {
                        Tile currentTile=tiles[row][col];
                        if(currentTile.threshold==3)
                        {
                            score+=2;
                        }
                        else if(currentTile.threshold==2)
                        {
                            score+=3;
                        }
                        else if(currentTile.numOrbs+1==currentTile.threshold)
                        {
                            score+=2;
                        }
                    }
                } else if (color == Tile.RED) {
                    enemyOrbs++;
                }


            }
        }
        if (myOrbs > 1 && enemyOrbs == 0) {
            score += 1000;    //win case
        } else if (myOrbs == 0 && enemyOrbs > 1) {
            score -= 1000;    //lose case
        }


        return score;
    }

    public int  chains(Tile[][] tiles)
    {
        int numRows=tiles.length;
        int numColumns=tiles[0].length;
        boolean visited[][]=new boolean[numRows][numColumns];
        for(int i=0;i<numRows;i++)
        {
            for(int j=0;j<numColumns;j++)
            {
                Tile tile = tiles[i][j];

            }
        }
    }

    public class TileData {
        int row;
        int col;
        int numOrbs;
        int color;

        public TileData(int row, int col, int numOrbs, int color) {
            this.row = row;
            this.col = col;
            this.numOrbs = numOrbs;
            this.color = color;
        }
    }

    public List<TileData> storeGrid(Tile[][] grid) {
        List<TileData> filledTiles = new ArrayList<TileData>();
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = grid[i][j];
                if (tile.color != Tile.EMPTY) {
                    filledTiles.add(new TileData(i, j, tile.numOrbs, tile.color));
                }
            }
        }
        return filledTiles;
    }

    public boolean checkExistance(int rows, int cols, int x, int y) {
        if (x < 0 || x >= rows)
            return false;
        if (y < 0 || y >= cols)
            return false;
        return true;
    }


}
