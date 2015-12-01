package com.ai.chainreaction.algorithms;

/**
 * Created by Manan on 18-11-2015.
 */
import com.ai.chainreaction.Utilities;
import com.ai.chainreaction.Utilities.Pos;
import com.ai.chainreaction.algorithms.IAlgorithm;

import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements IAlgorithm {

//    public static Pos getNextCoord(Tile[][] tiles, int turn)
//    {
//        List<Pos> listPlayablePostitions = Utilities.getPlayablePositions(tiles, turn);
//        Random random = new Random();
//        return listPlayablePostitions.get(random.nextInt(listPlayablePostitions.size()));
//    }

    public Pos getNextMove(int[][] tiles, int turn)
    {
        List<Pos> listPlayablePostitions = Utilities.getPlayablePositions(tiles, turn);
        Random random = new Random();
        return listPlayablePostitions.get(random.nextInt(listPlayablePostitions.size()));
    }

}
