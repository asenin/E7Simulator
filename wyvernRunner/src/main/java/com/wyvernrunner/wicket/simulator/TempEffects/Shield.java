package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Shield extends TempEffect {

    private int duration;

    public Shield(int duration,double rate,Player caster,Player target) {
        this.duration = duration;
        this.rate = rate*100;
        this.caster = caster;
        this.target = target;
    }

    public int getType(){
        type = 18;
        return type;
    }
    public int getDuration(){
        return this.duration;
    }

    public void applyEffects(Player caster, Player target){

    }

    public void resetEffects(Player caster, Player target) {target.setShield(0); }

    public void reduceDuration(){
        duration--;
    }
}
