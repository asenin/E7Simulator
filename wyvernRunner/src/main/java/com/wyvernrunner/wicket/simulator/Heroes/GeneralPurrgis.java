package com.wyvernrunner.wicket.simulator.Heroes;

import com.wyvernrunner.wicket.simulator.Player;
import com.wyvernrunner.wicket.simulator.TempEffects.Provoke;
import com.wyvernrunner.wicket.simulator.TempEffects.Stun;
import com.wyvernrunner.wicket.simulator.TempEffects.TempEffect;

import java.util.*;

public class GeneralPurrgis extends Player {

    /**********************************************************
     *                    SKILL RATIOS                        *
     **********************************************************/

    private double rateSkill1up = 1.0 ;
    private double powSkill1 = 1.0;
    private double EnhanceModSkill1 = 0.0;
    private double tauntRateSkill1up = 0.5;

    private double rateSkill3up = 0.8;
    private double powSkill3 = 1;
    private double EnhanceModSkill3 = 0.0;
    private double stunRateSkill3up = 0.75;
    private int cdSkill3 = 0;
    private int cdGlobalSkill3 = 6;

    /**********************************************************
     *                  TEMPORARY EFFECTS                     *
     **********************************************************/

    private Map<Integer, TempEffect> DebuffsList = new HashMap<>(); // unique debuffs / key = debuff number ... value = tempeffect so we can trigger its effect
    private ArrayList<TempEffect> TickDamageList = new ArrayList<>(); // for burns / poisons / bombs
    private Map<Integer, TempEffect> BuffsList = new HashMap<>(); // unique debuffs


    /**********************************************************
     *                    INSTANCIATION                       *
     **********************************************************/


    public GeneralPurrgis(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual, int element, int team, double shield) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual,element, team, shield );
    }


    /**********************************************************
     *                    CHOOSE SKILL                        *
     **********************************************************/


    public void skillAI(Player currentTarget, Map<String, Player> playerList, double tickValue,ArrayList<String> listA,ArrayList<String> listE1) {
        if ((DebuffsList.get(7) != null) && (DebuffsList.get(7).duration > 0)) { // if stunned
            // doesn't do anything because stunned
            if (cdSkill3 > 0) { // reduce cd of S3 by 1
                cdSkill3--;
            }
        } else if ((DebuffsList.get(21) != null) && (DebuffsList.get(21).duration > 0)) { // if taunted, do S1 onto caster of taunt
            skill1(DebuffsList.get(21).caster);
        } else if ((DebuffsList.get(25) != null) && (DebuffsList.get(25).duration > 0)) { // if silenced -> S1
            skill1(currentTarget);
        } else {
            if (cdSkill3 == 0) {
                skill3(listE1,playerList);
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
                0.06*getMaxhp(), // 6% of caster hp
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

        landS1Debuff(currentTarget,new Provoke(1,tauntRateSkill1up)); // 1 turn taunt

        if (cdSkill3 > 0) { // reduce cd of S3 by 1
            cdSkill3--;
        }
    }

    public void skill3( ArrayList<String> listE1,Map<String, Player> playerList ) {

        Player currentTarget;
        for (String player : listE1) {
            currentTarget = playerList.get(player);
            applyDamage(currentTarget, damageDealt(getAttack(),
                    getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                    rateSkill3up, // S3 Rate
                    0.08*getMaxhp(), // 8% of caster max hp
                    getFlat2Mod(currentTarget), // TODO DDJ
                    getPOW(powSkill3),
                    getSkillEnhanceMod(EnhanceModSkill3),
                    0.0,
                    getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg()),
                    getTargetDebuff(currentTarget),
                    getElementalMod(getElement(), currentTarget.getElement()),
                    currentTarget.getDefense(),
                    getDefbreakMod(currentTarget),
                    0.0, 0.0, 0.0
            ));
        }

        // STUN

        if (listE1.size() > 2) { // if more than 2 are alive
            ArrayList<String> tempList = new ArrayList<>(listE1);
            Random r = new Random();
            int randomInt = r.nextInt(tempList.size());
            Player target = playerList.get(tempList.get(randomInt));
            landS3Debuff(target,new Stun(1,stunRateSkill3up)); // 1 turn stun
            tempList.remove(randomInt);
            randomInt = r.nextInt(tempList.size());
            target = playerList.get(tempList.get(randomInt));
            landS3Debuff(target,new Stun(1,stunRateSkill3up)); // 1 turn stun

        } else { // 1 or 2 alive
            for (String player : listE1) {
                Player target = playerList.get(player);
                landS3Debuff(target,new Stun(1,stunRateSkill3up)); // 1 turn stun
            }
        }


        cdSkill3 = cdGlobalSkill3; // put the skill on CD
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
                } else { // non earth
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
            case 2 : // fire
                if (targelement == 1) { // water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
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
                } else { // non water
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
            case 3 : // earth
                if (targelement == 2) { // fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
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
                } else { // non water
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


    /**********************************************************
     *                  GETTERS AND SETTERS                   *
     **********************************************************/


    public int getCdSkill3() {
        return cdSkill3;
    }

    public void setCdSkill3(int cdSkill3) {
        this.cdSkill3 = cdSkill3;
    }


    /**********************************************************
     *                  LANDING DEBUFF TEST                   *
     **********************************************************/

    public void landS1Debuff(Player currentTarget, TempEffect tempEffect) {
        Random r = new Random();
        int randomInt = r.nextInt(100);
        if (randomInt < tempEffect.rate) { // debuff effect triggers
            randomInt = r.nextInt(100);
            if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
                currentTarget.getDebuffsList().put(21,new Provoke(tempEffect.duration, tempEffect.rate) {
                });
            }
        }
    }

    public void landS3Debuff(Player currentTarget, TempEffect tempEffect) {
        Random r = new Random();
        int randomInt = r.nextInt(100);
        if (randomInt < tempEffect.rate) { // debuff effect triggers
            randomInt = r.nextInt(100);
            if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
                currentTarget.getDebuffsList().put(7,new Stun(tempEffect.getDuration(), tempEffect.getRate()) {
                });
            }
        }
    }


}
