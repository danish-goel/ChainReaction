package com.ai.chainreaction;

import java.util.ArrayList;

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

    int getBestMove(Tile[][] tiles, int currentDepth, boolean Max,int color) {

        //check all the places where you can place the Orb
        ArrayList<Tile> orbPlacements = new ArrayList<Tile>();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                Tile til = tiles[row][col];
                if (til.color == Tile.EMPTY || til.color ==color) {
                    orbPlacements.add(til);
                }
            }
        }

        int min=0;
        int max=0;
        if(Max)
        {
            max=-100000;    //set to -infinity
        }
        else {
            min=100000; //set to +infinity
        }
        for(Tile tile:orbPlacements)
        {

            //base case when the depth is reached
            if (currentDepth == depthLimit) {

                return getHeuristic(tiles,tile);

            }

            //--------------------
            //at position of tile put the orb


            int ret=getBestMove(tiles,currentDepth+1,!Max,-color); //invert the color and minMax node and call for next depth
            if(Max)
            {
                if(ret>max)
                {
                    max=ret;
                }
            }
            else
            {
                if(ret<min)
                {
                    min=ret;
                }
            }


            //--------------------
            //reset the grid
        }

        return 0;
    }

    int getHeuristic(Tile[][] tiles,Tile tile) {
        int row;
        int column;
        return 0;
    }

}
