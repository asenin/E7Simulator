package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseDefense extends TempEffect {

    private int duration;

    public DecreaseDefense(int duration,double rate, Player caster, Player target) {
        this.duration = duration;
        this.rate = rate*100;
        this.caster = caster;
        this.target = target;
    }

    public int getType(){
        type = 3;
        return type;
    }
    public int getDuration(){
        return this.duration;
    }

    public void applyEffects(Player caster, Player target){
        //target.setDefense(target.getDefense()*0.3);
    }

    public void resetEffects(Player caster, Player target) {
        //target.setDefense(target.getDefense()/0.3);
    }

    public void reduceDuration(){
        duration--;
    }
}
