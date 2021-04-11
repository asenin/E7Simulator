package com.wyvernrunner.wicket.simulator;

import java.util.*;

public class Main {

    public static double tickValue; // taille de la barre de CR (exemple : si 300 speed qui est le plus rapide alors tickValue = 300 ) //
    public static Player activePlayer;
    public static HashMap<String,Player> playerList = new HashMap<>();
    public static ArrayList<String> listA = new ArrayList<>(); // store names of the target lists of allies
    public static ArrayList<String> listE1 = new ArrayList<>(); // store names of the target lists of wave 1
    public static String front;

    // public static TreeMap<Double,String> perNames = new TreeMap<Double, String>();

    public interface action {
         void action(Player player);
    }



    public static void main(String[] args) {

        long startTime = System.nanoTime(); // start

        // Initiate the game
        System.out.println("jemarche");
        initGame(playerList);
        while(listE1.size() > 0 && listA.size() > 0){ // WAVE 1
            // simule le déroulement des tours de jeu ( 1 tour = 1 personnage à 100% ) // boucler sur le nombre de joueurs vivants
            //displayCR(playerList)

            // Simulate every unit turn
            CRcounter(playerList); // print CR de chaque personnage
            // activePlayer est attribué

            // Get the active player
            // Todo : Coder les actions

            if (activePlayer instanceof Hero) { // hero attacking
                activePlayer.action(getLowHP(listE1)); // if the current player is a hero, attacks a monsterHero attacks one monster
                //System.out.println(activePlayer.getName() +" hit " + getLowHP(listE1).getName());
                if (playerList.get(getLowHP(listE1).getName()).getHealth() <= 0) { // if dead we update our playerslist
                    for (int i = 0 ; i < listE1.size() ; i++) { // search the target in listE to remove it
                        if (listE1.get(i).equals(getLowHP(listE1).getName())){
                            playerList.get(getLowHP(listE1).getName()).setAlive(false); // considers dead
                            listE1.remove(i); // remove the monster from listE1
                        }
                    }
                }
            } else { // monster attacking
                String target = getTarget(listA);
                activePlayer.action(playerList.get(target));
                //System.out.println(activePlayer.getName() +" hit " + target);
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

    public static String getTarget(ArrayList<String> list){
        double randNumber = Math.random();
        randNumber = randNumber * 100;
        if (playerList.get(front).getAlive()){ // if he is alive
            if (list.size() > 2){ // case front is 40% and rest is same probability
                if (randNumber < 40){ // target the front at 40%
                    return front;
                } else { // target the dpses
                    int index = new Random().nextInt(list.size()-1); // generate [0,list.size]
                    return playerList.get(list.get(index)).getName(); // get the player with the name
                }
            } else {
                if (randNumber > 60){
                    return front; // get the front
                } else {
                    return list.get(0); // get the other survivor
                }
            }
        } else { // front is dead everyone has the same chance to get targeted
            int index = new Random().nextInt(list.size()-1); // generate [0,list.size]
            return playerList.get(list.get(index)).getName(); // get the player with the name
        }
    }


    public static void initGame(HashMap<String,Player> playerList){
        // Hero
        Player p1 = new Hero("Krau",200,true,1400,1500,25000,35,160,65,120,5);
        Player p2 = new Hero("Alexa",197,true,1663,733,7484,87,272,54,0,5);
        Player p3 = new Hero("Chloe",208,true,2347,660,7189,40,233,66,59,5);
        Player p4 = new Hero("SSB",201,true,2622,871,10186,83,260,96,14,5);
        // Monster
        Player p5 = new Monster("Naga1",154,true,2222, 1340,13358,50,150,50,45,5); // LES 2 DEUX NAGA DOIVENT AVOIR DEUX SPEED DIFFÉRENTES
        Player p6 = new Monster("Naga2",155,true,2222,1340,13358,50,150,50,45,5);
        Player p7 = new Monster("Dragona",175,true,3333,1392,20241,50,150,50,65,5);

        // Add heroes on data
        playerList.put(p1.getName(),p1); // p1 is the front
        front = p1.getName();
        listA.add(p1.getName());
        playerList.put(p2.getName(),p2);
        listA.add(p2.getName());
        playerList.put(p3.getName(),p3);
        listA.add(p3.getName());
        playerList.put(p4.getName(),p4);
        listA.add(p4.getName());


        // Add monsters on data
        playerList.put(p5.getName(),p5);
        listE1.add(p5.getName());
        //perNames.put( 100.00,"Naga1");
        playerList.put(p6.getName(),p6);
        listE1.add(p6.getName());
        //perNames.put( 100.00,"Naga2");
        playerList.put(p7.getName(),p7);
        listE1.add(p7.getName());
        //perNames.put( 100.00,"Dragona");

        //speedRNG(playerList); // applique la speed RNG
        setCRBar(playerList); // permet de setup les variables CR/% pour chaque personnage

        for (Player player : playerList.values()) {
            player.setCRinPercentage(player.getSpeed()*100/tickValue) ; // initialise la valeur et facilite l'affichage
            player.setNumberOftick(100/(player.getSpeed()/tickValue)); //
        }
    }

    /*
    public static void speedRNG(ArrayList<Player> playerList) {
        for (Player player : playerList) { // Initialise le CR et détermine le nombre de tick à diminuer pour que le prochain personnage joue
            Random rand = new Random();
            double random = player.speed + Math.random() * (player.speed*1.05 - player.speed);
            player.speed = player.speed*random;
        }
    }*/


    public static void setCRBar(HashMap<String,Player> playerList) {
        ArrayList<Double> sortedCRList = new ArrayList<>(); // arraylist de speed
        //sortedCRList.add(243); // speed wyvern

        playerList.forEach((key, value) -> { // Sort the list of speeds to get the fastest unit
            sortedCRList.add(value.getSpeed());
        });

       Collections.sort(sortedCRList);
        tickValue = sortedCRList.get(playerList.size()-1); // Construction de tickValue en fonction de la speed du personnage le plus rapide
    }

    public static void CRcounter(HashMap<String,Player> playerList) { // calcule et update le CR de tout le monde pour le tour du joueur en cours
        // Affichage t1
        double jumpCRbar = tickValue; // compteur inverse de tick commençant à tickValue, et diminue vers 0. Saute de joueur de joueur

        // display CR
        ArrayList<Double> sortedCRList = new ArrayList<>();

        playerList.forEach((key, value) -> {
            sortedCRList.add(value.getCRinPercentage());
        });

        Collections.sort(sortedCRList);
        for (int i = sortedCRList.size(); i-- > 0;) {
            for (Map.Entry player : playerList.entrySet()) {
                Player pl = (Player) player.getValue();
                if (pl.getCRinPercentage() == sortedCRList.get(i)) {
                    //if (sortedCRList.get(i) == 100) {
                        //System.out.println(pl.getName() + "'s turn !");
                        //System.out.println("-------------------------------------");
                        //activePlayer = pl;
                    //}

                    System.out.println(pl.getName() + " has a CR of : " + String.format("%.2f", sortedCRList.get(i)) +"%");
                    if (pl.getCRinPercentage() == 100) {
                        pl.setCRinPercentage(0);
                        activePlayer = pl;
                    }
                }
            }
        }

        // on se prépare à update pour le t2
        for (Map.Entry player : playerList.entrySet()) {
            Player pl = (Player) player.getValue();
            pl.setNumberOftick((100 - pl.getCRinPercentage()) / (pl.getSpeed()/tickValue)) ; // nombre de ticks à réaliser pour atteindre 100%
            if (pl.getNumberOftick() < jumpCRbar){
                jumpCRbar = pl.getNumberOftick();
            }
        }

        for (Map.Entry player : playerList.entrySet()) { // Sort the list of speeds to get the fastest unit
            Player pl = (Player) player.getValue();
            pl.setNumberOftick(pl.getNumberOftick() - jumpCRbar); // actualise la CR bar de tout le monde en décalant tout le monde linéairement
            // du nombre de tick du joueur qui atteint la barre 0 en premier
            pl.setCRinPercentage(100*(1-pl.getNumberOftick()/ (tickValue*100/pl.getSpeed()))); // current CR = nb_ticks/total_ticks
        }
        /*
        System.out.println("------------------------------------- \n" +
                "-------------------------------------"
        );*/
    }
}
