package com.ai.chainreaction;

import com.ai.chainreaction.Utilities.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Danish on 14-Nov-15.
 */
public class Tile {

    public static final int EMPTY = 0;
    public static final int BLUE = 1;
    public static final int RED = -1;

    public int player = EMPTY;

    TextureRegion tile;
    TextureRegion red;
    TextureRegion blue;
    TextureRegion red2;
    TextureRegion blue2;
    TextureRegion red3;
    TextureRegion blue3;

    public float x;
    public float y;
    float width;
    float height;

    int threshold;
    int color;
    boolean explode = false;
    int numOrbs;

    HashMap<String, TextureAtlas> atlases;
    Rectangle rectangle = new Rectangle();

    public Tile(float x, float y, float width, float height, int cell_threshold) {
        this.x = x;
        this.y = y;
        this.width = width - 4; //4 is tile spacing
        this.height = height - 4;
        threshold = cell_threshold;
        color = EMPTY;
        numOrbs = 0;

        atlases = new HashMap<String, TextureAtlas>();
        loadAtlas("expand.pack", "pack");
        tile = getAtlas("pack").findRegion("tile");
        blue = getAtlas("pack").findRegion("BlueOrb");
        red = getAtlas("pack").findRegion("RedOrb");
        blue2 = getAtlas("pack").findRegion("BlueOrb2");
        red2 = getAtlas("pack").findRegion("RedOrb2");
        blue3 = getAtlas("pack").findRegion("BlueOrb3");
        red3 = getAtlas("pack").findRegion("RedOrb3");


    }

    public void loadAtlas(String path, String key) {
        atlases.put(key, new TextureAtlas(Gdx.files.internal(path)));
    }

    public TextureAtlas getAtlas(String key) {
        return atlases.get(key);
    }

    public void render(SpriteBatch batch) {
        batch.draw(tile, x - width / 2, y - height / 2, width, height);
        if (color == RED) {
            if (numOrbs == 1) {
                batch.draw(blue, x - width / 2, y - height / 2, width, height);
            } else if (numOrbs == 2) {
                batch.draw(blue2, x - width / 2, y - height / 2, width, height);
            } else if (numOrbs == 3) {
                batch.draw(blue3, x - width / 2, y - height / 2, width, height);
            }
        } else if (color == BLUE) {

            if (numOrbs == 1) {
                batch.draw(red, x - width / 2, y - height / 2, width, height);
            } else if (numOrbs == 2) {
                batch.draw(red2, x - width / 2, y - height / 2, width, height);
            } else if (numOrbs == 3) {
                batch.draw(red3, x - width / 2, y - height / 2, width, height);
            }

        }

    }

    public boolean touched(float x, float y) {
        rectangle.set(this.x - width / 2, this.y - height / 2, width, height);
        return rectangle.contains(x, y);
    }

    public static List<Pos> get4Neighbours(int rows, int cols, int x, int y) {
        List<Pos> list = new ArrayList<Pos>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == j)
                    continue;
                if (i != 0 || j != 0)
                    continue;
                if (checkExistance(rows, cols, x + i, y + j)) {
                    list.add(new Pos(x + i, y + j));
                }
            }
        }
        return list;
    }

    public static boolean checkExistance(int rows, int cols, int x, int y) {
        if (x < 0 || x >= rows)
            return false;
        if (y < 0 || y >= cols)
            return false;
        return true;
    }

}
