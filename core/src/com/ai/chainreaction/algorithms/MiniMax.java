package com.ai.chainreaction.algorithms;

import com.ai.chainreaction.ChainReaction;
import com.ai.chainreaction.Tile;
import com.ai.chainreaction.TileCoordinate;
import com.ai.chainreaction.Utilities;
import com.ai.chainreaction.Utilities.Pos;
import com.ai.chainreaction.algorithms.IAlgorithm;
import com.ai.chainreaction.heuristics.ChainHeuristic;
import com.ai.chainreaction.heuristics.IHeuristic;
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
public class MiniMax implements IAlgorithm {

    Tile[][] tiles;
    int depthLimit;
    int numRows;
    int numColumns;
    int[][] grid;
    Pos myBestPos;
    IHeuristic heuristic;

    ChainReaction chainReaction;
    static int numTraverseTreeCalls = 0;
    int globalColor;

    public MiniMax(ChainReaction chainReaction, Tile[][] tiles, int depthLimit, IHeuristic heuristic) {
        this.chainReaction = chainReaction;
        this.tiles = tiles;
        this.depthLimit = depthLimit;
        this.heuristic = heuristic;
        numRows = tiles.length;
        numColumns = tiles[0].length;
        grid = Utilities.initalizeGrid(tiles);
    }

    public MiniMax(ChainReaction chainReaction, Tile[][] tiles, int depthLimit) {
        this(chainReaction,tiles,depthLimit,new ChainHeuristic());
    }

    public Pos getNextMove(int color) {
        return getNextMove(this.grid,color);
    }

    public Pos getNextMove(int[][] grid, int color) {
        this.globalColor = color;
        myBestPos = new RandomAlgorithm().getNextMove(grid, color);
        traverseMinimaxTree(0, true, color);
        return myBestPos;
    }

    int traverseMinimaxTree(int currentDepth, boolean Max, int color) {

        int returnValue = 0;

//        Gdx.app.log("traverse",(numTraverseTreeCalls++)+"");

        //base case when the depth is reached
        if (checkWinnerIfExists(grid) != Tile.EMPTY) {
            Gdx.app.debug("depth", "" + currentDepth + Max + color);
            int factor = depthLimit - currentDepth + 1;
            if (!Max) {
                return 100000 * factor;    //set to -infinity
            } else {
                return -100000 * factor; //set to +infinity
            }
        }
        if (currentDepth == depthLimit) {
            return heuristic.getHeuristicValue(grid, globalColor);
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
