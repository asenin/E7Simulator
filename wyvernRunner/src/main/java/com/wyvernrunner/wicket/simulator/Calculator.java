package com.wyvernrunner.wicket.simulator;


import java.util.concurrent.ThreadLocalRandom;

public class Calculator {
    int randomInt = ThreadLocalRandom.current().nextInt(1, 2 + 1);

   public String hitType(Hero Hero){


        if (Hero.getCc()==100){
            return "crit";
        } else {
            switch (randomInt){
                case 1:
                    return "crush";
                case 2:
                    return "normal";
            }
        }
        return "miss";
    }
    public double calculate(String hitType, Hero attacker, Hero defender /* Artifact Artifact , Skill Skill*/){
        //query skill data this will come from Skill object
        double skillMultiplier =0;
        double pow=0;
        double flatMod = 0;
        //This will come from Hero object
        boolean elementAdvantage = true;

        switch (hitType){
            case "crit":
                if (elementAdvantage = true){
                    return (attacker.getAttack()+flatMod)*skillMultiplier*1.15*(1.871*pow)/(defender.getDefense()/300+1)*attacker.getCdmg();
                } else{
                    return (attacker.getAttack()+flatMod)*skillMultiplier*(1.871*pow)/(defender.getDefense()/300+1)*attacker.getCdmg();
                }
                break;
            case "crush":
                if (elementAdvantage = true){
                    return (attacker.getAttack()+flatMod)*skillMultiplier*1.15*(1.871*pow)/(defender.getDefense()/300+1)*1.3;
                } else{
                    return (attacker.getAttack()+flatMod)*skillMultiplier*(1.871*pow)/(defender.getDefense()/300+1)*1.3;
                }
                break;
            case "normal":
                if (elementAdvantage = true){
                    return (attacker.getAttack()+flatMod)*skillMultiplier*1.15*(1.871*pow)/(defender.getDefense()/300+1);
                } else{
                    return (attacker.getAttack()+flatMod)*skillMultiplier*(1.871*pow)/(defender.getDefense()/300+1);
                }
                break;
            case "miss":
                if (elementAdvantage = true){
                    return (attacker.getAttack()+flatMod)*skillMultiplier*1.15*(1.871*pow)/(defender.getDefense()/300+1)*0.75;
                } else{
                    return (attacker.getAttack()+flatMod)*skillMultiplier*(1.871*pow)/(defender.getDefense()/300+1)*0.75;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + hitType);
        }
        return 0;
    }
    public double artifactDamage(Hero Hero/*Artifact Artifact */){

        //From artifact list determines damage
        return 0;
    }
}
