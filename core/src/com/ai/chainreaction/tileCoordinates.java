package com.ai.chainreaction;

/**
 * Created by Danish on 20-Nov-15.
 */

public class TileCoordinates {
    int row;
    int col;

    public TileCoordinates(int row, int col)
    {
        this.row=row;
        this.col=col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}