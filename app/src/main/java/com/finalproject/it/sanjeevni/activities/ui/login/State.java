package com.finalproject.it.sanjeevni.activities.ui.login;

public class State implements Comparable<State> {
    private int stateID;
    private String stateName;

    public State(int stateID,  String stateName) {
        this.stateID = stateID;
        this.stateName = stateName;
    }

    public int getStateID() {
        return stateID;
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public String toString() {
        return stateName;
    }

    @Override
    public int compareTo(State another) {
        return this.getStateID() - another.getStateID();//ascending order
//            return another.getStateID()-this.getStateID();//descending order
    }
}
