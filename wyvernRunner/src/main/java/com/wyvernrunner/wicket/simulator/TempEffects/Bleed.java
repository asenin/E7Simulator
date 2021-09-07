package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public class Bleed extends TempEffect{


    private int duration;

    public Bleed(int duration,double rate, Player caster, Player target) {
        this.duration = duration;
        this.rate = rate*100;
        this.caster = caster;
        this.target = target;
    }

    public int getType() {
        type = 13;
        return type;}

    public int getDuration() {return this.duration;}

    public void applyEffects(Player caster, Player target){
        double damageReceived = caster.getAttack()*0.3*1.871/((1-0.7)/target.getDefense()/300+1);
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
