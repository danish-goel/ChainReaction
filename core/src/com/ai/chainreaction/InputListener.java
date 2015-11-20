package com.ai.chainreaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Danish on 14-Nov-15.
 */
public class InputListener extends InputAdapter {

    ChainReaction chainreaction;
    Vector3 coord = new Vector3();

    public InputListener(ChainReaction chainreaction) {
        this.chainreaction = chainreaction;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        coord.set(screenX, screenY, 0);
        chainreaction.camera.unproject(coord);

        if (!chainreaction.gameOver)
            playTurnOnTouch();
        //dbshjsdb
        Gdx.app.log("moves", ""+chainreaction.moves);

        return super.touchDown(screenX, screenY, pointer, button);
    }

    public void playTurnOnTouch() {
        for (int row = 0; row < chainreaction.boardRows; row++) {
            for (int col = 0; col < chainreaction.boardCols; col++) {
                Tile tile = chainreaction.tiles[row][col];
                if (tile.touched(coord.x, coord.y)) {
                    tile.player = chainreaction.turn;
                    if (tile.player == Tile.RED && (tile.color == Tile.EMPTY || tile.color == Tile.RED)) {
                        tileClicked(tile, Tile.RED, row, col, chainreaction.boardRows, chainreaction.boardCols);
                    } else if (tile.player == Tile.BLUE && (tile.color == Tile.EMPTY || tile.color == Tile.BLUE)) {
                        tileClicked(tile, Tile.BLUE, row, col, chainreaction.boardRows, chainreaction.boardCols);
                    } else {
                        chainreaction.turn = -1 * chainreaction.turn;   //turn does not change
                        chainreaction.moves--;
                    }
                    if (chainreaction.checkWinnerSimple() != Tile.EMPTY) {
                        chainreaction.gc.gameOver();
                        chainreaction.gameOver = true;
                    }
                    chainreaction.turn = -1 * chainreaction.turn;
                    chainreaction.moves++;
                }
            }
        }
    }

    void tileClicked(Tile tile, int color, int currentRow, int currentColumn, int totalRows, int totalColumns) {

        Queue<TileCoordinate> tilesToBeClicked = new LinkedList<TileCoordinate>();
        TileCoordinate start = new TileCoordinate(currentRow, currentColumn);
        tilesToBeClicked.add(start);
        Gdx.app.log("start", "r:" + currentRow + " c:" + currentColumn + "color:" + color);
        int count = 0;
        while (!tilesToBeClicked.isEmpty()) {
            Gdx.app.log("moves", "qSize: "+tilesToBeClicked.size());
            if(count%1==0)
            {
                if (chainreaction.checkWinnerSimple() != Tile.EMPTY) {
                    chainreaction.gameOver = true;
                    chainreaction.gc.gameOver();
                    return;
                }
            }
            count++;

            TileCoordinate clickThisTileCoordinate = tilesToBeClicked.remove();
//            Gdx.app.log(clickThisTileCoordinate.getRow()+" "+clickThisTileCoordinate.col+ "Size", ""+tilesToBeClicked.size());
            Gdx.app.log("minimax", "x"+tile.x+ "y"+tile.y);
            Gdx.app.log("minimax", "x"+clickThisTileCoordinate.row+ "y"+clickThisTileCoordinate.col);
            Tile clickThisTile = chainreaction.tiles[clickThisTileCoordinate.row][clickThisTileCoordinate.col];
            Queue<TileCoordinate> returnedTiles = explodedTiles(clickThisTile, color, clickThisTileCoordinate.row, clickThisTileCoordinate.col, totalRows, totalColumns);
            if (returnedTiles.size() > 0) {
                tilesToBeClicked.addAll(returnedTiles);

            }
        }
        Gdx.app.log("start complete", ChainReaction.debug + " r:" + currentRow + " c:" + currentColumn);
    }

    Queue<TileCoordinate> explodedTiles(Tile tile, int color, int currentRow, int currentColumn, int totalRows, int totalColumns) {
        Gdx.app.log("explode", ChainReaction.debug + " debug");
        tile.color = color;
        tile.numOrbs++;
        Queue<TileCoordinate> tilesToBeClicked = new LinkedList<TileCoordinate>();
        if (tile.numOrbs >= tile.threshold) {
            tile.explode = true;
            tile.color = Tile.EMPTY;
            tile.numOrbs = tile.numOrbs % tile.threshold;

            if (leftTileExists(currentRow, currentColumn, totalRows, totalColumns)) {
                int leftColumn = currentColumn - 1;
//                Tile leftTile = chainreaction.tiles[currentRow][leftColumn];
                TileCoordinate leftTile = new TileCoordinate(currentRow, leftColumn);
                tilesToBeClicked.add(leftTile);
//                tileClicked(leftTile, color, currentRow, leftColumn, totalRows, totalColumns);
            }
            if (rightTileExists(currentRow, currentColumn, totalRows, totalColumns)) {
                int rightColumn = currentColumn + 1;
//                Tile rightTile = chainreaction.tiles[currentRow][rightColumn];
                TileCoordinate rightTile = new TileCoordinate(currentRow, rightColumn);
                tilesToBeClicked.add(rightTile);
//                tileClicked(rightTile, color, currentRow, rightColumn, totalRows, totalColumns);
            }
            if (topTileExists(currentRow, currentColumn, totalRows, totalColumns)) {
                int topRow = currentRow - 1;
//                Tile topTile = chainreaction.tiles[topRow][currentColumn];
                TileCoordinate topTile = new TileCoordinate(topRow, currentColumn);
                tilesToBeClicked.add(topTile);
//                tileClicked(topTile, color, topRow, currentColumn, totalRows, totalColumns);
            }
            if (bottomTileExists(currentRow, currentColumn, totalRows, totalColumns)) {
                int bottomRow = currentRow + 1;
//                Tile bottomTile = chainreaction.tiles[bottomRow][currentColumn];
                TileCoordinate bottomTile = new TileCoordinate(bottomRow, currentColumn);
                tilesToBeClicked.add(bottomTile);
//                tileClicked(bottomTile, color, bottomRow, currentColumn, totalRows, totalColumns);
            }
        }
        return tilesToBeClicked;
    }

    public boolean leftTileExists(int currentRow, int currentColumn, int totalRows, int totalColumns) {
        int leftColumn = currentColumn - 1;
        if (leftColumn >= 0 && currentRow >= 0 && leftColumn < totalColumns && currentRow < totalRows) {
            return true;
        } else {
            return false;
        }
    }

    public boolean rightTileExists(int currentRow, int currentColumn, int totalRows, int totalColumns) {
        int rightColumn = currentColumn + 1;
        if (rightColumn >= 0 && currentRow >= 0 && rightColumn < totalColumns && currentRow < totalRows) {
            return true;
        } else {
            return false;
        }
    }

    public boolean topTileExists(int currentRow, int currentColumn, int totalRows, int totalColumns) {
        int topRow = currentRow - 1;
        if (currentColumn >= 0 && topRow >= 0 && currentColumn < totalColumns && topRow < totalRows) {
            return true;
        } else {
            return false;
        }
    }

    public boolean bottomTileExists(int currentRow, int currentColumn, int totalRows, int totalColumns) {
        int bottomRow = currentRow + 1;
        if (currentColumn >= 0 && bottomRow >= 0 && currentColumn < totalColumns && bottomRow < totalRows) {
            return true;
        } else {
            return false;
        }
    }

}
