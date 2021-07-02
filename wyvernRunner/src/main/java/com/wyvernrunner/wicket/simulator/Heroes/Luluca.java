package com.wyvernrunner.wicket.simulator.Heroes;

import com.wyvernrunner.wicket.simulator.Hero;
import com.wyvernrunner.wicket.simulator.Player;
import com.wyvernrunner.wicket.simulator.TempEffects.Bleed;
import com.wyvernrunner.wicket.simulator.TempEffects.TempEffect;

import java.util.List;

public class Luluca extends Hero {

    private double rateSkill1up;
    private double rateS1 = 0.5;

    private double rateSkill2up;
    private int cdSkill2 = 3;

    private double rateSkill3up;
    private int cdSkill3 = 5;

    public Luluca(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual,int element, String skillups) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual,element, skillups);

        //Test EE
        //if ee1 add 0.15 to S1 rate



        //Test skillups
    }

    public int skill1(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> buffs){
        double damage = 0; //remplace with damage calculation, mod 20% increase proportionnal to lost hp

        //if EE2 do another S1 35% chance
        return (int)damage;

    }

    public int skill2(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> effects){

        //if EE n2 remove all buffs from enemy befor applying nail

        double damage = 0; //remplace with damage calculation

        TempEffect nailChloe = new Bleed(3, 1); //Change to Magic nail
        // effects.add(nailChloe);

        //Test for extra turn if EE n1




        return (int)damage;
    }

    public int skill3(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> buffs){



        double damage = 0; //remplace with damage calculation, mod is 35% if enemy has nail

        //if EE3 decrease defense

        return (int)damage;
    }
}
