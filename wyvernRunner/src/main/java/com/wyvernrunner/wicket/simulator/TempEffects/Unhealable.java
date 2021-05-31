package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Unhealable extends TempEffect {


    private int duration;
    private double rate;

    public Unhealable(int duration,double rate) {
        this.duration = duration;
        this.rate = rate;
    }

    public int getType() {
        int type = 9;
        return type;
    }

    public int getDuration(){
        return this.duration;
    }

    public void applyEffects(Player caster, Player target){

    }

    public void resetEffects(Player caster, Player target) {

    }
}