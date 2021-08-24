package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public abstract class TempEffect {

    public int type;
    public int duration;
    public Player caster;
    public Player target;
    public double rate;


    //public int getType(){ return this.type; }

    public int getType() {return type;}

    public void setType(int type) {this.type = type;}

    public double getRate() {return rate;}

    public void setRate(double rate) {this.rate = rate;}

    public Player getCaster(){ return this.caster; }

    public Player getTarget(){ return this.target; }

    public void setDuration(int duration) {
        this.duration=duration;
    }

    public int getDuration(){
        return this.duration;
    }

    public abstract void applyEffects(Player caster, Player target);

    public abstract void resetEffects(Player caster, Player target);

    public abstract void reduceDuration();


}
