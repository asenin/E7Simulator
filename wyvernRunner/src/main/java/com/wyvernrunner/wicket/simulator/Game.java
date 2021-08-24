package com.wyvernrunner.wicket.simulator;

import com.wyvernrunner.wicket.simulator.Heroes.*;
import com.wyvernrunner.wicket.simulator.Monsters_W13.Dragona;
import com.wyvernrunner.wicket.simulator.Monsters_W13.Naga;
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


    public Game(){
        // default constructor
    }


    public static void main(String[] args) {
        long startTime = System.nanoTime(); // start



        /**********************************************************
         *                     WAVE 1 START                       *
         **********************************************************/

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
             *              CHECK ALLY OR ENEMY TURN                  *
             **********************************************************/

            if(activePlayer.getTeam() == 0){ // 0 = ally
                updateDeadList(listA);
            } else { // 1 = enemy
                updateDeadList(listE1);
            }

            /**********************************************************
             *         CHECK TICK DEBUFF BEFORE TURN STARTS           *
             **********************************************************/

            for (Iterator<TempEffect> iterator = activePlayer.getTickDamageList().iterator(); iterator.hasNext();) {
                TempEffect effect = iterator.next();
                effect.applyEffects(effect.getCaster(),effect.getTarget());
                effect.reduceDuration(); // reduce duration by 1 turn
                if (effect.duration == 0){
                    iterator.remove(); // remove the debuff if duration = 0
                }
            }


            /**********************************************************
             *         CHECK IF PLAYER IS ALIVE AFTER TICKS           *
             **********************************************************/

            if (activePlayer.getAlive()) { // if he is alive after the debuffs applied on him (poison, burn, etc...)
                //System.out.println(activePlayer.getName() + "'s turn !");

                /**********************************************************
                 *                       ALLY TURN                        *
                 **********************************************************/

                if (activePlayer.getTeam() == 0 ) { // hero attacking
                    Player currentTarget = getLowHP(listE1);
                    activePlayer.skillAI(currentTarget,playerList,tickValue,listA,listE1); // if the current player is a hero, attacks a monster


                    updateDeadList(listE1); // update enemy alive list



                /**********************************************************
                 *                      ENEMY TURN                        *
                 **********************************************************/

                } else { // monster attacking
                    Player currentTarget = getTarget(listA); // find a target among allies
                    activePlayer.skillAI(currentTarget,playerList,tickValue,listA,listE1); // if the current player is a monster, attacks a hero

                    if (currentTarget instanceof GeneralPurrgis) {
                        for (String player : listA) {
                            Player pl = playerList.get(player);
                            pl.setNumberOftick(Math.max(0, pl.getNumberOftick() - 0.16 * tickValue)); // actualise la CR bar de tout le monde en décalant tout le monde
                            // avec le push de GPurrgis
                            pl.setCRinPercentage(100 * (1 - pl.getNumberOftick() / (tickValue * 100 / pl.getSpeed())));
                        }
                    }

                    if (currentTarget instanceof SeasideBellona) {
                        // depending on the stacks
                        if (((SeasideBellona) currentTarget).getS2stacks() > 4) { // If the target was SeasideBellona, we check the nb of stacks
                            ((SeasideBellona) currentTarget).skill2(listE1,playerList); // Use S2
                            ((SeasideBellona) currentTarget).setS2stacks(0); // reset stacks
                        }
                    }

                    updateDeadList(listA); // update allies alive list
                }
            }

            /**********************************************************
             *         REDUCE DURATION OF BUFFS & DEBUFFS             *
             **********************************************************/

            // DEBUFFS
            TempEffect i;
            Iterator<Map.Entry<Integer, TempEffect>> it = activePlayer.getDebuffsList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, TempEffect> pair = it.next();
                i = pair.getValue(); // i = TempEffect
                if (i != null) {
                    if (i.duration > 0) {
                        i.setDuration(i.getDuration()-1); //
                        if (i.duration == 0) {
                            i.resetEffects(activePlayer,activePlayer); // caster,currentTarget : current target is the one resetting the debuff, ignore waster
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
                        i.setDuration(i.getDuration()-1);
                    }
                }
            }


            /**********************************************************
             *                     END OF A TURN                      *
             **********************************************************/


        }

        long endTime = System.nanoTime();
        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in nanoseconds  : " + timeElapsed);
        System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);

        /*
        for (Map.Entry player : playerList.entrySet()) {
            Player pl = (Player) player.getValue();
            System.out.println(pl.getName() + " has " + pl.getHealth() + " HP");
        }
        */
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

    private static void updateDeadList(ArrayList<String> listE1) {
        for (Iterator<String> iterator = listE1.iterator(); iterator.hasNext(); ) {
            String deadName = iterator.next();
            if (playerList.get(deadName).getHealth() <= 0) {
                playerList.get(deadName).setAlive(false); // considers dead /
                // System.out.println(deadName + " is dead ");
                iterator.remove(); // remove the monster from listE1
                deadplayerList.put(deadName,playerList.get(deadName));
                playerList.remove(deadName);
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
        Player p2 = new Alexa("Alexa", 116, true, 2690, 812, 7400, 89, 306, 82, 9, 5,1, 0,0);
        Player p3 = new Luluca("Luluca", 204, true, 1786, 1056, 6237, 64, 277, 88, 14, 13,1, 0,0);
        Player p4 = new SeasideBellona("SeasideBellona", 126, true, 3426, 1126, 13839, 94, 308, 63, 0, 5,1, 0,0);


        // Monster
        Player p5 = new Dragona("Dragona1", 154, true, 5294, 1392, 20241, 50, 150, 0, 0, 5,2,1,0);
        Player p6 = new Dragona("Dragona2", 154, true, 5294, 1392, 20241, 50, 150, 0, 0, 5,2,1,0);
        Player p7 = new Naga("Naga", 175, true, 2800, 1340, 13354, 50, 150, 0, 0, 5,2,1,0);
        //Player p8 = new Monster("Wyvern",242,true,6835,1940,233578,50,150,0,80,5,2);


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

    // naga skill 2 ratio = 1.3
    // draguna skill 2 ratio = 1.4


    //((this.getAtk(skillId)*rate + flatMod)*dmgConst + flatMod2) * pow * skillEnhance * elemAdv * target * dmgMod;
    // ((1-dmgReduc)*(1-dmgTrans))/(((this.def / 300)*this.getPenetration(skill)) + 1);























}