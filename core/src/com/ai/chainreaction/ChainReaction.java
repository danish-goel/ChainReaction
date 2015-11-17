package com.ai.chainreaction;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ChainReaction extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;

    Tile[][] tiles;


    int screenWidth;
    int screenHeight;
    int screentileSize;
    int boardOffset;
    int boardHeight;

    int boardRows=5;
    int boardCols=5;

    private Texture img;
    private Sprite sprite;

    boolean turn = true;
    boolean gameOver = false;

    @Override
    public void create() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        tiles = new Tile[boardRows][boardCols];
        screentileSize = screenWidth / boardCols;
        boardOffset = (screenHeight - screentileSize * boardRows) / 2;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        batch = new SpriteBatch();

        for (int row = 0; row < boardRows; row++) {
            for (int col = 0; col < boardCols; col++) {
                if ((row == 0 && col == 0) || (row == 0 && col == boardCols-1) || (col == 0 && row == boardRows-1) || (col == boardCols-1 && row == boardRows-1)) {
                    tiles[row][col] = new Tile(col * screentileSize + screentileSize / 2,
                            row * screentileSize + boardOffset + screentileSize / 2,
                            screentileSize,
                            screentileSize, 2);
                } else if (row == 0 || row == boardRows - 1 || col == 0 || col == boardCols-1) {
                    tiles[row][col] = new Tile(col * screentileSize + screentileSize / 2,
                            row * screentileSize + boardOffset + screentileSize / 2,
                            screentileSize,
                            screentileSize, 3);
                } else {
                    tiles[row][col] = new Tile(col * screentileSize + screentileSize / 2,
                            row * screentileSize + boardOffset + screentileSize / 2,
                            screentileSize,
                            screentileSize, 4);
                }

            }
        }

        Gdx.input.setInputProcessor(new InputListener(this));

        img = new Texture("gameOver.png");
        sprite = new Sprite(img);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (int row = 0; row < boardRows; row++) {
            for (int col = 0; col < boardCols; col++) {
                tiles[row][col].render(batch);
            }
        }

        if (gameOver) {
            sprite.draw(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    public int checkWinner(int color) {
        final int noWin = 0;
        final int Win = 1;
        for (int row = 0; row < boardRows; row++) {
            for (int col = 0; col < boardCols; col++) {
                Tile tile = tiles[row][col];
                if (tile.color != color) {
                    return noWin;
                }
            }
        }
        return Win;
    }
}