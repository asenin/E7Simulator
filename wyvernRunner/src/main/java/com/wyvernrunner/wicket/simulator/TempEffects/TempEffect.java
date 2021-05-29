package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public abstract class TempEffect {

    public int type;
    public int duration;
    public Player caster;
    public Player target;
    public double rate;


    //public int getType(){ return this.type; }

    public abstract int getType();

    public Player getCaster(){ return this.caster; }

    public Player getTarget(){ return this.target; }

    public void setDuration(int duration) {
        this.duration=duration;
    }

    public int getDuration(){
        return this.duration;
    }

    public double getRate(){return this.rate; }

    public abstract void applyEffects(Player caster, Player target);

    public abstract void resetEffects(Player caster, Player target);


}
