package com.ai.chainreaction;

import com.ai.chainreaction.Utilities.Pos;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
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

    public MiniMax(ChainReaction chainReaction, Tile[][] tiles, int depthLimit) {
        this.chainReaction = chainReaction;
        this.tiles = tiles;
        this.depthLimit = depthLimit;
        numRows = tiles.length;
        numColumns = tiles[0].length;
        myBestPos= new Pos(-1, -1);
        grid = initalizeGrid(tiles);
    }

    public Pos getBestMove(int color) {
        traverseMinimaxTree(0, true, color);
//        Gdx.app.log("BEST MOVE",myBestPos);
        return myBestPos;
    }

    int traverseMinimaxTree(int currentDepth, boolean Max, int color) {

        int returnValue = 0;

        //base case when the depth is reached
        if (currentDepth == depthLimit) {
            return getHeuristic(tiles, color);
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
            // TODO put generic array touch here

            //recursion
            int ret = traverseMinimaxTree(currentDepth + 1, !Max, -color); //invert the color and minMax node and call for next depth
            if (Max) {
                if (ret > max) {
                    max = ret;
                    returnValue = ret;
                    if(currentDepth == 0) {
                        myBestPos.row = playablePos.row;
                        myBestPos.col = playablePos.col;
                    }
                }
            } else {
                if (ret < min) {
                    min = ret;
                    returnValue = ret;
                    if(currentDepth == 0) {
                        myBestPos.row = playablePos.row;
                        myBestPos.col = playablePos.col;
                    }
                }
            }

            //revert the grid
//            revertGrid(tiles, filledTiles);
            revertGrid(backUpGrid,grid);
        }

        return returnValue;
    }

    public int[][] initalizeGrid(Tile tiles[][])
    {
        int grid[][] = new int[tiles.length][tiles[0].length];
        for (int row=0;row<tiles.length;row++)
        {
            for (int col=0;col<tiles[0].length;col++)
            {
                grid[row][col]=tiles[row][col].getColor()*tiles[row][col].getNumOrbs();
            }
        }
        return grid;
    }

    int[][] copyGrid(int tiles[][])
    {
        int newGrid[][]=new int[tiles.length][tiles[0].length];
        for (int row=0;row<tiles.length;row++)
        {
            for (int col=0;col<tiles[0].length;col++)
            {
                newGrid[row][col]=tiles[row][col];
            }
        }
        return newGrid;
    }

    void revertGrid(int[][] source,int[][] destination)
    {
        for (int row=0;row<source.length;row++) {
            for (int col = 0; col < source[0].length; col++) {
                destination[row][col]=source[row][col];
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

    int getHeuristic(Tile[][] tiles, int color) {

        int myOrbs = 0;
        int enemyOrbs = 0;
        int score = 0;
        boolean vurnerable = true;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                if (color == Tile.BLUE) {
                    myOrbs++;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (i == j)
                                continue;
                            if (i != 0 || j != 0)
                                continue;
                            if (Tile.checkExistance(tiles.length, tiles[0].length, row + i, col + j)) {
                                Tile tile = tiles[row + i][col + j];
                                if (tile.color != color)   //surrounding enemy neighbours
                                {
                                    if (tile.numOrbs == tile.threshold - 1) {
                                        score -= 5 - tile.threshold;
                                        vurnerable = false;
                                    }
                                }
                            }
                        }
                    }

                    if (vurnerable) {
                        Tile currentTile = tiles[row][col];
                        if (currentTile.threshold == 3) {
                            score += 2;
                        } else if (currentTile.threshold == 2) {
                            score += 3;
                        } else if (currentTile.numOrbs + 1 == currentTile.threshold) {
                            score += 2;
                        }
                    }
                } else if (color == Tile.RED) {
                    enemyOrbs++;
                }


            }
        }

        score += chains(tiles, color);

        if (myOrbs > 1 && enemyOrbs == 0) {
            score += 1000;    //win case
        } else if (myOrbs == 0 && enemyOrbs > 1) {
            score -= 1000;    //lose case
        }


        return score;
    }

    public int chains(Tile[][] tiles, int color) {
        //init vars
        int answer = 0;
        int numRows = tiles.length;
        int numColumns = tiles[0].length;
        boolean visited[][] = new boolean[numRows][numColumns];
        //init visited
        for (int i = 0; i < numRows; i++)
            for (int j = 0; j < numColumns; j++)
                visited[i][j] = false;

        // main code
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                //for each tile
                Tile tile = tiles[i][j];
                //if my cell is unstable - need to count this chain
                if (tile.numOrbs == tile.threshold - 1 && tile.color == color) {
                    //init dfs
                    int count = 0;
                    Stack<Pos> stack = new Stack();
                    stack.add(new Pos(i, j));
                    visited[i][j] = true;
                    //dfs loop
                    while (!stack.empty()) {
                        count++;
                        Pos pos = stack.pop();
                        //iterating over neighbors
                        for (Pos eachPos : Tile.get4Neighbours(numRows, numColumns, pos.row, pos.col)) {
                            //visited
                            if (visited[eachPos.row][eachPos.col])
                                continue;
                            visited[eachPos.row][eachPos.col] = true;

                            //add tile if unstable
                            tile = tiles[eachPos.row][eachPos.col];
                            if (tile.numOrbs == tile.threshold - 1 && tile.color == color) {
                                stack.add(eachPos);
                            }
                        }
                    }

                    //TODO chain returning unstable cells (size>1), might be changed to include ememy unstable cells
                    if (count > 1)
                        answer += count;

                }
            }
        }
        return answer;
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

}
