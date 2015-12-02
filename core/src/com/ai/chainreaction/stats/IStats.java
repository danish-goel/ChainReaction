package com.ai.chainreaction.stats;

/**
 * Created by mg on 1/12/15.
 */
public interface IStats {

    public void pushStats(String algo, int turn, long timeTaken, int numStatesExpanded, int numMaxStatesInMemory);

}
