package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseAttack extends Debuff {

    private final int type = 1;
    private int duration;


    public DecreaseAttack(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    /*
    public void applyEffects(Player target){
        target.setAttack(target.getAttack()*0.5); // reduce atk by 50%
    }

    public void updateEffect(int dur){ // update duration only
        duration = dur;
    }

    public void resetEffect(Player target){ // reset original stat
        target.setAttack(target.getAttack()*2);
    }
    */

}
