package com.wyvernrunner.wicket.simulator;

import java.util.*;

public class Game {

    public static double tickValue; // taille de la barre de CR (exemple : si 300 speed qui est le plus rapide alors tickValue = 300 ) //
    public static Player activePlayer;
    public static Map<String,Player> playerList = new HashMap<>();
    public static ArrayList<String> listA = new ArrayList<>(); // store names of the target lists of allies
    public static ArrayList<String> listE1 = new ArrayList<>(); // store names of the target lists of wave 1
    public static Player front;


    public static void main(String[] args) {

        long startTime = System.nanoTime(); // start

        // Initiate the game
        System.out.println("After");
        initGame(playerList);
        while(listE1.size() > 0 && listA.size() > 0){ // WAVE 1 stops whenever one list is empty
            // simule le déroulement des tours de jeu ( 1 tour = 1 personnage à 100% ) // boucler sur le nombre de joueurs vivants
            //displayCR(playerList)

            // Simulate every unit turn
            CRcounter(playerList); // print CR de chaque personnage
            // activePlayer est attribué

            // Get the active player
            // Todo : Coder les actions


            if (activePlayer instanceof Hero) { // hero attacking
                Player currentTarget = getLowHP(listE1);
                activePlayer.action(currentTarget); // if the current player is a hero, attacks a monster
                //System.out.println(activePlayer.getName() +" hit " + getLowHP(listE1).getName());
                updateDeadList(currentTarget, listE1); // update ennemy alives list
            } else { // monster attacking
                Player currentTarget = getTarget(listA); // find a target among allies
                activePlayer.action(playerList.get(currentTarget.getName()));
                //System.out.println(activePlayer.getName() +" hit " + target);
                updateDeadList(currentTarget, listA); // update allies alive list
            }

            /*System.out.println("------------------------------------- \n" +
                    "-------------------------------------"
            );*/
        }

        long endTime = System.nanoTime();
        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in nanoseconds  : " + timeElapsed);
        System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);

    }

    private static void updateDeadList(Player currentTarget, ArrayList<String> listE1) {
        if (playerList.get(currentTarget.getName()).getHealth() <= 0) { // if dead we update our monsterslist
            for (Iterator<String> iterator = listE1.iterator(); iterator.hasNext();) {
                String deadName = iterator.next();
                if (deadName.equals(currentTarget.getName())){
                    playerList.get(currentTarget.getName()).setAlive(false); // considers dead // USELESS?
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

        ////////////////////////////////////////
        //              DISPLAY               //
        ////////////////////////////////////////

        double jumpCRbar = tickValue; // compteur inverse de tick commençant à tickValue, et diminue vers 0. Saute de joueur de joueur

            // display CR
            // We store key = CR and value = String name so it's sorted ; easy to display
            TreeMap<Double,String> displayCRForPlayers = new TreeMap<>(Collections.reverseOrder()); // DESC order of CR

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

            // Prepare the the value of the next step
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

    public static void initGame(Map<String,Player> playerList) {
        // Hero
        Player p1 = new Hero("Krau", 200, true, 1400, 1500, 25000, 35, 160, 65, 120, 5);
        Player p2 = new Hero("Alexa", 197, true, 1663, 733, 7484, 87, 272, 54, 0, 5);
        Player p3 = new Hero("Chloe", 208, true, 2347, 660, 7189, 40, 233, 66, 59, 5);
        Player p4 = new Hero("SSB", 201, true, 2622, 871, 10186, 83, 260, 96, 14, 5);
        // Monster
        Player p5 = new Monster("Naga1", 154, true, 2222, 1340, 13358, 50, 150, 50, 45, 5); // LES 2 DEUX NAGA DOIVENT AVOIR DEUX SPEED DIFFÉRENTES
        Player p6 = new Monster("Naga2", 155, true, 2222, 1340, 13358, 50, 150, 50, 45, 5);
        Player p7 = new Monster("Dragona", 175, true, 3333, 1392, 20241, 50, 150, 50, 65, 5);

        // Add heroes on data
        playerList.put(p1.getName(), p1); // p1 is the front
        front = p1;
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
            int index = new Random().nextInt(list.size()-1); // generate [0,list.size]
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


}