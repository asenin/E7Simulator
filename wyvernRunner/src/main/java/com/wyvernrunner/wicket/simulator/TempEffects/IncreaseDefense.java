package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class IncreaseDefense extends TempEffect {

    private int duration;
    private double originalDefense = target.getDefense();

    public IncreaseDefense(int duration,double rate) {
        this.duration = duration;
        this.rate = rate*100;
    }

    public int getType(){
        int type = 6;
        return type;
    }
    public int getDuration(){
        return this.duration;
    }

    public void applyEffects(Player caster, Player target){
        target.setDefense(target.getDefense()+originalDefense*0.6);
    }

    public void resetEffects(Player caster, Player target) {target.setDefense(target.getDefense()-originalDefense*0.6); }

    public void reduceDuration(){
        duration--;
    }
}
