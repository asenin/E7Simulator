package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class TempEffect {

    public int type;
    public int duration;
    public Player caster;
    public Player target;

    public int getType(){
        return this.type;
    }

    public int getDuration(){
        return this.duration;
    }

    public Player getCaster(){ return this.caster; }

    public Player getTarget(){ return this.target; }

    public void setDuration(int duration) {
        this.duration=duration;
    }

}
