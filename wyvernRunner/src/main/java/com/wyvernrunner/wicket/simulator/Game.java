package com.wyvernrunner.wicket.simulator;

import com.wyvernrunner.wicket.simulator.Heroes.*;
import com.wyvernrunner.wicket.simulator.Interfaces.IFocus;
import com.wyvernrunner.wicket.simulator.Monsters_W13.Dragona;
import com.wyvernrunner.wicket.simulator.Monsters_W13.Naga;
import com.wyvernrunner.wicket.simulator.Monsters_W13.Wyvern;
import com.wyvernrunner.wicket.simulator.TempEffects.*;


import java.util.*;


public class Game {

    /**********************************************************
     *                      VARIABLES                         *
     **********************************************************/

    public static double tickValue; // taille de la barre de CR (exemple : si 300 speed qui est le plus rapide alors tickValue = 300 ) //
    public static Player activePlayer;
    public static Map<String,Player> playerList = new HashMap<>();
    public static Map<String,Player> deadplayerList = new HashMap<>();

    public static ArrayList<String> listA = new ArrayList<>(); // store names of the target lists of allies
    public static ArrayList<String> listE1 = new ArrayList<>(); // store names of the target lists of wave 1
    public static Player front;
    public static boolean wyvernAlive = true; // true = dead / false = alive
    private ArrayList<Integer> dualArray = new ArrayList<>(); // contains every possibility for dual attacks
    private static boolean firstTurn = true;


    public Game(){

    }


    public static void main(String[] args) {

        double victory = 0;



        long startTime = System.nanoTime(); // start
        int loop = 0;
        while (loop < 100000) {

            /**********************************************************
             *                     WAVE 1 START                       *
             **********************************************************/
            playerList.clear();
            deadplayerList.clear();
            listE1.clear();
            listA.clear();

            // Initiate the wave 1
            initGame(playerList);

            //Player p1 = new Alexa("Alexa",200,true,1400,1500,25000,35,160,65,120,5,4);
            //p1.skillAI(currentTarget, playerList);

                /*
                System.out.println("listA size is :" + listA.size());
                System.out.println("listE1 size is :" + listE1.size());
                 */


            /**********************************************************
             *                     FIGHT STARTS                       *
             **********************************************************/


            while (listE1.size() > 0 && listA.size() > 0) { // WAVE 1 stops whenever one list is empty
                // simule le déroulement des tours de jeu ( 1 tour = 1 personnage à 100% ) // boucler sur le nombre de joueurs vivants
                //displayCR(playerList)

                // simulate every unit turn
                CRcounter(playerList); // print each unit CR
                // activePlayer est attribué


                /**********************************************************
                 *         CHECK TICK DEBUFF BEFORE TURN STARTS           *
                 **********************************************************/

                updateTickStatus(activePlayer);

                /**********************************************************
                 *         CHECK IF PLAYER IS ALIVE AFTER TICKS           *
                 **********************************************************/

                if (activePlayer.getAlive()) { // if he is alive after the debuffs applied on him (poison, burn, etc...)
                    //System.out.println(activePlayer.getName() + "'s turn !");

                    /**********************************************************
                     *                       ALLY TURN                        *
                     **********************************************************/

                    if (activePlayer.getTeam() == 0) { // hero attacking
                        Player currentTarget = getLowHP(listE1);
                        activePlayer.skillAI(currentTarget, playerList, tickValue, listA, listE1); // if the current player is a hero, attacks a monster


                        updateDeadList(listE1); // update enemy alive list


                        /**********************************************************
                         *                      ENEMY TURN                        *
                         *******************************************************
                         * ***/

                    } else { // monster attacking
                        Player currentTarget = getTarget(listA); // find a target among allies
                        activePlayer.skillAI(currentTarget, playerList, tickValue, listA, listE1); // if the current player is a monster, attacks a hero

                        if (currentTarget instanceof GeneralPurrgis) {
                            for (String player : listA) {
                                Player pl = playerList.get(player);
                                // actualise la CR bar de tout le monde en décalant tout le monde avec le push de GPurrgis
                                pl.setCRinPercentage(pl.getCRinPercentage() + 16);
                            }
                        }

                        for (String Focusunit : listA) {
                            if (playerList.get(Focusunit) instanceof IFocus) {
                                // depending on the stacks
                                if (((IFocus) playerList.get(Focusunit)).getFocus() > 4) { // If the target was SeasideBellona, we check the nb of stacks
                                    ((IFocus) playerList.get(Focusunit)).triggerFocus(playerList.get(Focusunit), playerList, tickValue, listA, listE1); // Use S2
                                    ((IFocus) playerList.get(Focusunit)).setFocus(0); // reset stacks
                                }
                            }
                        }


                        updateDeadList(listA); // update allies alive list
                    }
                }

                /**********************************************************
                 *         REDUCE DURATION OF BUFFS & DEBUFFS             *
                 **********************************************************/

                updateTempEffect(activePlayer);

                /**********************************************************
                 *              CHECK ALLY OR ENEMY TURN                  *
                 **********************************************************/

                updateDeadStatus(activePlayer);

                /**********************************************************
                 *                     END OF A TURN                      *
                 **********************************************************/

            }
            // end of the wave 1 fight

            /**********************************************************
             *                    END OF A WAVE 1                     *
             **********************************************************/
            /*
            for (Map.Entry player : deadplayerList.entrySet()) {
                Player pl = (Player) player.getValue();
                System.out.println(pl.getName() + " has " + pl.getHealth() + " HP");
            }

             */


            /**********************************************************
             *                   BOSS FIGHT START                     *
             **********************************************************/

            initBoss(); // prepare fight against boss - reduce 1 turn buff/debuff, add wyvern on listE and playerlist

            while (listE1.size() > 0 && listA.size() > 0) {

                // simulate every unit turn
                CRcounter(playerList); // print each unit CR
                // activePlayer est attribué
                /**********************************************************
                 *         CHECK TICK DEBUFF BEFORE TURN STARTS           *
                 **********************************************************/

                updateTickStatus(activePlayer);

                /**********************************************************
                 *         CHECK IF PLAYER IS ALIVE AFTER TICKS           *
                 **********************************************************/

                if (activePlayer.getAlive()) { // if he is alive after the debuffs applied on him (poison, burn, etc...)
                    //System.out.println(activePlayer.getName() + "'s turn !");

                    /**********************************************************
                     *                       ALLY TURN                        *
                     **********************************************************/

                    if (activePlayer.getTeam() == 0) { // hero attacking
                        Player currentTarget = getLowHP(listE1);
                        activePlayer.skillAI(currentTarget, playerList, tickValue, listA, listE1); // if the current player is a hero, attacks a monster

                        if (activePlayer.getElement() != 1) { // if active player is not water, Wyvern self CR pushes 20%
                            for (String player : listE1) { // only wyvern
                                Player pl = playerList.get(player);
                                // actualise la CR bar de tout le monde en décalant tout le monde avec le push de GPurrgis
                                pl.setCRinPercentage(pl.getCRinPercentage() + 16);
                            }
                        }

                        updateDeadList(listE1); // update enemy alive list


                        /**********************************************************
                         *                      WYVERN TURN                       *
                         **********************************************************/

                    } else { // monster attacking
                        Player currentTarget = getWyvernTarget(listA, listE1, tickValue); // find a target among allies
                        if (activePlayer.getShield() > 0 && activePlayer instanceof Wyvern) {
                            ((Wyvern) activePlayer).skill2blow(playerList, tickValue, listA, listE1); // if wyvern has shield, it means it's time to blow entire team
                        } else {
                            activePlayer.skillAI(currentTarget, playerList, tickValue, listA, listE1); // if the current player is a monster, attacks a hero
                        }
                        if (currentTarget instanceof GeneralPurrgis && currentTarget.getHealth() > 0) { // if target is GP and alive
                            for (String player : listA) {
                                Player pl = playerList.get(player);
                                pl.setNumberOftick(Math.max(0, pl.getNumberOftick() - 0.16 * tickValue)); // actualise la CR bar de tout le monde en décalant tout le monde
                                // avec le push de GPurrgis
                                pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed())));
                            }
                        }

                        if (currentTarget instanceof IFocus) {
                            // depending on the stacks
                            if (((IFocus) currentTarget).getFocus() > 4) { // If the target was SeasideBellona, we check the nb of stacks
                                ((IFocus) currentTarget).triggerFocus(currentTarget, playerList, tickValue, listA, listE1); // Use S2
                                ((IFocus) currentTarget).setFocus(0); // reset stacks
                            }
                        }

                        updateDeadList(listA); // update allies alive list
                    }
                }

                /**********************************************************
                 *         REDUCE DURATION OF BUFFS & DEBUFFS             *
                 **********************************************************/

                updateTempEffect(activePlayer);

                /**********************************************************
                 *              CHECK ALLY OR ENEMY TURN                  *
                 **********************************************************/

                updateDeadStatus(activePlayer);

                /**********************************************************
                 *                     END OF A TURN                      *
                 **********************************************************/

            }

            /**********************************************************
             *                    END OF THE FIGHT                    *
             **********************************************************/

            /*
            for (Map.Entry player : deadplayerList.entrySet()) {
                Player pl = (Player) player.getValue();
                System.out.println(pl.getName() + " has " + pl.getHealth() + " HP");
            }
             */


            if (listE1.size() < 1) {
                victory++;
            }

            //System.out.println("--------------------");

            loop++;

        }
        long endTime = System.nanoTime();
        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in nanoseconds  : " + timeElapsed);
        System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);
        System.out.println("Winrate against wave 1 + wyvern is : " + (victory/100000)*100 + "%");




        /*
        System.out.println("listA size is :" + listA.size());
        System.out.println("listE1 size is :" + listE1.size());

        */
        /*
        for (int i = 0; i < listA.size(); i++) {
            System.out.println(listA.get(i));
        }
        for (int i = 0; i < listE1.size(); i++) {
            System.out.println(listE1.get(i));
        }
         */

    }

    private static void updateDeadList(ArrayList<String> list) {
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String deadName = iterator.next();
            if (playerList.get(deadName).getHealth() <= 0) {
                playerList.get(deadName).setAlive(false); // considers dead /
                // System.out.println(deadName + " is dead ");
                deadplayerList.put(deadName,playerList.get(deadName));
                playerList.remove(deadName);
                iterator.remove(); // remove the player from list

            }

        }
    }

    public static void CRcounter(Map<String,Player> playerList) {  // calcule et update le CR de tout le monde pour le tour du joueur en cours

        double jumpCRbar = tickValue; // compteur inverse de tick commençant à tickValue, et diminue vers 0. Saute de joueur de joueur

        // display CR
        // We store key = CR and value = String name, so it's sorted ; easy to display
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

            // pl.setNumberOftick((100 - pl.getCRinPercentage()) / (pl.getSpeed() / tickValue)); // nombre de ticks à réaliser pour att
            // pl.setNumberOftick(pl.getNumberOftick() - jumpCRbar); // actualise la CR bar de tout le monde en décalant tout le monde linéairement
            // pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed()))); // current CR = nb_ticks/total_ticks


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

    public static void initGame(Map<String,Player> playerList) {
        // Hero
        Player p1 = new GeneralPurrgis("GeneralPurrgis", 152, true, 1365, 1727, 23275, 37, 162, 12, 106, 5,5, 0,0);
        Player p2 = new Alexa("Alexa", 116, true, 2690, 812, 7400, 89, 306, 82, 9, 5, 1, 0, 0);
        Player p3 = new Luluca("Luluca" , 204, true, 1786, 1056, 6237, 64, 277, 88, 14, 13, 1, 0, 0);
        Player p4 = new SeasideBellona("SeasideBellona", 126, true, 3426, 1126, 13839, 94, 308, 63, 0, 5,1, 0,0);


        // Monster
        Player p5 = new Dragona("Dragona1", 175, true, 5294, 1392, 20241, 50, 150, 0, 0, 5,2,1,0);
        Player p6 = new Dragona("Dragona2", 175, true, 5294, 1392, 20241, 50, 150, 0, 0, 5,2,1,0);
        Player p7 = new Naga("Naga", 154, true, 2800, 1340, 13354, 50, 150, 0, 0, 5,2,1,0);



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
        playerList.put(p6.getName(), p6);
        listE1.add(p6.getName());
        playerList.put(p7.getName(), p7);
        listE1.add(p7.getName());


        //speedRNG(playerList); // apply speed RNG
        setCRBar(playerList); // permet de setup les variables CR/% pour chaque personnage

        for (Player player : playerList.values()) {
            double minRNG = 0.00;
            double maxRNG = 5.00;
            double random = new Random().nextDouble();
            player.setCRinPercentage((player.getSpeed() * 100 / tickValue)+(minRNG + (random * (maxRNG - minRNG)))); // initialise la valeur et facilite l'affichage
            player.setNumberOftick(100 / (player.getSpeed() / tickValue)); //
        }
    }

    public static void initBoss(){
        listE1.clear(); // clear the list of ennemies
        // boss has entered the chat
        Player p8 = new Wyvern("Wyvern",242,true,6835,1940,233578,50,150,100,80,5,2,1,0);

        // add the boss on the
        playerList.put(p8.getName(), p8);
        listE1.add(p8.getName());

        setCRBar(playerList); // permet de setup le paramètre tickvalue qui setup les variables CR/% pour chaque personnage

        for (Player player : playerList.values()) { // setup first turn
            double minRNG = 0.00;
            double maxRNG = 5.00;
            double random = new Random().nextDouble();
            player.setCRinPercentage((player.getSpeed() * 100 / tickValue)+(minRNG + (random * (maxRNG - minRNG)))); // initialise la valeur et facilite l'affichage
            player.setNumberOftick(100 / (player.getSpeed() / tickValue)); //
        }

        // reduce duration of debuffs by 1 turn

        for (Iterator<String> iterator = listA.iterator(); iterator.hasNext(); ) {
            String player = iterator.next();
            TempEffect i;
            // DEBUFFS

            Iterator<Map.Entry<Integer, TempEffect>> it = playerList.get(player).getDebuffsList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, TempEffect> pair = it.next();
                i = pair.getValue(); // i = TempEffect
                if (i != null) {
                    if (i.duration > 0) {
                        i.setDuration(i.getDuration() - 1); //
                        if (i.duration == 0) {
                            //i.resetEffects(activePlayer, activePlayer); // caster,currentTarget : current target is the one resetting the debuff
                            pair.setValue(null); // reset
                        }
                    }
                }
            }

            // BUFFS
            it = playerList.get(player).getBuffsList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, TempEffect> pair = it.next();
                i = pair.getValue(); // i = TempEffect
                if (i != null) {
                    if (i.duration > 0) {
                        i.setDuration(i.getDuration() - 1);
                        if (i.duration == 0) {
                            //i.resetEffects(activePlayer, activePlayer); // caster,currentTarget : current target is the one resetting the buff
                            pair.setValue(null); // reset
                        }
                    }
                }
            }
        }

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
    }

    public static void updateTempEffect(Player activePlayer){

        // DEBUFF
        TempEffect i;
        Iterator<Map.Entry<Integer, TempEffect>> it = activePlayer.getDebuffsList().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, TempEffect> pair = it.next();
            i = pair.getValue(); // i = TempEffect
            if (i != null) {
                if (i.duration > 0) {
                    i.setDuration(i.getDuration() - 1); //
                    if (i.duration == 0) {
                        //i.resetEffects(activePlayer, activePlayer); // caster,currentTarget : current target is the one resetting the debuff, ignore caster
                        pair.setValue(null); // reset
                    }
                }
            }
        }

        // BUFFS
        it = activePlayer.getBuffsList().entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, TempEffect> pair = it.next();
            i = pair.getValue(); // i = TempEffect
            if (i != null) {
                if (i.duration > 0) {
                    i.setDuration(i.getDuration() - 1);
                    if (i.duration == 0) {
                        //i.resetEffects(activePlayer, activePlayer); // caster,currentTarget : current target is the one resetting the debuff, ignore caster
                        pair.setValue(null); // reset
                    }
                }

            }
        }

    }

    public static void updateDeadStatus(Player activePlayer){
        if (activePlayer.getTeam() == 0) {
            // 0 = ally
            updateDeadList(listA);
        } else {
            // 1 = enemy
            updateDeadList(listE1);
        }
    }

    public static void updateTickStatus(Player activePlayer){
        for (Iterator<TempEffect> iterator = activePlayer.getTickDamageList().iterator(); iterator.hasNext(); ) {
            TempEffect effect = iterator.next();
            effect.applyEffects(effect.getCaster(), effect.getTarget());
            effect.reduceDuration(); // reduce duration by 1 turn
            if (effect.duration == 0) {
                iterator.remove(); // remove the debuff if duration = 0
            }
        }
    }

    public static Player getLowHP(ArrayList<String> list) { // return the monster with the lowest hp
        double perc = playerList.get(list.get(0)).getHealth() / playerList.get(list.get(0)).getMaxhp();
        Player pl = playerList.get(list.get(0));

        // check if everyone has same amount of hp
        boolean equalHP = true;
        for (String s : list) {
            if ((playerList.get(s).getHealth() / playerList.get(list.get(0)).getMaxhp() != perc) ){ // if not all equals to % hp
                equalHP = false;
            }
        }

        if (equalHP) {
            // if yes, pick a random enemy
            Random r = new Random();
            int randomInt = r.nextInt(listE1.size());
            return playerList.get(list.get(randomInt));
        } else { // return the enemy with the lowest HP
            for (int i = 1; i < list.size(); i++) {
                double p = playerList.get(list.get(i)).getHealth() / playerList.get(list.get(i)).getMaxhp();
                if (p < perc) { // we get the %
                    perc = p;
                    pl = playerList.get(list.get(i));
                }
            }
            return pl;
        }

    }

    public static Player getTarget(ArrayList<String> list){ // return the ally to target for monsters
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

    public static Player getWyvernTarget(ArrayList<String> listA, ArrayList<String> listE1, double tickvalue){
        if (firstTurn){ // case first turn
            if (tickvalue == playerList.get(listE1.get(0)).getSpeed() ){
                firstTurn = false;
                return front;
            } else {
                return getTarget(listA);
            }
        } else { // other normal turns
            int numberOfDebuffs = 0;
            // COUNT NUMBER OF TICK DAMAGE DEBUFF
            for (TempEffect element : playerList.get(listE1.get(0)).getTickDamageList()) {
                numberOfDebuffs++;
            }
            // COUNT NUMBER OF UNIQUE DEBUFF
            TempEffect i;
            Iterator<Map.Entry<Integer, TempEffect>> it = playerList.get(listE1.get(0)).getDebuffsList().entrySet().iterator();
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
                return front;
            } else {
                return getTarget(listA);
            }
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

    /* TODO : REMOVE
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

     */

}