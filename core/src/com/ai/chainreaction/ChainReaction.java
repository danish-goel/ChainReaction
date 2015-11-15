package com.ai.chainreaction;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ChainReaction extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;

    Tile[][] tiles;


    int WIDTH;
    int HEIGHT;
    int tileSize;
    int boardOffset;
    int boardHeight;

    private Texture img;
    private Sprite sprite;

    boolean turn = true;
    boolean gameOver = false;

    @Override
    public void create() {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        tiles = new Tile[5][5];
        tileSize = WIDTH / tiles[0].length;
        boardOffset = (HEIGHT - tileSize * tiles.length) / 2;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        batch = new SpriteBatch();

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                if ((row == 0 && col == 0) || (row == 0 && col == tiles.length) || (col == 0 && row == tiles.length) || (col == tiles.length && row == tiles.length)) {
                    tiles[row][col] = new Tile(col * tileSize + tileSize / 2,
                            row * tileSize + boardOffset + tileSize / 2,
                            tileSize,
                            tileSize, 2);
                } else if (row == 0 || row == tiles.length - 1 || col == 0 || col == tiles.length) {
                    tiles[row][col] = new Tile(col * tileSize + tileSize / 2,
                            row * tileSize + boardOffset + tileSize / 2,
                            tileSize,
                            tileSize, 3);
                } else {
                    tiles[row][col] = new Tile(col * tileSize + tileSize / 2,
                            row * tileSize + boardOffset + tileSize / 2,
                            tileSize,
                            tileSize, 4);
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
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
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
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                Tile tile = tiles[row][col];
                if (tile.color != color) {
                    return noWin;
                }
            }
        }
        return Win;
    }
}