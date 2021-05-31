package com.wyvernrunner.wicket.simulator.Heroes;

import com.wyvernrunner.wicket.simulator.Hero;
import com.wyvernrunner.wicket.simulator.TempEffects.Bleed;
import com.wyvernrunner.wicket.simulator.TempEffects.TempEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Alexa extends Hero {

    //private List<Double> skill1up;
    //private List<Double> skill2up;
    //private List<Double> skill3up;

    //Skillups
    private double rateSkill1up;

    private double rateSkill2up;
    private double poisonRateSkill2up = 0.8;
    private int cdSkill2 = 3;

    private double rateSkill3up;
    private int cdSkill3 = 5;
    private double dmgBoostS3 = 0.15;

    public Alexa(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual,int element, String skillups) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual,element, skillups);

        //this.skill1up = new ArrayList<>();
        //this.skill2up = new ArrayList<>();
        //this.skill3up = new ArrayList<>();

        //Populate first skill
        //this.skill1up.add(0.0); //Placeholder to add bonus dmg based on skillups

        //Populate second skill
        //this.skill2up.add(0.0); //dmgup
        //this.skill2up.add(0.0); //poison up

        //Populate third skill
        //this.skill3up.add(0.0);
        /*
            if(test if skillup is applied){
                this.cdSkill3 = true;
            }
         */

        /**********************************************************
        *  Complete Skillups -> Add poison rateup, cds3down test  *
        ***********************************************************/
    }


    public int skill1(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> buffs){
        double damage = 0; //remplace with damage calculation

        return (int)damage;
    }

    public int skill2(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> effects){
        double damage = 0; //remplace with damage calculation
        int i=0;

        while(i<2){
            TempEffect poisonAlexa = new Bleed(2, this.poisonRateSkill2up);
            effects.add(poisonAlexa);
        }

        return (int)damage;
    }

    public int skill3(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> buffs){
        double damage = 0; //remplace with damage calculation

        return (int)damage;
    }

}
