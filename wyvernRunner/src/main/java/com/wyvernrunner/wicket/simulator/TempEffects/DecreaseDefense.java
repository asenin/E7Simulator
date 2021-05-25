package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseDefense extends Debuff implements EffectsInterface{

    private final double type = 3;
    private int duration;

    public DecreaseDefense(int dur) {
        duration = dur;
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
