package com.ai.chainreaction;

import com.ai.chainreaction.Utilities.Pos;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Danish on 17-Nov-15.
 */
public class MiniMax {

    Tile[][] tiles;
    int depthLimit;
    int numRows;
    int numColumns;
    int[][] grid;
    Pos myBestPos;

    ChainReaction chainReaction;
    static int numTraverseTreeCalls = 0;

    public MiniMax(ChainReaction chainReaction, Tile[][] tiles, int depthLimit) {
        this.chainReaction = chainReaction;
        this.tiles = tiles;
        this.depthLimit = depthLimit;
        numRows = tiles.length;
        numColumns = tiles[0].length;
        myBestPos = new Pos(-1, -1);
        grid = Utilities.initalizeGrid(tiles);
    }

    public Pos getBestMove(int color) {
        traverseMinimaxTree(0, true, color);
        Gdx.app.log("BEST MOVE", "ab" + color);
        return myBestPos;
    }

    int traverseMinimaxTree(int currentDepth, boolean Max, int color) {

        int returnValue = 0;

//        Gdx.app.log("traverse",(numTraverseTreeCalls++)+"");

        //base case when the depth is reached
        if (checkWinnerIfExists(grid) != Tile.EMPTY) {
            Gdx.app.debug("depth", "" + currentDepth + Max + color);
            if (Max) {
                return 100000;    //set to -infinity
            } else {
                return -100000; //set to +infinity
            }
        }
        if (currentDepth == depthLimit) {
            return getHeuristic(grid, color);
        }

        // back up grid
//        List<TileData> filledTiles = storeGrid(tiles);
        int[][] backUpGrid = copyGrid(grid);

        //check all the places where you can place the Orb
        List<Pos> listPlayablePostitions = Utilities.getPlayablePositions(grid, color);

        int min = 0;
        int max = 0;
        if (Max) {
            returnValue = max = -100000;    //set to -infinity
        } else {
            returnValue = min = 100000; //set to +infinity
        }

        for (Pos playablePos : listPlayablePostitions) {

            //modifications
//            Tile tile = tiles[playablePos.row][playablePos.col];
            //chainReaction.inputListener.tileClicked(tile, color, (int) tile.x, (int) tile.y, chainReaction.boardRows, chainReaction.boardCols);
//            chainReaction.inputListener.touchDown((int) tile.x, (int) tile.y, 0, 0);
            clickTile(color, playablePos.row, playablePos.col, grid.length, grid[0].length);

            //recursion
            int ret = traverseMinimaxTree(currentDepth + 1, !Max, -color); //invert the color and minMax node and call for next depth
            if (Max) {
                if (ret > max) {
                    max = ret;
                    returnValue = ret;
                    if (currentDepth == 0) {
                        myBestPos.row = playablePos.row;
                        myBestPos.col = playablePos.col;
                    }
                }
            } else {
                if (ret < min) {
                    min = ret;
                    returnValue = ret;
                    if (currentDepth == 0) {
                        myBestPos.row = playablePos.row;
                        myBestPos.col = playablePos.col;
                    }
                }
            }

            //revert the grid
//            revertGrid(tiles, filledTiles);
            revertGrid(backUpGrid, grid);
        }

        return returnValue;
    }

    int[][] copyGrid(int tiles[][]) {
        int newGrid[][] = new int[tiles.length][tiles[0].length];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                newGrid[row][col] = tiles[row][col];
            }
        }
        return newGrid;
    }

    void revertGrid(int[][] source, int[][] destination) {
        for (int row = 0; row < source.length; row++) {
            for (int col = 0; col < source[0].length; col++) {
                destination[row][col] = source[row][col];
            }
        }
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

    int getHeuristic(int[][] grid, int color) {

        int myOrbs = 0;
        int enemyOrbs = 0;
        int score = 0;
        boolean vulnerable = true;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int curColor = (int) Math.signum(grid[row][col]);
                if (curColor == color) {
                    myOrbs++;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (i == j)
                                continue;
                            if (i != 0 || j != 0)
                                continue;
                            if (Tile.checkExistance(grid.length, grid[0].length, row + i, col + j)) {
                                //Tile tile = tiles[row + i][col + j];
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
        Map<Pos, Integer> chains = Utilities.getChains(grid, color);
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

    private List<TileData> storeGrid(Tile[][] grid) {
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

    void clickTile(int color, int currentRow, int currentColumn, int totalRows, int totalColumns) {

        Queue<TileCoordinate> tilesToBeClicked = new LinkedList<TileCoordinate>();
        TileCoordinate start = new TileCoordinate(currentRow, currentColumn);
        tilesToBeClicked.add(start);
        Gdx.app.log("start", "r:" + currentRow + " c:" + currentColumn + "color:" + color);
        int count = 0;
        while (!tilesToBeClicked.isEmpty()) {
            Gdx.app.log("moves", "qSize: " + tilesToBeClicked.size());
            if (count % 1 == 0) {
                if (checkWinnerIfExists(grid) != Tile.EMPTY) {
                    return;
                }
            }
            count++;

            TileCoordinate clickThisTileCoordinate = tilesToBeClicked.remove();
            Gdx.app.log("minimax", "x" + currentRow + "y" + currentColumn);
            Gdx.app.log("minimax", "x" + clickThisTileCoordinate.row + "y" + clickThisTileCoordinate.col);
            Queue<TileCoordinate> returnedTiles = explodedTiles(color, clickThisTileCoordinate.row, clickThisTileCoordinate.col, totalRows, totalColumns);
            if (returnedTiles.size() > 0) {
                tilesToBeClicked.addAll(returnedTiles);

            }
        }
        Gdx.app.log("start complete", ChainReaction.debug + " r:" + currentRow + " c:" + currentColumn);
    }

    Queue<TileCoordinate> explodedTiles(int color, int currentRow, int currentColumn, int totalRows, int totalColumns) {
        Gdx.app.log("explode", ChainReaction.debug + " debug");

        int cur = grid[currentRow][currentColumn];
        cur = Math.abs(cur) + 1;
        cur *= color;
        grid[currentRow][currentColumn] = cur;

        Queue<TileCoordinate> tilesToBeClicked = new LinkedList<TileCoordinate>();

        int threshold = Tile.getThreshold(totalRows, totalColumns, currentRow, currentColumn);
        if (Math.abs(cur) >= threshold) {
            grid[currentRow][currentColumn] = (Math.abs(cur) % threshold) * color;

            if (Tile.checkExistance(totalRows, totalColumns, currentRow, currentColumn - 1)) {
                int leftColumn = currentColumn - 1;
                TileCoordinate leftTile = new TileCoordinate(currentRow, leftColumn);
                tilesToBeClicked.add(leftTile);
            }
            if (Tile.checkExistance(totalRows, totalColumns, currentRow, currentColumn + 1)) {
                int rightColumn = currentColumn + 1;
                TileCoordinate rightTile = new TileCoordinate(currentRow, rightColumn);
                tilesToBeClicked.add(rightTile);
            }
            if (Tile.checkExistance(totalRows, totalColumns, currentRow - 1, currentColumn)) {
                int topRow = currentRow - 1;
                TileCoordinate topTile = new TileCoordinate(topRow, currentColumn);
                tilesToBeClicked.add(topTile);
            }
            if (Tile.checkExistance(totalRows, totalColumns, currentRow + 1, currentColumn)) {
                int bottomRow = currentRow + 1;
                TileCoordinate bottomTile = new TileCoordinate(bottomRow, currentColumn);
                tilesToBeClicked.add(bottomTile);
            }
        }
        return tilesToBeClicked;
    }

    public int checkWinnerIfExists(int[][] grid) {
        boolean foundRed = false;
        boolean foundBlue = false;
        int count = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int tile = grid[row][col];
                if (tile == 0)
                    continue;
                count++;
                if (tile / Math.abs(tile) == Tile.BLUE)
                    foundBlue = true;
                if (tile / Math.abs(tile) == Tile.RED)
                    foundRed = true;
            }
        }
        if (count < 2)
            return Tile.EMPTY;
        if (foundBlue && foundRed)
            return Tile.EMPTY;
        if (foundBlue)
            return Tile.BLUE;
        if (foundRed)
            return Tile.RED;
        return Tile.EMPTY;
    }

}
