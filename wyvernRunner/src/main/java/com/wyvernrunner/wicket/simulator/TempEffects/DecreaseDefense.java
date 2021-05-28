package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseDefense extends Debuff implements EffectsInterface{

    private int duration;
    private double rate;

    public DecreaseDefense(int duration,double rate) {
        this.duration = duration;
        this.rate = rate;
    }

    public int getType(){
        int type = 3;
        return type;
    }
    public int getDuration(){
        return this.duration;
    }

    public void applyEffects(Player target){
        target.setDefense(target.getDefense()*0.5); // reduce def by 50%
    }

    public void updateEffect(int dur){ // update duration only
        duration = dur;
    }

    public void resetEffect(Player target){ // reset original stat
        target.setDefense(target.getDefense()*2);
    }
}
