package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Burn extends Debuff {

    private final double type = 1;
    private int duration;

    public Burn(int dur) {
        duration = dur;
    }

    public void applyEffects(Player caster, Player target){
        target.setHealth(target.getHealth()-caster.getAttack()*0.6*1.871/((1-0.7)/target.getDefense()/300+1)); // reduce atk by 50%
    }

}
