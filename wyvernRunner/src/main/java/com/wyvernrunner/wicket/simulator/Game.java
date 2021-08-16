package com.wyvernrunner.wicket.simulator;

import com.wyvernrunner.wicket.simulator.Heroes.Alexa;
import com.wyvernrunner.wicket.simulator.Heroes.Krau;
import com.wyvernrunner.wicket.simulator.Skills.Skills;
import com.wyvernrunner.wicket.simulator.Skills.def_aoe_shield;
import com.wyvernrunner.wicket.simulator.Skills.off_aoe_damage;
import com.wyvernrunner.wicket.simulator.Skills.off_mono_damage;
import com.wyvernrunner.wicket.simulator.TempEffects.*;


import java.util.*;


public class Game {

    public static double tickValue; // taille de la barre de CR (exemple : si 300 speed qui est le plus rapide alors tickValue = 300 ) //
    public static Player activePlayer;
    public static Map<String,Player> playerList = new HashMap<>();

    public static ArrayList<String> listA = new ArrayList<>(); // store names of the target lists of allies
    public static ArrayList<String> listE1 = new ArrayList<>(); // store names of the target lists of wave 1
    public static Player front;
    public static boolean wyvernAlive = true; // true = dead / false = alive


    public static Map<String,Integer> skills2Cooldown = new HashMap<String, Integer>(); // store CD for each unit's skill 2
    public static Map<String,Integer> skills3Cooldown = new HashMap<String, Integer>(); // store CD for each unit's skill 3
    public static Map<String,Integer> skills2tracker = new HashMap<>(); // store real time cooldown on skill 2
    public static Map<String,Integer> skills3tracker = new HashMap<>(); // store real time cooldown on skill 3
    public static Map<String,Skills> skills1Stored = new HashMap<>(); // store the list of skill 1 : key = name, value = Skill
    public static Map<String,Skills> skills2Stored = new HashMap<>(); // store the list of skill 2: key = name, value = Skill
    public static Map<String,Skills> skills3Stored = new HashMap<>(); // store the list of skill 3 : key = name, value = Skill


    public static Map<String, ArrayList<TempEffect>> skills1Debuff = new HashMap<>();
    public static Map<String, ArrayList<TempEffect>> skills2Debuff = new HashMap<>();
    public static Map<String, ArrayList<TempEffect>> skills3Debuff = new HashMap<>();
    public static Map<String, TempEffect> skills1Buff = new HashMap<>();
    public static Map<String, TempEffect> skills2Buff = new HashMap<>();
    public static Map<String, TempEffect> skills3Buff = new HashMap<>();

    public static Map<String,ArrayList<TempEffect>> Debuffstracker = new HashMap<>();
    public static Map<String,ArrayList<TempEffect>> Buffstracker = new HashMap<>();

    public Game(){
        //default constructor
    }




    public static void main(String[] args) {
        long startTime = System.nanoTime(); // start

        // Prepare each skill for everyunit containing the constant values for the damage formula
        initSkills(playerList);

        // Initiate the wave 1
        initGame(playerList);

        //Player p1 = new Alexa("Alexa",200,true,1400,1500,25000,35,160,65,120,5,4);
        //p1.skillAI(currentTarget, playerList);


        if (playerList.containsKey("GeneralPurrgis")){
            while (listE1.size() > 0 && listA.size() > 0) { // WAVE 1 stops whenever one list is empty
                // simule le déroulement des tours de jeu ( 1 tour = 1 personnage à 100% ) // boucler sur le nombre de joueurs vivants
                //displayCR(playerList)

                // Simulate every unit turn
                CRcounter(playerList); // print CR de chaque personnage
                // activePlayer est attribué

                // Get the active player
                // Todo : Coder les actions


                updateDebuffsStartTurn(activePlayer);
                if(activePlayer instanceof Hero){ // check if active player is dead, and do the updates everywhere when it's the case
                    updateDeadList(activePlayer,listA);
                } else {
                    updateDeadList(activePlayer,listE1);
                }

                if (activePlayer.getAlive()) { // if he is alive after the debuffs applied on him (poison, burn, etc...)
                    if (activePlayer instanceof Hero) { // hero attacking
                        Player currentTarget = getLowHP(listE1);
                        activePlayer.skillAI(currentTarget,playerList); // if the current player is a hero, attacks a monster
                        // reduce certains debuffs durations / buffs 




                        Iterator<TempEffect> i =  Debuffstracker.get(activePlayer.getName()).iterator();


                        switch (chooseSkill(activePlayer)){ // skill
                            case 1 : // skill 1
                                switch (activePlayer.getName()){
                                    case "Luluca":
                                        applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                                getBuffs(activePlayer),
                                                getSkillRate(activePlayer,1),
                                                getFlatMod(activePlayer,1),
                                                getFlat2Mod(activePlayer,currentTarget),
                                                getpow(activePlayer,1),
                                                getSkillEnhanceMod(activePlayer,1),
                                                getextMod(activePlayer,currentTarget,1),
                                                getHitTypeMod(activePlayer,currentTarget),
                                                getTargetDebuff(currentTarget),
                                                getElementalMod(activePlayer,1),
                                                currentTarget.getDefense(),
                                                getDefbreakMod(currentTarget),
                                                0.0,0.0,0.0
                                        ));


                                        while (i.hasNext()) { // list of active player debuff on skill 1
                                            TempEffect effect = i.next();
                                            Random r = new Random();
                                            int randomInt = r.nextInt(100);
                                            if (randomInt < effect.getRate()*100){
                                                randomInt = r.nextInt(100);
                                                if (randomInt < 85){ // test 85%
                                                    Iterator<TempEffect> j =  Debuffstracker.get(currentTarget.getName()).iterator();
                                                    while (j.hasNext()) { // check among all target debuff if it exists
                                                        TempEffect effectTarget = i.next();
                                                        if (effectTarget.getType()==3){ // def decrease
                                                            effectTarget.setDuration(2);
                                                            break;
                                                        }
                                                    }
                                                    Debuffstracker.get(currentTarget.getName()).add(effect);
                                                    effect.applyEffects(activePlayer,currentTarget);
                                                }
                                            }
                                        }
                                    case "Alexa" :
                                        double secondAttack = getHitTypeMod(activePlayer,currentTarget);
                                        applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                                getBuffs(activePlayer),
                                                getSkillRate(activePlayer,1),
                                                getFlatMod(activePlayer,1),
                                                getFlat2Mod(activePlayer,currentTarget),
                                                getpow(activePlayer,1),
                                                getSkillEnhanceMod(activePlayer,1),
                                                getextMod(activePlayer,currentTarget,1),
                                                secondAttack,
                                                getTargetDebuff(currentTarget),
                                                getElementalMod(activePlayer,1),
                                                currentTarget.getDefense(),
                                                getDefbreakMod(currentTarget),
                                                0.0,0.0,0.0
                                        ));
                                        if (secondAttack >= 1.5) { // Alexa second attack
                                            applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                                    getBuffs(activePlayer)*0.75,
                                                    getSkillRate(activePlayer,1),
                                                    getFlatMod(activePlayer,1),
                                                    getFlat2Mod(activePlayer,currentTarget),
                                                    getpow(activePlayer,1),
                                                    getSkillEnhanceMod(activePlayer,1),
                                                    getextMod(activePlayer,currentTarget,1),
                                                    getHitTypeMod(activePlayer,currentTarget),
                                                    getTargetDebuff(currentTarget),
                                                    getElementalMod(activePlayer,1),
                                                    currentTarget.getDefense(),
                                                    getDefbreakMod(currentTarget),
                                                    0.0,0.0,0.0
                                            ));
                                        }

                                    case "SeasideBellona" :
                                        applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                                getBuffs(activePlayer),
                                                getSkillRate(activePlayer,1),
                                                getFlatMod(activePlayer,1),
                                                getFlat2Mod(activePlayer,currentTarget),
                                                getpow(activePlayer,1),
                                                getSkillEnhanceMod(activePlayer,1),
                                                getextMod(activePlayer,currentTarget,1),
                                                getHitTypeMod(activePlayer,currentTarget),
                                                getTargetDebuff(currentTarget),
                                                getElementalMod(activePlayer,1),
                                                currentTarget.getDefense(),
                                                getDefbreakMod(currentTarget),
                                                0.0,0.0,0.0
                                        ));

                                        while (i.hasNext()) { // list of active player debuff on skill 1
                                            TempEffect effect = i.next();
                                            Random r = new Random();
                                            int randomInt = r.nextInt(100);
                                            if (randomInt < effect.getRate()*100){
                                                randomInt = r.nextInt(100);
                                                if (randomInt < 85){ // test 85%
                                                    Iterator<TempEffect> j =  Debuffstracker.get(currentTarget.getName()).iterator();
                                                    while (j.hasNext()) { // check among all target debuff if it exists
                                                        TempEffect effectTarget = i.next();
                                                        if (effectTarget.getType()==27){ // target
                                                            effectTarget.setDuration(2);
                                                            break;
                                                        }
                                                    }
                                                    Debuffstracker.get(currentTarget.getName()).add(effect);
                                                }
                                            }
                                        }

                                    case "GeneralPurrgis" :
                                        applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                                getBuffs(activePlayer),
                                                getSkillRate(activePlayer,1),
                                                getFlatMod(activePlayer,1),
                                                getFlat2Mod(activePlayer,currentTarget),
                                                getpow(activePlayer,1),
                                                getSkillEnhanceMod(activePlayer,1),
                                                getextMod(activePlayer,currentTarget,1),
                                                getHitTypeMod(activePlayer,currentTarget),
                                                getTargetDebuff(currentTarget),
                                                getElementalMod(activePlayer,1),
                                                currentTarget.getDefense(),
                                                getDefbreakMod(currentTarget),
                                                0.0,0.0,0.0
                                        ));

                                        while (i.hasNext()) { // list of active player debuff on skill 1
                                            TempEffect effect = i.next();
                                            Random r = new Random();
                                            int randomInt = r.nextInt(100);
                                            if (randomInt < effect.getRate()*100){
                                                randomInt = r.nextInt(100);
                                                if (randomInt < 85){ // test 85%
                                                    Iterator<TempEffect> j =  Debuffstracker.get(currentTarget.getName()).iterator();
                                                    while (j.hasNext()) { // check among all target debuff if it exists
                                                        TempEffect effectTarget = i.next();
                                                        if (effectTarget.getType()==27){ // target
                                                            effectTarget.setDuration(2);
                                                            break;
                                                        }
                                                    }
                                                    Debuffstracker.get(currentTarget.getName()).add(effect);
                                                }
                                            }
                                        }
                                }






                        }


                        //System.out.println(activePlayer.getName() +" hit " + getLowHP(listE1).getName());
                        updateDeadList(currentTarget, listE1); // update ennemy alives list
                    } else { // monster attacking
                        Player currentTarget = getTarget(listA); // find a target among allies
                        activePlayer.action(playerList.get(currentTarget.getName()));
                        if (currentTarget.getName().equals("GeneralPurrgis")) {
                            if (playerList.get(currentTarget.getName()).getAlive()) { // if he is alive = true
                                for (Map.Entry player : playerList.entrySet()) { // Sort the list of speeds to get the fastest unit
                                    Player pl = (Player) player.getValue();
                                    pl.setNumberOftick(Math.max(0, pl.getNumberOftick() - 0.18 * tickValue)); // actualise la CR bar de tout le monde en décalant tout le monde
                                    // avec le push de GPurrgis
                                    pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed()))); // current CR = nb_ticks/total_ticks
                                }
                            }
                        }


                        //System.out.println(activePlayer.getName() +" hit " + target);
                        updateDeadList(currentTarget, listA); // update allies alive list
                    }

                }
            }
        } else {
            while (listE1.size() > 0 && listA.size() > 0) { // WAVE 1 stops whenever one list is empty
                // simule le déroulement des tours de jeu ( 1 tour = 1 personnage à 100% ) // boucler sur le nombre de joueurs vivants
                //displayCR(playerList)

                // Simulate every unit turn
                CRcounter(playerList); // print CR de chaque personnage
                // activePlayer est attribué

                // Get the active player
                // Todo : Coder les actions


                updateDebuffsStartTurn(activePlayer);

                if(activePlayer instanceof Hero){ // check if active player is dead, and do the updates everywhere when it's the case
                    updateDeadList(activePlayer,listA);
                } else {
                    updateDeadList(activePlayer,listE1);
                }

                if (activePlayer.getAlive()) { // if he is alive after the debuffs applied on him (poison, burn, etc...)
                    if (activePlayer instanceof Hero) { // hero attacking

                        Player currentTarget = getLowHP(listE1); // define a target among all ennemies

                        switch (chooseSkill(activePlayer)){
                            case 1 : // use skill 1
                                applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                                getBuffs(activePlayer),
                                                getSkillRate(activePlayer,1),
                                                getFlatMod(activePlayer,1),
                                                getFlat2Mod(activePlayer,currentTarget),
                                                getpow(activePlayer,1),
                                                getSkillEnhanceMod(activePlayer,1),
                                                getextMod(activePlayer,currentTarget,1),
                                                getHitTypeMod(activePlayer,currentTarget),
                                                getTargetDebuff(currentTarget),
                                                getElementalMod(activePlayer,1),
                                                currentTarget.getDefense(),
                                                getDefbreakMod(currentTarget),
                                        0.0,0.0,0.0
                                ));
                                break;
                            case 2 : // use skill 2
                                skills2tracker.put(activePlayer.getName(),skills2Cooldown.get(activePlayer.getName())); // reset the CD of the S2
                                Skills skill = skills2Stored.get(activePlayer.getName());
                                if (skill instanceof off_aoe_damage) { // OFF AOE
                                    for (String player : listE1){ // attack each enemy
                                        applyDamage(playerList.get(player),damageDealt(activePlayer.getAttack(),
                                                getBuffs(activePlayer),
                                                getSkillRate(activePlayer,1),
                                                getFlatMod(activePlayer,1),
                                                getFlat2Mod(activePlayer,playerList.get(player)),
                                                getpow(activePlayer,1),
                                                getSkillEnhanceMod(activePlayer,1),
                                                getextMod(activePlayer,playerList.get(player),1),
                                                getHitTypeMod(activePlayer,playerList.get(player)),
                                                getTargetDebuff(playerList.get(player)),
                                                getElementalMod(activePlayer,1),
                                                playerList.get(player).getDefense(),
                                                getDefbreakMod(playerList.get(player)),
                                                0.0,0.0,0.0
                                        ));
                                    }
                                } else if (skill instanceof off_mono_damage) {
                                    applyDamage(currentTarget,damageDealt(activePlayer.getAttack(),
                                            getBuffs(activePlayer),
                                            getSkillRate(activePlayer,2),
                                            getFlatMod(activePlayer,2),
                                            getFlat2Mod(activePlayer,currentTarget),
                                            getpow(activePlayer,2),
                                            getSkillEnhanceMod(activePlayer,2),
                                            getextMod(activePlayer,currentTarget,2),
                                            getHitTypeMod(activePlayer,currentTarget),
                                            getTargetDebuff(currentTarget),
                                            getElementalMod(activePlayer,2),
                                            currentTarget.getDefense(),
                                            getDefbreakMod(currentTarget),
                                            0.0,0.0,0.0
                                    ));
                                } else if (skill instanceof def_aoe_shield) {
                                    switch (activePlayer.getName()){
                                        case "Luluca" : // Luluca S2
                                            for (String player : listA) { // attack each enemy
                                                playerList.get(player).setShield(playerList.get(player).getShield() + 0.375*activePlayer.getAttack());
                                            }
                                    }

                                }


                                break;
                            case 3 : // use skill 3
                                skills3tracker.put(activePlayer.getName(),skills3Cooldown.get(activePlayer.getName())); // reset the CD of the S3

                                break;

                        }
                        //System.out.println(activePlayer.getName() +" hit " + getLowHP(listE1).getName());
                        updateDeadList(currentTarget, listE1); // update ennemy alives list

                    } else { // monster attacking
                        Player currentTarget = getTarget(listA); // find a target among allies

                        switch (chooseSkill(activePlayer)){
                            case 1 :




                                activePlayer.action(currentTarget); // if the current player is a hero, attacks a monster
                                break;
                            case 2 :

                                break;

                        }

                        activePlayer.action(playerList.get(currentTarget.getName()));

                        //System.out.println(activePlayer.getName() +" hit " + target);
                        updateDeadList(currentTarget, listA); // update allies alive list
                    }
                }


            }
        }


        long endTime = System.nanoTime();
        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in nanoseconds  : " + timeElapsed);
        System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);

        System.out.println(damageDealt(2500,0,1,0,0,1,0,0.3+0.1,
                2.50,0,1.1,1940,0,0,0,0));

    }

    private static void updateDeadList(Player currentTarget, ArrayList<String> listE1) {
        if (playerList.get(currentTarget.getName()).getHealth() <= 0) { // if dead we update our monsterslist
            for (Iterator<String> iterator = listE1.iterator(); iterator.hasNext();) {
                String deadName = iterator.next();
                if (deadName.equals(currentTarget.getName())){
                    playerList.get(currentTarget.getName()).setAlive(false); // considers dead /
                    System.out.println(deadName +" is dead ");
                    iterator.remove(); // remove the monster from listE1
                    /*
                    playerList.forEach((key, value) -> {
                        System.out.println(key + "  : " + value.getHealth());
                    });*/
                }
            }
        }
    }

    public static void CRcounter(Map<String,Player> playerList) {  // calcule et update le CR de tout le monde pour le tour du joueur en cours

        double jumpCRbar = tickValue; // compteur inverse de tick commençant à tickValue, et diminue vers 0. Saute de joueur de joueur

        // display CR
        // We store key = CR and value = String name so it's sorted ; easy to display
        TreeMap<Double,String> displayCRForPlayers = new TreeMap<>(Collections.reverseOrder()); // DESC order of CR displayed

        playerList.forEach((key, value) -> displayCRForPlayers.put(value.getCRinPercentage(),key));

        activePlayer = playerList.get(displayCRForPlayers.firstEntry().getValue()); // set activePlayer as the one having 100% CR via his name
        //System.out.println(displayCRForPlayers.firstEntry().getValue() + "'s turn !");
        playerList.get(displayCRForPlayers.firstEntry().getValue()).setCRinPercentage(0); // reset CR bar of the active player

        // Iterate over TreeMap to display CR
        /*
        Set set = displayCRForPlayers.entrySet();
        for (Object o : set) {
            Map.Entry me = (Map.Entry) o;
            System.out.println( me.getValue() + " has a CR of : " + String.format("%.2f", me.getKey()) +"%");
        }*/


        if (activePlayer.getCRinPercentage() < 100.10) { // update only if there is no unit beyond 100% CR
            // Prepare the value of the next step
            for (Map.Entry player : playerList.entrySet()) {
                Player pl = (Player) player.getValue();
                pl.setNumberOftick((100 - pl.getCRinPercentage()) / (pl.getSpeed() / tickValue)); // nombre de ticks à réaliser pour atteindre 100%
                if (pl.getNumberOftick() < jumpCRbar) {
                    jumpCRbar = pl.getNumberOftick();
                }
            }

            // Jump to the next step by removing from everyone NumberOfTick value
            for (Map.Entry player : playerList.entrySet()) { // Sort the list of speeds to get the fastest unit
                Player pl = (Player) player.getValue();
                pl.setNumberOftick(pl.getNumberOftick() - jumpCRbar); // actualise la CR bar de tout le monde en décalant tout le monde linéairement
                // du nombre de tick du joueur qui atteint la barre 0 en premier
                pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed()))); // current CR = nb_ticks/total_ticks
            }
        /*
        System.out.println("------------------------------------- \n" +
            "-------------------------------------"
        );*/
        }
    }

    public static void updateDebuffsStartTurn(Player pl) {
        Iterator<TempEffect> i =  Debuffstracker.get(pl.getName()).iterator();
        while (i.hasNext()){
            TempEffect effect = i.next();
            switch (effect.getType()){
                case 11: // burn
                    pl.setHealth(pl.getHealth()-effect.getCaster().getAttack()*0.6*1.871/((1-0.7)/pl.getDefense()/300+1));
                    effect.setDuration(effect.getDuration()-1);
                    //effect.getTarget().setHealth(effect.getTarget().getHealth()-effect.getCaster().getAttack()*0.6*1.871/((1-0.7)/effect.getTarget().getDefense()/300+1)); // same instructions
                    if (effect.getDuration() == 0){ // remove the debuff when it's over
                        i.remove();
                    }
                    break;
                case 13 : // bleed
                    pl.setHealth(pl.getHealth()-effect.getCaster().getAttack()*0.3*1.871/((1-0.7)/pl.getDefense()/300+1));
                    effect.setDuration(effect.getDuration()-1);
                    if (effect.getDuration() == 0){ // remove the debuff when it's over
                        i.remove();
                    }
                    break;
                case 19 : // poison
                    pl.setHealth(pl.getHealth() - 0.05*pl.getMaxhp());
                    effect.setDuration(effect.getDuration()-1);
                    if (effect.getDuration() == 0){ // remove the debuff when it's over
                        i.remove();
                    }
                    break;
            }
        }

    }


    public static void initSkills(Map<String,Player> playerList){ // initiate skills and debuffs for every potential unit
        for (Map.Entry player : playerList.entrySet()) {
            Player pl = (Player) player.getValue();
            switch (pl.getName()){
                case "Luluca":
                    // Skills //
                    Skills Luluca_S1 = new off_mono_damage(1,1,0,1.1,0);
                    skills1Stored.put(pl.getName(),Luluca_S1);
                    Skills Luluca_S2 = new def_aoe_shield(1,1,1,1,4);
                    skills2Stored.put(pl.getName(),Luluca_S2);
                    Skills Luluca_S3 = new off_aoe_damage(0.9,1.05,0,1.1,5);
                    skills3Stored.put(pl.getName(),Luluca_S3);

                    // Debuffs //
                    ArrayList<TempEffect> Luluca_S1_Debuff_List = new ArrayList<>();
                    TempEffect Luluca_S1_DecreaseDefense= new DecreaseDefense(2,0.5);
                    Luluca_S1_Debuff_List.add(Luluca_S1_DecreaseDefense);
                    skills1Debuff.put(pl.getName(),Luluca_S1_Debuff_List);

                    ArrayList<TempEffect> Luluca_S3_Debuff_List = new ArrayList<>();
                    TempEffect Luluca_S3_DecreaseDefense = new DecreaseDefense(2,0.6);
                    Luluca_S3_Debuff_List.add(Luluca_S3_DecreaseDefense);
                    skills3Debuff.put(pl.getName(),Luluca_S3_Debuff_List);
                    break;

                case "Alexa" :
                    Skills Alexa_S1 = new off_mono_damage(1,1,0,1.1,0);
                    skills1Stored.put(pl.getName(),Alexa_S1);
                    Skills Alexa_S2 = new off_mono_damage(1,1,0.3,1.1,3);
                    skills2Stored.put(pl.getName(),Alexa_S2);
                    Skills Alexa_S3 = new off_mono_damage(1.5,0.9,0.1,1.1,5);
                    skills3Stored.put(pl.getName(),Alexa_S3);

                    // Debuffs //
                    ArrayList<TempEffect> Alexa_S2_Debuff_List = new ArrayList<>();
                    TempEffect Alexa_S2_Poison_1 = new DecreaseDefense(2,1);
                    TempEffect Alexa_S2_Poison_2 = new DecreaseDefense(2,1);
                    Alexa_S2_Debuff_List.add(Alexa_S2_Poison_1);
                    Alexa_S2_Debuff_List.add(Alexa_S2_Poison_2);
                    skills2Debuff.put(pl.getName(),Alexa_S2_Debuff_List);
                    break;

                case "SeasideBellona" :
                    Skills SeasideBellona_S1 = new off_aoe_damage(1,1,0,1.1,0);
                    skills1Stored.put(pl.getName(),SeasideBellona_S1);
                    Skills SeasideBellona_S3 = new off_aoe_damage(1,1,0.3,1.1,4);
                    skills3Stored.put(pl.getName(),SeasideBellona_S3);

                    // Debuffs //

                    ArrayList<TempEffect> SeasideBellona_S1_Debuff_List = new ArrayList<>();
                    TempEffect SeasideBellona_S1_Target = new Target(2,0.75);
                    SeasideBellona_S1_Debuff_List.add(SeasideBellona_S1_Target);
                    skills3Debuff.put(pl.getName(),SeasideBellona_S1_Debuff_List);

                    ArrayList<TempEffect> SeasideBellona_S3_Debuff_List = new ArrayList<>();
                    TempEffect SeasideBellona_S3_Unbuffable = new Unbuffable(2,1);
                    TempEffect SeasideBellona_S3_Unhealable = new Unhealable(2,1);
                    SeasideBellona_S3_Debuff_List.add(SeasideBellona_S3_Unbuffable);
                    SeasideBellona_S3_Debuff_List.add(SeasideBellona_S3_Unhealable);
                    skills3Debuff.put(pl.getName(),SeasideBellona_S3_Debuff_List);

                    break;

                case "GeneralPurrgis" :
                    Skills GeneralPurrgis_S1 = new off_aoe_damage(0.8,1,0.3,1,0);
                    skills1Stored.put(pl.getName(),GeneralPurrgis_S1);
                    Skills GeneralPurrgis_S3 = new off_aoe_damage(0.8,1,0.3,1,5);
                    skills3Stored.put(pl.getName(),GeneralPurrgis_S3);

                    // Debuffs //
                    ArrayList<TempEffect> GeneralPurrgis_S1_Debuff_List = new ArrayList<>();
                    TempEffect GeneralPurrgis_S1_Provoke = new Provoke(1,0.8);
                    GeneralPurrgis_S1_Debuff_List.add(GeneralPurrgis_S1_Provoke);
                    skills1Debuff.put(pl.getName(),GeneralPurrgis_S1_Debuff_List);

                    ArrayList<TempEffect> GeneralPurrgis_S3_Debuff_List = new ArrayList<>();
                    TempEffect GeneralPurrgis_S1_Stun = new Stun(1,1);
                    GeneralPurrgis_S3_Debuff_List.add(GeneralPurrgis_S1_Stun);
                    skills3Debuff.put(pl.getName(),GeneralPurrgis_S3_Debuff_List);
                    break;

                case "TaranorGuard" :
                    Skills TaranorGuard_S1 = new off_aoe_damage(0.8,1,0.3,1,0);
            }

        }

    }

    public static void initGame(Map<String,Player> playerList) {
        // Hero
        Player p1 = new Hero("GeneralPurrgis", 152, true, 1365, 1727, 23275, 37, 162, 12, 106, 5,5, "");
        Player p2 = new Hero("Alexa", 116, true, 2690, 812, 7400, 89, 306, 82, 9, 5,1, "");
        Player p3 = new Hero("Luluca", 204, true, 1786, 1056, 6237, 64, 277, 88, 14, 13,1, "");
        Player p4 = new Hero("SeasideBellona", 126, true, 3426, 1126, 13839, 94, 308, 63, 0, 5,1, "");


        // Monster
        Player p5 = new Monster("Naga1", 154, true, 6113, 1340, 13358, 50, 150, 0, 0, 5,2);
        Player p6 = new Monster("Naga2", 154, true, 6113, 1340, 13358, 50, 150, 0, 0, 5,2);
        Player p7 = new Monster("Dragona", 175, true, 3234, 1392, 20241, 50, 150, 0, 0, 5,2);
        Player p8 = new Monster("Wyvern",242,true,6835,1940,233578,50,150,0,80,5,2);


        // Add heroes on data
        front = p1;
        playerList.put(p1.getName(), p1); // p1 is the front
        listA.add(p1.getName());
        playerList.put(p2.getName(), p2);
        listA.add(p2.getName());
        playerList.put(p3.getName(), p3);
        listA.add(p3.getName());
        playerList.put(p4.getName(), p4);
        listA.add(p4.getName());


        // Add monsters on data
        playerList.put(p5.getName(), p5);
        listE1.add(p5.getName());
        //perNames.put( 100.00,"Naga1");
        playerList.put(p6.getName(), p6);
        listE1.add(p6.getName());
        //perNames.put( 100.00,"Naga2");
        playerList.put(p7.getName(), p7);
        listE1.add(p7.getName());
        //perNames.put( 100.00,"Dragona");


        skills3Cooldown.put(p1.getName(),5);
        skills2Cooldown.put(p2.getName(),3);
        skills3Cooldown.put(p2.getName(),4);
        skills2Cooldown.put(p3.getName(),4);
        skills3Cooldown.put(p3.getName(),5);
        skills3Cooldown.put(p4.getName(),4);

        skills3tracker.put(p1.getName(),5);
        skills2tracker.put(p2.getName(),3);
        skills3tracker.put(p2.getName(),4);
        skills2tracker.put(p3.getName(),4);
        skills3tracker.put(p3.getName(),5);
        skills3tracker.put(p4.getName(),4);

        //speedRNG(playerList); // applique la speed RNG
        setCRBar(playerList); // permet de setup les variables CR/% pour chaque personnage

        for (Player player : playerList.values()) {
            double minRNG = 0.00;
            double maxRNG = 5.00;
            double random = new Random().nextDouble();
            player.setCRinPercentage((player.getSpeed() * 100 / tickValue)+(minRNG + (random * (maxRNG - minRNG)))); // initialise la valeur et facilite l'affichage
            player.setNumberOftick(100 / (player.getSpeed() / tickValue)); //
        }
    }

    public static Player getLowHP(ArrayList<String> list) { // return the player with the lowest hp
        double perc = playerList.get(list.get(0)).getHealth() * 100 / playerList.get(list.get(0)).getMaxhp();
        Player pl = playerList.get(list.get(0)) ;

        for (int i = 1; i < list.size(); i++) {
            double p = playerList.get(list.get(i)).getHealth() * 100 / playerList.get(list.get(i)).getMaxhp();
            if ( p < perc) { // we get the %
                perc = p;
                pl = playerList.get(list.get(i));
            }
        }
        return pl;
    }

    public static Player getTarget(ArrayList<String> list){
        double randNumber = Math.random();
        randNumber = randNumber * 100;
        if (front.getAlive()){ // if he is alive
            if (list.size() > 2){ // case front is 40% and rest is same probability
                if (randNumber < 40){ // target the front at 40%
                    return front;
                } else { // target the dpses
                    int index = new Random().nextInt(list.size()-1); // generate [0,list.size]
                    return playerList.get(list.get(index)); // get the player with the name
                }
            } else {
                if (randNumber > 60){
                    return front; // get the front
                } else {
                    return playerList.get(list.get(0)); // get the other survivor
                }
            }
        } else { // front is dead everyone has the same chance to get targeted
            int index = new Random().nextInt(list.size()); // generate [0,list.size]
            return playerList.get(list.get(index)); // get the player with the name
        }
    }

    public static void setCRBar(Map<String,Player> playerList) {
        ArrayList<Double> sortedCRList = new ArrayList<>(); // arraylist de speed
        //sortedCRList.add(243); // speed wyvern

        //HashMap<String,Player> playerList =  sortByComparator()

        playerList.forEach((key, value) -> { // Sort the list of speeds to get the fastest unit
            sortedCRList.add(value.getSpeed());
        });

        Collections.sort(sortedCRList);
        tickValue = sortedCRList.get(playerList.size()-1); // Construction de tickValue en fonction de la speed du personnage le plus rapide
    }

    // Sum effectiveDamage and mitigationDamage to get finalDamage
    // Attack = atk
    // Atkmod = (buffs / bonus atk)
    // Rate = rate
    // FlatMod = (damage based on hp/speed/any stat written on skill description)
    // Flat2mod = DDJ
    // gamma(1+pow) = pow!
    // EnhanceMod = Skill Enhance Damage Increase
    // HitTypeMod = Miss = .75 Modifier   Hit = 1.00 Modifier   Crushing/Strike = 1.3 Modifier   Critical Hit = Critical Damage Stat Modifier
    // ElementMod = 1.1 or 1.0
    // DamageUpMod = Sigret S3,wyvern element advantage, EE : mostly multipliers
    // TargetDebuffMod = 1.15 or 1

    // DamageReduction = Adamant
    // DamageTransfer = Aurius
    // DEFmod = 1 ?
    // DefensePen = Elyha

    public static Map<String,Double> prepareDamage(Player activePlayer, Player target, ArrayList<String> listA,ArrayList<String> listE1,int skill){
        Map<String,Double> result = new HashMap<String,Double>() ;
        result.put("Attack",activePlayer.getAttack());
        result.put("Atkmod",getBuffs(activePlayer));  // buffs
        result.put("SkillRate",getSkillRate(activePlayer,skill)); // stored
        result.put("FlatMod",getFlatMod(activePlayer,skill));  // dépend S1/S2/S3 +
        result.put("Flat2Mod",getFlat2Mod(activePlayer,target));      // dépend artéfact et cible
        result.put("pow",getpow(activePlayer,skill)); // stored
        result.put("SkillEnhanceMods",getSkillEnhanceMod(activePlayer,skill));  // stored
        result.put("extMods", getextMod(activePlayer,target,skill));
        result.put("HitTypeMod",getHitTypeMod(activePlayer,target));  // dépend coup critique ou non
        result.put("Target",getTargetDebuff(target));
        result.put("ElementalMod",getElementalMod(activePlayer,skill)); // stored
        result.put("Defense",target.getDefense());
        result.put("DefbreakMod",getDefbreakMod(target));
        //result.put("PenMod", ); // TODO Kise case
        result.put("PenMod",0.0);
        result.put("DamageReduction", 0.0);
        result.put("DamageSharing", 0.0);

        return result;
    }

    public static double damageDealt(double Attack, double Atkmod, double SkillRate, double FlatMod,double FlatMod2, double pow, double SkillEnhanceMods,double extMods, double HitTypeMod, double Target, double ElementalMod, double Defense,
                                     double DefbreakMod, double PenMod, double DamageReduction, double DamageSharing){
        double atkMods = (Attack * (1+Atkmod) * SkillRate + FlatMod ) * 1.871 + (FlatMod2);
        double DmgMods = (pow) * (1+SkillEnhanceMods)* (1+extMods);  // *( arti damage + hunt bonus damage + ...)
        double OtherMods = HitTypeMod * (1+Target) * ElementalMod;
        double DefenseMod = ((1 - PenMod) * (1 - DefbreakMod) * (Defense) / 300 +1);
        double DamageMitigation = (1-DamageReduction) * (1-DamageSharing);
        return atkMods*DmgMods*OtherMods*DamageMitigation/DefenseMod;
    }

    public static void applyDamage(Player player, double damageReceived){
        double damage = player.getShield() - damageReceived;
        if (damage > 0){
            player.setShield(player.getShield() - damageReceived);
        } else {
            player.setShield(0);
            player.setHealth(player.getHealth()-Math.abs(damage));
        }
    }

    // naga skill 2 ratio = 1.3
    // draguna skill 2 ratio = 1.4


    //((this.getAtk(skillId)*rate + flatMod)*dmgConst + flatMod2) * pow * skillEnhance * elemAdv * target * dmgMod;
    // ((1-dmgReduc)*(1-dmgTrans))/(((this.def / 300)*this.getPenetration(skill)) + 1);

    public static int chooseSkill(Player player){
        int choice = skills3Cooldown.get(player.getName());
        if (choice<=0) { // <=0 handle the first turn of the game
            return 3;
        }
        choice = skills2Cooldown.get(player.getName());
        if (choice<=0){ // <=0 to handle the first turn of the game
            return 2;
        }
        return 1;
    }

    public static double getBuffs(Player player) { // if atk buff, return 0.3, if not return 0
        Iterator<TempEffect> i =  Buffstracker.get(player.getName()).iterator();
        while (i.hasNext()){
            TempEffect effect = i.next();
            if (effect.getType()==2){
                return 0.5;
            } else if (effect.getType()==60){
                return 0.7;
            }
        }
        return 0.0;
    }

    public static double getFlatMod(Player player, int skill){
        switch (player.getName()){
            case "GeneralPurrgis":
                switch (skill){
                    case 1:
                        return player.getMaxhp()*0.06;
                    case 3:
                        return player.getMaxhp()*0.08;
                }
        }
        return 0.0;
    }

    public static double getFlat2Mod(Player player, Player target){
        switch (player.getName()){
            case "Alexa" :
                return target.getMaxhp()*0.03;
        }
        return 0.0;
    }

    public static double getSkillRate(Player player,int skill) { // find the good ratio for the right skill
        switch (skill){
            case 2 :
                return skills2Stored.get(player.getName()).getRatio();
            case 3 :
                return skills3Stored.get(player.getName()).getRatio();
        }
        return 1;
    }

    public static double getSkillEnhanceMod(Player player,int skill){
        switch (skill){
            case 2 :
                return skills2Stored.get(player.getName()).getEnhanceMod();
            case 3 :
                return skills3Stored.get(player.getName()).getEnhanceMod();
        }
        return 0;
    }

    public static double getElementalMod(Player player,int skill) {
        switch (skill){
            case 2 :
                return skills2Stored.get(player.getName()).getElement();
            case 3 :
                return skills3Stored.get(player.getName()).getElement();
        }
        return 1;
    }

    public static double getpow(Player player,int skill){
        switch (skill){
            case 2 :
                return skills2Stored.get(player.getName()).getPow();
            case 3 :
                return skills3Stored.get(player.getName()).getPow();
        }
        return 1;
    }

    public static double getextMod(Player player, Player target,int skill) {
        switch (player.getName()){
            case "Enott" :
                if (skill ==1 ){ // only on S1
                    return 0.005*(100-target.getHealth()*100/target.getMaxhp()); // 0.5% damage increase by 1% lost
                }
                break;
            case "Jena": // number of debuffs
                return 0;
            case "Karine" :
                if (true) { //  if crit
                    return 0.5;
                }
                return 0;
            case "Luluca":
                if (skill==1){
                    return 0.02*(100-target.getHealth()*100/target.getMaxhp()); // 20% of missing health in %
                }
                break;
            case "Luna" :
                return 1;
            case "Kise" :
                return 0.35*player.getHealth()*100/player.getMaxhp();

        }
        return 0;
    }

    public static double getTargetDebuff(Player target){
        Iterator<TempEffect> i =  Debuffstracker.get(target.getName()).iterator();
        while (i.hasNext()){
            TempEffect effect = i.next();
            if (effect.getType()==27) { // boost 15% damage
                return 0.15;
            }
        }
        return 0;
    }

    public static double getDefbreakMod(Player target){
        Iterator<TempEffect> i =  Debuffstracker.get(target.getName()).iterator();
        while (i.hasNext()){
            TempEffect effect = i.next();
            if (effect.getType()==3) { // decrease 70% defense
                return 0.7;
            }
        }
        return 0;
    }

    public static double getHitTypeMod(Player player, Player target){
        switch (player.getElement()) {
            case 1: // water
                if (target.getElement() == 3) { // earth
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < player.getCc()) { // crit hit
                            return player.getCdmg();
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
                    if (randomInt < player.getCc()) { // crit hit
                        return player.getCdmg();
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
                if (target.getElement() == 1) { // water
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < player.getCc()) { // crit hit
                            return player.getCdmg();
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
                    if (randomInt < player.getCc()) { // crit hit
                        return player.getCdmg();
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
                if (target.getElement() == 2) { // fire
                    Random r = new Random();
                    int randomInt = r.nextInt(100);
                    if (randomInt < 50) { // miss hit
                        return 0.75;
                    } else {
                        randomInt = r.nextInt(100);
                        if (randomInt < player.getCc()) { // crit hit
                            return player.getCdmg();
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
                    if (randomInt < player.getCc()) { // crit hit
                        return player.getCdmg();
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
                if (randomInt < player.getCc()) { // crit hit
                    return player.getCdmg();
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

    /*
    public static double getPenMod(Player player,int skill) {
        if (player.getName().equals("Kise") && skill==2){

    }*/

}