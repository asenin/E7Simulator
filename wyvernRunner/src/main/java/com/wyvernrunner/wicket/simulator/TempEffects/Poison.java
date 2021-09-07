package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Poison extends TempEffect{


    private int duration;

    public Poison(int duration,double rate, Player caster,Player target) {
        this.duration = duration;
        this.rate = rate*100;
        this.caster = caster;
        this.target = target;
    }

    public int getType() {
        type = 19;
        return type;}

    public int getDuration() {return this.duration;}

    public void applyEffects(Player caster,Player target){
        double damageReceived = target.getHealth()*0.05;
        double damage = target.getShield() - damageReceived;
        if (damage > 0){
            target.setShield(target.getShield() - damageReceived);
        } else {
            target.setShield(0);
            target.setHealth(target.getHealth()-Math.abs(damage));
        }
    }

    public void resetEffects(Player caster, Player target) {

    }

    public void reduceDuration(){
        duration--;
    }
}
