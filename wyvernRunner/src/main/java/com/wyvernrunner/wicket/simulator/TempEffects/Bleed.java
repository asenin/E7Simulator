package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Bleed {
    private final int type = 1;
    private int duration;

    public Bleed(int dur) {
        duration = dur;
    }

    public void applyEffects(Player caster, Player target){
        target.setHealth(target.getHealth()-caster.getAttack()*0.3*1.871/((1-0.7)/target.getDefense()/300+1)); // reduce atk by 50%
    }
}
