package com.ai.chainreaction;

/**
 * Created by Danish on 25-Nov-15.
 */
public class MiniMaxPruning {

        int alpha;
    int beta;
    int depthLimit;

    public MiniMaxPruning(int alpha,int beta,int depthLimit)
    {
        this.alpha=alpha;
        this.beta=beta;
        this.depthLimit=depthLimit;
    }
}
