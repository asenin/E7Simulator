package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseAttack extends Debuff implements EffectsInterface{

    private final double type = 1;
    private int duration;

    public DecreaseAttack(int dur) {
        duration = dur;
    }

    public void applyEffects(Player target){
        target.setAttack(target.getAttack()*0.5); // reduce atk by 50%
    }

    public void updateEffect(int dur){ // update duration only
        duration = dur;
    }

    public void resetEffect(Player target){ // reset original stat
        target.setAttack(target.getAttack()*2);
    }
}
