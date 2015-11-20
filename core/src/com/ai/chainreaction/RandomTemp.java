package com.ai.chainreaction;

/**
 * Created by Manan on 18-11-2015.
 */
import com.ai.chainreaction.Utilities.Pos;

import java.util.List;
import java.util.Random;

public class RandomTemp {

    public static Pos getNextCoord(Tile[][] tiles, int turn)
    {
        List<Pos> listPlayablePostitions = Utilities.getPlayablePositions(tiles, turn);
        Random random = new Random();
        return listPlayablePostitions.get(random.nextInt(listPlayablePostitions.size()));
    }

}
