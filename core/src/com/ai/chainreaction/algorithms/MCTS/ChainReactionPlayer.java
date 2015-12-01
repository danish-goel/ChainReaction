package com.ai.chainreaction.algorithms.MCTS;


import com.ai.chainreaction.ChainReaction;
import com.ai.chainreaction.Utilities;

import java.util.Collections;
import java.util.List;

/**
 * Created by Pranav on 01-12-2015.
 */

public class ChainReactionPlayer implements MctsDomainAgent<ChainReactionState>{
    int color;

    public ChainReactionPlayer(int color) {
        this.color = color;
    }

    public int getTurn() {
        return color;
    }

    @Override
    public ChainReactionState getTerminalStateByPerformingSimulationFromState(ChainReactionState state) {
        while (!state.isTerminal()) {
            Utilities.Pos action = getBiasedOrRandomActionFromStatesAvailableActions(state);
            state.performActionForCurrentAgent(action);
        }
        return state;
    }

    private Utilities.Pos getBiasedOrRandomActionFromStatesAvailableActions(ChainReactionState state) {
        List<Utilities.Pos> availableActions = state.getAvailableActionsForCurrentAgent();
        for (Utilities.Pos action : availableActions) {
            if (actionWinsGame(state, action))
                return action;
        }
        return getRandomActionFromActions(availableActions);
    }

    private boolean actionWinsGame(ChainReactionState state, Utilities.Pos action) {
        state.performActionForCurrentAgent(action);
        boolean actionEndsGame = state.isTerminal();
        state.undoAction(action);
        return actionEndsGame;
    }

    private Utilities.Pos getRandomActionFromActions(List<Utilities.Pos> actions) {
        Collections.shuffle(actions);
        return actions.get(0);
    }

    @Override
    public double getRewardFromTerminalState(ChainReactionState terminalState) {
        if (terminalState.specificPlayerWon(this))
            return 1;
        else
            return 0;
    }
}
