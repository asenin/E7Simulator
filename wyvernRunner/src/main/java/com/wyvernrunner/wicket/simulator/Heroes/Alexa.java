package com.wyvernrunner.wicket.simulator.Heroes;
//package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Monsters_W13.Wyvern;
import com.wyvernrunner.wicket.simulator.Player;
import com.wyvernrunner.wicket.simulator.TempEffects.Poison;
import com.wyvernrunner.wicket.simulator.TempEffects.TempEffect;

import java.util.*;

public class Alexa extends Player {

    //private List<Double> skill1up;
    //private List<Double> skill2up;
    //private List<Double> skill3up;

    /**********************************************************
     *                    SKILL RATIOS                        *
     **********************************************************/

    private double rateSkill1up = 1.0;
    private double powSkill1 = 1.0;
    private double EnhanceModSkill1 = 0.2;

    private double rateSkill2up = 1.0;
    private double powSkill2 = 1.0;
    private double EnhanceModSkill2 = 0.3;
    private double poisonRateSkill2up = 1.0;
    private int cdSkill2 = 0;
    private int cdGlobalSkill2 = 3;

    private double rateSkill3up = 1.5;
    private double powSkill3 = 0.9;
    private double EnhanceModSkill3 = 0.1;
    private int cdSkill3 = 0;
    private double dmgBoostS3 = 0.15;
    private int cdGlobalSkill3 = 5;

    /**********************************************************
     *                  TEMPORARY EFFECTS                     *
     **********************************************************/

    private Map<Integer, TempEffect> DebuffsList = new HashMap<>(); // unique debuffs / key = debuff number ... value = tempeffect so we can trigger its effect
    private ArrayList<TempEffect> TickDamageList = new ArrayList<>(); // for burns / poisons / bombs
    private Map<Integer, TempEffect> BuffsList = new HashMap<>(); // unique debuffs


    /**********************************************************
     *                    INSTANCIATION                       *
     **********************************************************/


    public Alexa(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual, int element, int team, double shield) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual, element,team,shield);

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

        /***********************************************************
         *  Complete Skillups -> Add poison rateup, cds3down test  *
         ***********************************************************/

        //Skillup skeleton

    }


    /**********************************************************
     *                    CHOOSE SKILL                        *
     **********************************************************/


    public void skillAI(Player currentTarget, Map<String, Player> playerList, double tickValue,ArrayList<String> listA,ArrayList<String> listE1) {
        if ((DebuffsList.get(7) != null) && (DebuffsList.get(7).duration > 0)) { // if stunned
            // doesn't do anything because stunned
            if (cdSkill2 > 0) { // reduce cd of S2 by 1
                cdSkill2--;
            }
            if (cdSkill3 > 0) { // reduce cd of S3 by 1
                cdSkill3--;
            }
        } else if ((DebuffsList.get(21) != null) && (DebuffsList.get(21).duration > 0)) { // if taunted, do S1 onto caster of taunt
            skill1(DebuffsList.get(21).caster);
        } else if ((DebuffsList.get(25) != null) && (DebuffsList.get(25).duration > 0)) { // if silenced -> S1
            skill1(currentTarget);
        } else {
            if (cdSkill3 == 0) {
                skill3(currentTarget);
            } else if (cdSkill2 == 0) {
                skill2(currentTarget, playerList,tickValue);
            } else { // reduce cd of S2 and S3 by 1
                skill1(currentTarget);
            }
        }
    }


    /**********************************************************
     *                  SKILLS DESCRIPTION                    *
     **********************************************************/


    public void skill1(Player currentTarget) {

        double doubleAtk = getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg());

        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                rateSkill1up, // S1 Rate
                0.0, // S1 has no flat mod
                getFlat2Mod(currentTarget), // TODO DDJ
                getPOW(powSkill1), // S1 pow is 1.0
                getSkillEnhanceMod(EnhanceModSkill1),
                0.0, // no external mod
                doubleAtk,
                getTargetDebuff(currentTarget),
                getElementalMod(getElement(), currentTarget.getElement()),
                currentTarget.getDefense(),
                getDefbreakMod(currentTarget),
                0.0, 0.0, 0.0
        ));

        // if crit, double atk

        if (doubleAtk > 1.4) { // it's a critical hit
            applyDamage(currentTarget, damageDealt(getAttack(),
                    getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                    0.75, // S1 additional hit rate
                    0.0, // S1 has no flat mod
                    getFlat2Mod(currentTarget), // TODO DDJ
                    getPOW(powSkill1), // S1 pow is 1.0
                    getSkillEnhanceMod(EnhanceModSkill1),
                    0.0, // no external mod
                    getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg()),
                    getTargetDebuff(currentTarget),
                    getElementalMod(getElement(), currentTarget.getElement()),
                    currentTarget.getDefense(),
                    getDefbreakMod(currentTarget),
                    0.0, 0.0, 0.0
            ));
        }

        if (cdSkill2 > 0) { // reduce cd of S2 by 1
            cdSkill2--;
        }
        if (cdSkill3 > 0) { // reduce cd of S3 by 1
            cdSkill3--;
        }
    }

    public void skill2(Player currentTarget, Map<String, Player> playerList, double tickValue){

        // DAMAGE
        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                rateSkill2up, // S2 Rate
                0.0, // S2 has no flat mod
                getFlat2Mod(currentTarget), // TODO DDJ
                getPOW(powSkill2),
                getSkillEnhanceMod(EnhanceModSkill2),
                0.0, // no external mod
                getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg()),
                getTargetDebuff(currentTarget),
                getElementalMod(getElement(), currentTarget.getElement()),
                currentTarget.getDefense(),
                getDefbreakMod(currentTarget),
                0.0, 0.0, 0.0
        ));

        // DEBUFFS

        landPoisonDebuff(currentTarget,new Poison(2,poisonRateSkill2up,playerList.get(getName()),currentTarget));
        landPoisonDebuff(currentTarget,new Poison(2,poisonRateSkill2up,playerList.get(getName()),currentTarget));

        Player pl = (playerList.get(getName()));
        pl.setNumberOftick(Math.max(0, pl.getNumberOftick() - 0.3 * tickValue)); // Boost de 30% le CR d'Alexa
        pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed()))); // current CR = nb_ticks/total_ticks

        cdSkill2 = cdGlobalSkill2; // put the skill on CD

        if (cdSkill3 > 0) { // reduce cd of S2 by 1
            cdSkill3--;
        }
    }

    public void skill3(Player currentTarget) {

        // COUNT NUMBER OF TICK DAMAGE DEBUFF
        int numberOfDebuffs = 0;
        for (TempEffect element : currentTarget.getTickDamageList()) {
            numberOfDebuffs++;
        }

        // COUNT NUMBER OF UNIQUE DEBUFF
        TempEffect i;
        Iterator<Map.Entry<Integer, TempEffect>> it = DebuffsList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, TempEffect> pair = it.next();
            i = pair.getValue(); // i = TempEffect
            if (i != null){
                if (i.duration >0){
                    numberOfDebuffs++;
                }
            }
        }

        // DAMAGE
        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                rateSkill3up, // S3 Rate
                0.0, // S3 has no flat mod
                getFlat2Mod(currentTarget), // TODO DDJ
                getPOW(powSkill3),
                getSkillEnhanceMod(EnhanceModSkill3),
                0.15*numberOfDebuffs + getExtMod(currentTarget), // bonus 15% damage per debuff
                getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg()),
                getTargetDebuff(currentTarget),
                getElementalMod(getElement(), currentTarget.getElement()),
                currentTarget.getDefense(),
                getDefbreakMod(currentTarget),
                0.0, 0.0, 0.0
        ));

        cdSkill3 = cdGlobalSkill3; // put the skill on CD

        if (cdSkill2 > 0) { // reduce cd of S2 by 1
            cdSkill2--;
        }
    }


    /**********************************************************
     *                  DAMAGE CALCULATION                    *
     **********************************************************/


    public static double damageDealt(double Attack, double Atkmod, double SkillRate, double FlatMod, double FlatMod2, double pow, double SkillEnhanceMods, double extMods, double HitTypeMod, double Target, double ElementalMod, double Defense,
                                     double DefbreakMod, double PenMod, double DamageReduction, double DamageSharing) {
        double atkMods = (Attack * (1 + Atkmod) * SkillRate + FlatMod) * 1.871 + (FlatMod2);
        double DmgMods = (pow) * (1 + SkillEnhanceMods) * (1 + extMods);  // *( arti damage + hunt bonus damage + ...)
        double OtherMods = HitTypeMod * (1 + Target) * (1+ElementalMod);
        double DefenseMod = ((1 - PenMod) * (1 - DefbreakMod) * (Defense) / 300 + 1);
        double DamageMitigation = (1 - DamageReduction) * (1 - DamageSharing);
        return atkMods * DmgMods * OtherMods * DamageMitigation / DefenseMod;
    }

    public static void applyDamage(Player player, double damageReceived) {
        double damage = player.getShield() - damageReceived;
        if (damage > 0) {
            player.setShield(player.getShield() - damageReceived);
        } else {
            player.setShield(0);
            player.setHealth(player.getHealth() - Math.abs(damage));
        }
    }

    public static double getAtkMods(Map<Integer, TempEffect> BuffsList,Map<Integer, TempEffect> DebuffsList) { // check atk buff and atk debuff
        // check atk buff
        if (BuffsList.get(2) == null) {
            if (DebuffsList.get(1) == null) {
                return 0;
            } else if (DebuffsList.get(1).getDuration() > 0) {
                return -0.5;
            } else {
                return 0;
            }
        } else if (BuffsList.get(2).getDuration() > 0) {
            if (DebuffsList.get(1) == null) {
                return -0.5;
            } else if (DebuffsList.get(1).getDuration() > 0) {
                 return 0.2;
            } else {
                return -0.5;
            }
        } else {
            if (DebuffsList.get(1) == null) {
                return 0;
            } else if (DebuffsList.get(1).getDuration() > 0) {
                return -0.5;
            } else {
                return 0;
            }
        }
    }


    public static double getFlat2Mod(Player target){ // DDJ?
        return target.getMaxhp()*0.03;
    }

    public static double getPOW(double pow){
        return pow;
    }

    public static double getSkillEnhanceMod(Double EnhanceMod){
        return EnhanceMod;
    }

    public static double getHitTypeMod(int element, int targelement, double CC, double CDMG){
        switch (element) {
            case 1: // water
                if (targelement == 3) { // earth
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < CC) { // crit hit
                            return CDMG / 100;
                        } else {
                            randomInt = r.nextInt(100);
                            if (randomInt < 30) { // strike hit
                                return 1.3;
                            } else {
                                return 1.0;
                            }
                        }
                    }
                } else if (targelement == 2) { // 15% bonus cc to fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC + 15) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                } else { // non earth non fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }
            case 2: // fire
                if (targelement == 1) { // water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < CC) { // crit hit
                            return CDMG / 100;
                        } else {
                            randomInt = r.nextInt(100);
                            if (randomInt < 30) { // strike hit
                                return 1.3;
                            } else {
                                return 1.0;
                            }
                        }
                    }
                } else if (targelement == 3) { // 15% bonus cc to fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC + 15) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                } else { // non water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }
            case 3: // earth
                if (targelement == 2) { // fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < CC) { // crit hit
                            return CDMG / 100;
                        } else {
                            randomInt = r.nextInt(100);
                            if (randomInt < 30) { // strike hit
                                return 1.3;
                            } else {
                                return 1.0;
                            }
                        }
                    }
                } else if (targelement == 1) { // 15% bonus cc to fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC + 15) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                } else { // non water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }
            case 4: // dark
                if (targelement == 5) { // 15% bonus cc to light
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC + 15) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                } else { // non light
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }
            case 5: // light
                if (targelement == 4) { // 15% bonus cc to dark
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC + 15) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                } else { // no dark
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG / 100;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }

            default : // dark or light
                Random r = new Random();
                int randomInt = r.nextInt(100);
                if (randomInt < CC) { // crit hit
                    return CDMG/100;
                } else {
                    randomInt = r.nextInt(100);
                    if (randomInt < 30) { // strike hit
                        return 1.3;
                    } else {
                        return 1.0;
                    }
                }
        }
    }

    public static double getTargetDebuff(Player target){
        if (target.getDebuffsList().get(27) == null) {
            return 0.0;
        } else if (target.getDebuffsList().get(27).getDuration() > 0) {
            return 0.15;
        } else {
            return 0.0;
        }
    }

    public static double getElementalMod(int element, int targelement) {
        switch (element) {
            case 1: // water
                if (targelement == 2) { // fire
                    return 0.1;
                } else {
                    return 0.0;
                }
            case 2 : // fire
                if (targelement == 3) { // earth
                    return 0.1;
                } else {
                    return 0.0;
                }
            case 3 : // earth
                if (targelement == 1) { // earth
                    return 0.1;
                } else {
                    return 0.0;
                }

            case 4 : // dark
                if (targelement == 5) { // light
                    return 0.1;
                } else {
                    return 0.0;
                }
            case 5 : // light
                if (targelement == 4) { // dark
                    return 0.1;
                } else {
                    return 0.0;
                }
            default : // pve monster
                return 0.0;
        }
    }

    public static double getDefbreakMod(Player target){
        if (target.getDebuffsList().get(3) == null) {
            return 0.0;
        } else if (target.getDebuffsList().get(3).getDuration() > 0) {
            return 0.7;
        } else {
            return 0.0;
        }
    }

    public static double getExtMod(Player currentTarget){
        double ExtMod = 0.0;
        if (currentTarget instanceof Wyvern) {
            ExtMod = ExtMod + 0.3; // 30% more damage if water against wyvern
        }
        return ExtMod;
    }

    /**********************************************************
     *                  GETTERS AND SETTERS                   *
     **********************************************************/


    public int getCdSkill2() {
        return cdSkill2;
    }

    public void setCdSkill2(int cdSkill2) {
        this.cdSkill2 = cdSkill2;
    }

    public int getCdSkill3() {
        return cdSkill3;
    }

    public void setCdSkill3(int cdSkill3) {
        this.cdSkill3 = cdSkill3;
    }

    /**********************************************************
     *                  LANDING DEBUFF TEST                   *
     **********************************************************/

    public void landPoisonDebuff(Player currentTarget, TempEffect tempEffect) {
        Random r = new Random();
        int randomInt = r.nextInt(100);
        if (randomInt < tempEffect.rate) { // debuff effect triggers
            randomInt = r.nextInt(100);
            if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
                currentTarget.getTickDamageList().add(tempEffect);
            }
        }
    }
}