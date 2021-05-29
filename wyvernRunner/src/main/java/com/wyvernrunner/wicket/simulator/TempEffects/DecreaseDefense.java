package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseDefense extends TempEffect {

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

    public void applyEffects(Player caster, Player target){
        target.setDefense(target.getDefense()*0.3);
    }

    public void resetEffects(Player caster, Player target) {
        target.setDefense(target.getDefense()/0.3);
    }

}
