package com.ai.chainreaction;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class ChainReaction extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;

    public Tile[][] tiles;

    int screenWidth;
    int screenHeight;
    int screentileSize;
    int boardOffset;
    int boardHeight;

    public int boardRows = 5;
    public int boardCols = 5;

    private Texture img;
    private Sprite sprite;

    public int turn = Tile.RED;
    public boolean gameOver = false;

    public InputListener inputListener;
    float simulationIntervalTimerBetweenTurns = (float) 0;

    public static int debug = 0;
    GameCallback gc;
    int moves;

    public ChainReaction(GameCallback gc) {
        this.gc = gc;
    }

    @Override
    public void create() {
        moves = 0;
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
                if ((row == 0 && col == 0) || (row == 0 && col == boardCols - 1) || (col == 0 && row == boardRows - 1) || (col == boardCols - 1 && row == boardRows - 1)) {
                    tiles[row][col] = new Tile(col * screentileSize + screentileSize / 2,
                            row * screentileSize + boardOffset + screentileSize / 2,
                            screentileSize,
                            screentileSize, 2);
                } else if (row == 0 || row == boardRows - 1 || col == 0 || col == boardCols - 1) {
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

        inputListener = new InputListener(this);
        Gdx.input.setInputProcessor(inputListener);

        img = new Texture("gameOver.png");
        sprite = new Sprite(img);

//        //TODO comment the following line to run it normally
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                startSimulating();
//            }
//        }, 2);
    }

    int turn2;
    int numMoves;
    Timer.Task myTimerTask;

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


    public int checkWinnerSimple() {
        if (moves < 2)
            return Tile.EMPTY;
        boolean foundRed = false;
        boolean foundBlue = false;
        for (int row = 0; row < boardRows; row++) {
            for (int col = 0; col < boardCols; col++) {
                Tile tile = tiles[row][col];
                if (tile.color == Tile.BLUE)
                    foundBlue = true;
                if (tile.color == Tile.RED)
                    foundRed = true;
            }
        }
        if (foundBlue && foundRed)
            return Tile.EMPTY;
        if (foundBlue)
            return Tile.BLUE;
        if (foundRed)
            return Tile.RED;
        return Tile.EMPTY;
    }

    public interface GameCallback {
        public void gameOver();
    }

}