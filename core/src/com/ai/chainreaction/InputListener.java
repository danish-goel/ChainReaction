package com.ai.chainreaction;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Danish on 14-Nov-15.
 */
public class InputListener extends InputAdapter {

    ChainReaction chainreaction;
    Vector3 coord = new Vector3();

    public InputListener(ChainReaction chainreaction){
        this.chainreaction =chainreaction;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        coord.set(screenX, screenY,0);
        chainreaction.camera.unproject(coord);

        for(int row=0;row< chainreaction.tiles.length;row++){
            for(int col=0;col< chainreaction.tiles[0].length;col++){
                Tile tile = chainreaction.tiles[row][col];
                if(tile.touched(coord.x, coord.y)){
                    tile.player = chainreaction.turn?Tile.RED :Tile.BLUE;
                    if(tile.player==Tile.RED && (tile.color==Tile.EMPTY || tile.color==Tile.RED))
                    {
                       tileClicked(tile,Tile.RED,row,col,chainreaction.tiles.length,chainreaction.tiles.length);

                    }
                    else if(tile.player==Tile.BLUE && (tile.color==Tile.EMPTY|| tile.color==Tile.BLUE))
                    {
                        tileClicked(tile,Tile.BLUE,row,col,chainreaction.tiles.length,chainreaction.tiles.length);
                    }
                    else
                    {
                        chainreaction.turn = !chainreaction.turn;   //turn does not change
                    }
                    chainreaction.turn = !chainreaction.turn;
                }
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    public void tileClicked(Tile tile,int color,int currentRow,int currentColumn,int totalRows,int totalColumns)
    {
        tile.color=color;
        tile.clicked++;
        if(tile.clicked==tile.threshold)
        {
            tile.explode=true;
            tile.color=Tile.EMPTY;
            tile.clicked=0;

            if(leftTileExists(currentRow, currentColumn, totalRows, totalColumns))
            {
                int leftColumn=currentColumn-1;
                Tile leftTile = chainreaction.tiles[currentRow][leftColumn];
                tileClicked(leftTile,color,currentRow,leftColumn,totalRows,totalColumns);
            }
            if(rightTileExists(currentRow, currentColumn, totalRows, totalColumns))
            {
                int rightColumn=currentColumn+1;
                Tile rightTile = chainreaction.tiles[currentRow][rightColumn];
                tileClicked(rightTile,color,currentRow,rightColumn,totalRows,totalColumns);
            }
            if(topTileExists(currentRow, currentColumn, totalRows, totalColumns))
            {
                int topRow=currentRow-1;
                Tile topTile = chainreaction.tiles[topRow][currentColumn];
                tileClicked(topTile,color,topRow,currentColumn,totalRows,totalColumns);
            }
            if(bottomTileExists(currentRow, currentColumn, totalRows, totalColumns))
            {
                int bottomRow=currentRow+1;
                Tile bottomTile = chainreaction.tiles[bottomRow][currentColumn];
                tileClicked(bottomTile,color,bottomRow,currentColumn,totalRows,totalColumns);
            }
        }

    }

    public boolean leftTileExists(int currentRow,int currentColumn,int totalRows,int totalColumns)
    {
        int leftColumn=currentColumn-1;
        if(leftColumn>=0 && currentRow>=0 && leftColumn<totalColumns && currentRow<totalRows) {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean rightTileExists(int currentRow,int currentColumn,int totalRows,int totalColumns)
    {
        int rightColumn=currentColumn+1;
        if(rightColumn>=0 && currentRow>=0 && rightColumn<totalColumns && currentRow<totalRows) {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean topTileExists(int currentRow,int currentColumn,int totalRows,int totalColumns)
    {
        int topRow=currentRow-1;
        if(currentColumn>=0 && topRow>=0 && currentColumn<totalColumns && topRow<totalRows) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean bottomTileExists(int currentRow,int currentColumn,int totalRows,int totalColumns)
    {
        int bottomRow=currentRow+1;
        if(currentColumn>=0 && bottomRow>=0 && currentColumn<totalColumns && bottomRow<totalRows) {
            return true;
        }
        else
        {
            return false;
        }
    }

}
