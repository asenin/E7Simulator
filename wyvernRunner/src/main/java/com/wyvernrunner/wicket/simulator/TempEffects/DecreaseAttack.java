package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class DecreaseAttack extends TempEffect {

    private int duration;
    private double originalAtk = target.getAttack();

    public DecreaseAttack(int duration,double rate) {
        this.duration = duration;
        this.rate = rate*100;
    }

    public int getType() {
        int type = 1;
        return type;
    }
    public int getDuration() {
        return this.duration;
    }

    public void applyEffects(Player caster, Player target){
        target.setAttack(target.getAttack()-originalAtk*0.5);
    }

    public void resetEffects(Player caster, Player target) {target.setAttack(target.getAttack()+originalAtk*0.5);
    }

    public void reduceDuration(){
        duration--;
    }


}
