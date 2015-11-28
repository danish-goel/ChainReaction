package com.ai.chainreaction;

import com.ai.chainreaction.Utilities.Pos;

import java.util.Map;

/**
 * Created by Danish on 25-Nov-15.
 */
public class Greedy {

    public static Pos getNextCoord(int[][] grid, int player) {
        Pos pos = null;
//        List<Pos> listPlayablePostitions = Utilities.getPlayablePositions(grid, player);
//        Random random = new Random();
//        return listPlayablePostitions.get(random.nextInt(listPlayablePostitions.size()));
        Map<Pos, Integer> chains = Utilities.getChains(grid, player, true);
        int maxCount = 0;
        for (Map.Entry<Pos, Integer> each : chains.entrySet()) {
            if (maxCount < each.getValue()) {
                maxCount = each.getValue();
                pos = each.getKey();
            }
        }
        if (chains.size() == 0)
            return RandomTemp.getNextCoord(grid, player);
        return pos;
    }


}