package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class IncreaseAttack extends TempEffect {

    private int duration;
    //private final double originalAtk = target.getAttack();

    public IncreaseAttack(int duration,double rate, Player caster, Player target) {
        this.duration = duration;
        this.rate = rate*100;
        this.caster = caster;
        this.target = target;
    }

    public int getType() {
        type = 2;
        return type;
    }
    public int getDuration() {
        return this.duration;
    }

    public void applyEffects(Player caster, Player target){
        //target.setAttack(target.getAttack()-originalAtk*0.5);
    }

    public void resetEffects(Player caster, Player target) {
        //target.setAttack(target.getAttack()+originalAtk*0.5);
    }

    public void reduceDuration(){
        duration--;
    }


}
