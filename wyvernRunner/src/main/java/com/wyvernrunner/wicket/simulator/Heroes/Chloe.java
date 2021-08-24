package com.wyvernrunner.wicket.simulator.Heroes;

import com.wyvernrunner.wicket.simulator.Player;
import com.wyvernrunner.wicket.simulator.TempEffects.Bleed;
import com.wyvernrunner.wicket.simulator.TempEffects.Poison;
import com.wyvernrunner.wicket.simulator.TempEffects.TempEffect;

import java.util.*;

public class Chloe extends Player {

    //Skillups



    /**********************************************************
     *                    SKILL RATIOS                        *
     **********************************************************/

    private final double rateSkill1up = 1.0;
    private double EnhanceModSkill1;

    private double rateSkill2up = 0.9;
    private double powSkill2 = 0.9;
    private double EnhanceModSkill2;
    private int cdSkill2 = 0;
    private int cdGlobalSkill2 = 3;

    private double rateSkill3up = 2.0;
    private double powSkill3 = 0.8;
    private double EnhanceModSkill3;
    private int cdSkill3 = 0;
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


    public Chloe(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual,int element,int team, double shield) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual,element, team,shield);

        //Test EE

        //test Skillup
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
            if (cdSkill2 == 0) {
                skill2(currentTarget,playerList,tickValue);
            } else if (cdSkill3 == 0) {
                skill3(currentTarget);
            } else { // reduce cd of S2 and S3 by 1
                skill1(currentTarget);
            }
        }


    }

    /**********************************************************
     *                  SKILLS DESCRIPTION                    *
     **********************************************************/


    public void skill1(Player currentTarget) {
        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                rateSkill1up, // S1 Rate
                0.0, // S1 has no flat mod
                getFlat2Mod(currentTarget), // TODO DDJ
                getPOW(1.0), // S1 pow is 1.0
                getSkillEnhanceMod(EnhanceModSkill1),
                getExtMod(currentTarget,0.3), // 30% bonus damage if enemy has nail debuff
                getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg()),
                getTargetDebuff(currentTarget),
                getElementalMod(getElement(), currentTarget.getElement()),
                currentTarget.getDefense(),
                getDefbreakMod(currentTarget),
                0.0, 0.0, 0.0
        ));



        if (cdSkill2 > 0) { // reduce cd of S2 by 1
            cdSkill2--;
        }
        if (cdSkill3 > 0) { // reduce cd of S2 by 1
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
        )); // add extra damage when nail debuff exists

        // DEBUFFS

        //currentTarget.getTickDamageList().add(new Poison(2,1.0)); // TODO : Change to nail

        cdSkill2 = cdGlobalSkill2; // put the skill on CD

        if (cdSkill3 > 0) { // reduce cd of S2 by 1
            cdSkill3--;
        }


        Player pl = (playerList.get(getName()));
        pl.setNumberOftick(Math.max(0, pl.getNumberOftick() - 3 * tickValue)); // Boost de 300% le CR de Chloe = extra turn
        pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed()))); // current CR = nb_ticks/total_ticks // TODO Set 300 au lieu de 100

    }

    public void skill3(Player currentTarget) {
        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                rateSkill3up, // S3 Rate
                0.0, // S3 has no flat mod
                getFlat2Mod(currentTarget), // TODO DDJ
                getPOW(powSkill3),
                getSkillEnhanceMod(EnhanceModSkill3),
                getExtMod(currentTarget,0.35), // bonus 35% damage if nail debuff is applied
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


    public int skill2(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> effects){ //  TODO : DELETE

        //if EE n2 remove all buffs from enemy befor applying nail

        double damage = 0; //remplace with damage calculation

        //TempEffect nailChloe = new Bleed(3, 1); //Change to Magic nail
        // effects.add(nailChloe);

        //Test for extra turn if EE n1


        return (int)damage;
    }

    public int skill3(int damageShare, int enemyDefense, int damageReduction, List<TempEffect> buffs){ // TODO : DELETE



        double damage = 0; //remplace with damage calculation, mod is 35% if enemy has nail

        //if EE3 decrease defense

        return (int)damage;
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
        // check if Alexa has DDJ
        return 0.0;
    }

    public static double getPOW(double pow){
        return pow;
    }

    public static double getSkillEnhanceMod(Double EnhanceMod){
        return EnhanceMod;
    }

    public static double getExtMod(Player currentTarget, double value) {
        if (currentTarget.getDebuffsList().get(15).duration > 0) {
            return value;
        }
        return 0.0;
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
                            return CDMG;
                        } else {
                            randomInt = r.nextInt(100);
                            if (randomInt < 30) { // strike hit
                                return 1.3;
                            } else {
                                return 1.0;
                            }
                        }
                    }
                } else { // non earth
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }
            case 2 : // fire
                if (targelement == 1) { // water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < CC) { // crit hit
                            return CDMG;
                        } else {
                            randomInt = r.nextInt(100);
                            if (randomInt < 30) { // strike hit
                                return 1.3;
                            } else {
                                return 1.0;
                            }
                        }
                    }
                } else { // non water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < 30) { // strike hit
                            return 1.3;
                        } else {
                            return 1.0;
                        }
                    }
                }
            case 3 : // earth
                if (targelement == 2) { // fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < CC) { // crit hit
                            return CDMG;
                        } else {
                            randomInt = r.nextInt(100);
                            if (randomInt < 30) { // strike hit
                                return 1.3;
                            } else {
                                return 1.0;
                            }
                        }
                    }
                } else { // non water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < CC) { // crit hit
                        return CDMG;
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
                    return CDMG;
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


}
