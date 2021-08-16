package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Poison extends TempEffect{


    private int duration;
    private double rate;

    public Poison(int duration,double rate) {
        this.duration = duration;
        this.rate = rate;
    }

    public int getType() {
        int type = 13;
        return type;}

    public int getDuration() {return this.duration;}

    public void applyEffects(Player caster, Player target){
        double damageReceived = caster.getHealth()*0.05;
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


}
