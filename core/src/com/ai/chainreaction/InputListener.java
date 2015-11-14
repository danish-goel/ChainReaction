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
                        tile.color=Tile.RED;
                        tile.threshold--;
                        if(tile.threshold==0)
                        {
                            tile.explode=true;
                        }
                    }
                    else if(tile.player==Tile.BLUE && (tile.color==Tile.EMPTY|| tile.color==Tile.BLUE))
                    {
                        tile.color=Tile.BLUE;
                        tile.threshold--;
                        if(tile.threshold==0)
                        {
                            tile.explode=true;
                        }
                    }
                    else
                    {
                        chainreaction.turn = !chainreaction.turn;
                    }
                    chainreaction.turn = !chainreaction.turn;
                }
            }
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }
}
