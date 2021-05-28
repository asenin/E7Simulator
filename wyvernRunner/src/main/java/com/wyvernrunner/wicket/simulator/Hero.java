package com.wyvernrunner.wicket.simulator;


import java.util.HashMap;

public class Hero extends Player  {

    private String artefact;
    private String skillsups;


    public Hero(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual,int element) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual,element);

    }

    /*
    @Override
    public void action(Player player) {
        player.setHealth(getHealth() - (player.getAttack() + 0 )*1*1*(1.871*1)/(player.getDefense()/300+1)*1); // flat modifier / multi / element / pow / hitType)
        if (player.getHealth() <= 0) {
            player.setAlive(false); // set it to dead
        }
    }*/
}
