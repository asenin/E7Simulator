package com.wyvernrunner.wicket.simulator.Monsters_W13;


import com.wyvernrunner.wicket.simulator.Heroes.SeasideBellona;
import com.wyvernrunner.wicket.simulator.Interfaces.IFocus;
import com.wyvernrunner.wicket.simulator.Player;
import com.wyvernrunner.wicket.simulator.TempEffects.*;

import java.util.*;


public class Wyvern extends Player {

    /**********************************************************
     *                    SKILL RATIOS                        *
     **********************************************************/

    private final double rateSkill1up = 1.0;
    private final double powSkill1 = 1.0;
    private final double EnhanceModSkill1 = 0.0;



    private final double rateSkill2up = 0.5; // aoe dash before barrier ratio is 0.5
    private final double powSkill2 = 1.0;
    private final double EnhanceModSkill2 = 0.0;

    private final double rateSkill2blow = 1.6; // blow when barrier is still up
    private final double rateSkill2splash = 2.1; // splash after blow


    private int cdSkill2 = 4; //
    private int cdGlobalSkill2 = 4; //

    private double selfAtkMod = 0.0;

    /**********************************************************
     *                  TEMPORARY EFFECTS                     *
     **********************************************************/

    private Map<Integer, TempEffect> DebuffsList = new HashMap<>(); // unique debuffs / key = debuff number ... value = tempeffect so we can trigger its effect
    private ArrayList<TempEffect> TickDamageList = new ArrayList<>(); // for burns / poisons / bombs
    private Map<Integer, TempEffect> BuffsList = new HashMap<>(); // unique debuffs


    public Wyvern(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual,int element, int team, double shield) {
        super(name, speed, alive, attack, defense, health, cc, cdmg, eff, effres, dual,element,team,shield);
    }

    /**********************************************************
     *                    CHOOSE SKILL                        *
     **********************************************************/


    public void skillAI(Player currentTarget, Map<String, Player> playerList, double tickValue,ArrayList<String> listA,ArrayList<String> listE1) {
        if ((DebuffsList.get(7) != null) && (DebuffsList.get(7).duration > 0)) { // if stunned
            System.out.println(getName() + " is not supposed to be stun, go check the code");
        } else if ((DebuffsList.get(21) != null) && (DebuffsList.get(21).duration > 0)) { // if taunted, do S1 onto caster of taunt
            System.out.println(getName() + " is not supposed to be stun, go check the code");
        } else if ((DebuffsList.get(25) != null) && (DebuffsList.get(25).duration > 0)) { // if silenced -> S1
            System.out.println(getName() + " is not supposed to be stun, go check the code");
        } else if (cdSkill2 == 0) {
            skill2(currentTarget,playerList,listA,tickValue);
        } else { // reduce cd of S2 and S3 by 1
            skill1(currentTarget,playerList,tickValue);
        }
    }

    /**********************************************************
     *                  SKILLS DESCRIPTION                    *
     **********************************************************/

    public void skill1(Player currentTarget,Map<String, Player> playerList, double tickValue) {
        Random r = new Random();
        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList) + getSelfAtkMod() , // automatically check if it has attack buff
                rateSkill1up, // S1 Rate
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
                0.0, getDamageReduction(playerList.get(getName()),tickValue), 0.0 // TODO : take in account aurius / adamant
        ));

        // remove one buff at random
        ArrayList<Integer> bufflist = new ArrayList<>();
        Iterator<Map.Entry<Integer, TempEffect>> it = currentTarget.getBuffsList().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, TempEffect> pair = it.next();
            TempEffect i = pair.getValue(); // i is a buff
            if (i != null) {
                if (i.duration > 0) {
                    bufflist.add(i.getType()); // remember the type of buff on this list
                }
            }
        }
        if (bufflist.size()>0) {
            int buffIndex = r.nextInt(bufflist.size());
            int randomInt = r.nextInt(100);
            if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
                currentTarget.getBuffsList().get(buffIndex).setDuration(0); //
            }
        }

        // push back at 10%
        int randomInt = r.nextInt(100);
        if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
            (currentTarget).setNumberOftick(Math.max(0, (currentTarget).getNumberOftick() + 0.1 * tickValue)); // Réduit de x% le CR de la cible
            (currentTarget).setCRinPercentage(100 * (1 - (currentTarget).getNumberOftick() / (tickValue * 100 / (currentTarget).getSpeed()))); // current CR = nb_ticks/total_ticks
        }

        // self bonus of 1.5% atk each hit
        setSelfAtkMod(getSelfAtkMod()+0.015);


        skill1_additional_hits(currentTarget,playerList, tickValue); // two next hits of fireball similar to S1

    }

    public void skill1_additional_hits(Player currentTarget,Map<String, Player> playerList, double tickValue) {
        // two next attacks of fireball
        Random r = new Random();
        for (int i = 0; i< 2; i++) {
            applyDamage(currentTarget, damageDealt(getAttack(),
                    getAtkMods(BuffsList,DebuffsList) + getSelfAtkMod() , // automatically check if it has attack buff
                    rateSkill1up, // S1 Rate
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
                    0.0, getDamageReduction(playerList.get(getName()),tickValue), 0.0 // TODO : take in account aurius / adamant
            ));

            // remove one buff at random
            ArrayList<Integer> bufflist = new ArrayList<>();
            Iterator<Map.Entry<Integer, TempEffect>> it = currentTarget.getBuffsList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, TempEffect> pair = it.next();
                TempEffect j = pair.getValue(); // i is a buff
                if (j != null) {
                    if (j.duration > 0) {
                        bufflist.add(j.getType()); // remember the type of buff on this list
                    }
                }
            }

            if (bufflist.size()>0) {
                int buffIndex = r.nextInt(bufflist.size());
                int randomInt = r.nextInt(100);
                if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
                    currentTarget.getBuffsList().get(buffIndex).setDuration(0); //
                }
            }

            // push back at 10%
            int randomInt = r.nextInt(100);
            if (randomInt > Math.max(currentTarget.getEffres()-getEffres(),15)){ // bypass innate 15% ER
                (currentTarget).setNumberOftick(Math.max(0, (currentTarget).getNumberOftick() + 0.1 * tickValue)); // Réduit de x% le CR de la cible
                (currentTarget).setCRinPercentage(100 * (1 - (currentTarget).getNumberOftick() / (tickValue * 100 / (currentTarget).getSpeed()))); // current CR = nb_ticks/total_ticks
            }

            // self bonus of 5% atk each hit
            setSelfAtkMod(getSelfAtkMod()+0.015);
        }

        // SeasideBellona test
        for (Map.Entry player : playerList.entrySet()) {
            Player pl = (Player) player.getValue();
            if (pl instanceof IFocus){
                ((IFocus) pl).setFocus(((IFocus) pl).getFocus() + 1);
            }
        }

        // CD reduction by 1 turn
        if (cdSkill2 > 0) { // reduce cd of S2 by 1
            cdSkill2--;
        }
    }

    public void skill2(Player currentTarget,Map<String, Player> playerList,ArrayList<String> listA, double tickvalue){

        // DAMAGE
        applyDamage(currentTarget, damageDealt(getAttack(),
                getAtkMods(BuffsList,DebuffsList)+getSelfAtkMod(), // automatically check if it has attack buff
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
                0.0, getDamageReduction(playerList.get(getName()),tickvalue), 0.0
        ));

        // self cleanse all its debuffs
        Iterator<Map.Entry<Integer, TempEffect>> it = getDebuffsList().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, TempEffect> pair = it.next();
            TempEffect i = pair.getValue(); // i is a buff
            if (i != null) {
                if (i.duration > 0) {
                    i = null; // reset all debuffs
                }
            }
        }

        // remove all buffs
        for (Iterator<String> iterator = listA.iterator(); iterator.hasNext(); ) {
            String player = iterator.next();
            Iterator<Map.Entry<Integer, TempEffect>> pl_it = playerList.get(player).getBuffsList().entrySet().iterator();
            while (pl_it.hasNext()) {
                Map.Entry<Integer, TempEffect> pair = pl_it.next();
                TempEffect j = pair.getValue(); // i is a buff
                if (j != null) {
                    if (j.duration > 0) {
                        j = null; // reset all debuffs
                    }
                }
            }
        }


        // 1311 crit on 2001 def S2

        // 1245 x9 S1

        // 1162 S1 x4


        // set slow speed
        setSpeed(63); // has 63 speed during barrier phase

        // gives himself 35% of its hp pool in shield
        if (getBuffsList().get(6) != null) {
            getBuffsList().get(6).setDuration(1);
        } else {
            getBuffsList().put(6,new Shield(1,1,playerList.get(getName()),playerList.get(getName())));
           setShield(getMaxhp()*0.35); // 35% of its own hp in barrier
        }

        // SeasideBellona test
        for (Map.Entry player : playerList.entrySet()) {
            Player pl = (Player) player.getValue();
            if (pl instanceof IFocus){
                ((IFocus) pl).setFocus(((IFocus) pl).getFocus() + 1);
            }
        }

        // put CD on global CD
        cdSkill2 = cdGlobalSkill2; // put the skill on CD
    }

    public void skill2blow(Map<String, Player> playerList, double tickValue,ArrayList<String> listA,ArrayList<String> listE1) { // TODO : Add SSB S2 between first proc and second
        for (String player : listA) {
            Player currentTarget = playerList.get(player);
            // blow
            applyDamage(currentTarget, damageDealt(getAttack(),
                    getAtkMods(BuffsList, DebuffsList), // automatically check if it has attack buff
                    rateSkill2blow, // S2 blow rate
                    0.0, // S3 has no flat mod
                    getFlat2Mod(currentTarget), // TODO DDJ
                    getPOW(powSkill2),
                    getSkillEnhanceMod(EnhanceModSkill2),
                    getExtMod(currentTarget), // 30% more damage against non-water units
                    getHitTypeMod(getElement(), currentTarget.getElement(), getCc(), getCdmg()),
                    getTargetDebuff(currentTarget),
                    getElementalMod(getElement(), currentTarget.getElement()),
                    currentTarget.getDefense(),
                    getDefbreakMod(currentTarget),
                    0.0, getDamageReduction(playerList.get(getName()),tickValue), 0.0
            ));

            // SeasideBellona test
            for (Map.Entry ally : playerList.entrySet()) {
                Player pl = (Player) ally.getValue();
                if (pl instanceof IFocus){
                    ((IFocus) pl).setFocus(((IFocus) pl).getFocus() + 1);
                }
            }

            for (Map.Entry ally : playerList.entrySet()) {
                Player pl = (Player) ally.getValue();
                if (pl instanceof IFocus){
                    if (((IFocus) pl).getFocus() > 4) { // If the target was SeasideBellona, we check the nb of stacks
                        ((IFocus) pl).triggerFocus(pl, playerList, tickValue, listA, listE1); // Use S2
                        ((IFocus) pl).setFocus(0); // reset stacks
                    }
                }
            }

            if (currentTarget instanceof IFocus) {
                // depending on the stacks
                if (((IFocus) currentTarget).getFocus() > 4) { // If the target was SeasideBellona, we check the nb of stacks
                    ((IFocus) currentTarget).triggerFocus(currentTarget, playerList, tickValue, listA, listE1); // Use S2
                    ((IFocus) currentTarget).setFocus(0); // reset stacks
                }
            }

        }

        for (String player : listA) {
            Player currentTarget = playerList.get(player);
            // splash (SSB can proc S2 on blow, before splash)
            applyDamage(currentTarget, damageDealt(getAttack(),
                    getAtkMods(BuffsList,DebuffsList), // automatically check if it has attack buff
                    rateSkill2splash, // S2 blow rate
                    0.0, // S3 has no flat mod
                    getFlat2Mod(currentTarget), // TODO DDJ
                    getPOW(powSkill2),
                    getSkillEnhanceMod(EnhanceModSkill2),
                    getExtMod(currentTarget), // 30% more damage against non-water units
                    getHitTypeMod(getElement(), currentTarget.getElement(),getCc(),getCdmg()),
                    getTargetDebuff(currentTarget),
                    getElementalMod(getElement(), currentTarget.getElement()),
                    currentTarget.getDefense(),
                    getDefbreakMod(currentTarget),
                    0.0, 0.0, 0.0
            ));

            // DEBUFF

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
        // check if Alexa has DDJ
        return 0.0;
    }

    public static double getPOW(double pow){
        return pow;
    }

    public static double getSkillEnhanceMod(Double EnhanceMod){
        return EnhanceMod;
    }

    public static double getHitTypeMod(int element, int targelement, double CC, double CDMG){ // never misses, no bonus 15% crit chance vs weak element
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
        if (currentTarget.getElement() == 1) { // if target is water
            ExtMod = ExtMod + 0.3;
        }
        if (currentTarget instanceof Wyvern) {
            ExtMod = ExtMod + 0.3; // 30% more damage if water against wyvern
        }
        return ExtMod;
    }

    public double getDamageReduction(Player currentTarget, double tickvalue) {

        if (getSpeed() == tickvalue && getHealth() == getMaxhp()) {
            return 0.35;
        } else {
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

            if (numberOfDebuffs >2){
                return 0.35;
            } else {
                return 0.0;
            }
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

    public double getSelfAtkMod() {
        return selfAtkMod;
    }

    public void setSelfAtkMod(double selfAtkMod) {
        this.selfAtkMod = selfAtkMod;
    }

}
