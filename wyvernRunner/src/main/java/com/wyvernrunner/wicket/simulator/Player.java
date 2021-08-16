package com.wyvernrunner.wicket.simulator;

import com.wyvernrunner.wicket.simulator.Main;
import com.wyvernrunner.wicket.simulator.TempEffects.DecreaseDefense;
import com.wyvernrunner.wicket.simulator.TempEffects.TempEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Player {


    /*Données:
    toutes les stats
            artefact

    skillups
    */
    ////////////////////////////

    private double speed;
    private String name;
    private boolean alive;
    private double CRinPercentage;
    private double numberOftick;

    ////////////////////////////

    private double attack;
    private double defense;
    private double health;
    private final double maxhp = health;
    private float cc ;
    private int cdmg;
    private int eff;
    private int effres;
    private int dual;
    private final int element;
    private double shield;


    ////////////////////////////
    // TODO : DELETE THIS SECTION AND KEEP BELOW ONCE EVERYTHING IS CLEANED

    private HashMap<Integer,Integer> buffList = new HashMap<>(); // check #glossary for every buff ( key : debuff, value = nb turns)
    private HashMap<Integer,Integer> debuffList = new HashMap<>(); // check #glossary for every debuff ( key : debuff, value = nb turns)
    private HashMap<String,Integer> skillsCooldown = new HashMap<>(); // skills cooldown

    ////////////////////////////
    // TODO : KEEP THIS SECTION

    private Map<Integer, TempEffect> DebuffsList = new HashMap<>(); // unique debuffs
    private ArrayList<TempEffect> TickDamageList = new ArrayList<>(); // for burns / poisons / bombs
    private Map<Integer, TempEffect> BuffsList = new HashMap<>(); // unique debuffs


    public Player(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual,int element) {
        this.speed = speed;
        this.name = name;
        this.alive = alive;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.cc = cc;
        this.cdmg = cdmg;
        this.eff = eff;
        this.effres = effres;
        this.dual = dual;
        this.element = element;
        this.shield= 0;


        // TODO : REPLACE WITH REMOVING ALL BUFFS/DEBUFFS AT DEATH
        buffList.replaceAll((k,v)->v=0); // reset all buffs
        debuffList.replaceAll((k,v)->v=0); // reset all debuffs

        /**********************************************************
         *               TempEffect lists initiation              *
         **********************************************************/

        for (int i = 1 ; i < 44 ; i = i + 2 ) { // 43 is curse debuff from banshee for now
            DebuffsList.put(i,null);
        }

        for (int i = 2; i < 42 ; i = i + 2 ) { // buffs start from 2 = atk buff
            BuffsList.put(i,null);
        }

    }

    public void playerPlayed() {
        System.out.println(name + " played !"); // affichage console preuve d'avoir joué
    }

    public void action(Player player) {
        player.setHealth(player.getHealth() - (getAttack()+ 0 )*1*1*(1.871*1)/(player.getDefense()/300+1)*1); // flat modifier / multi / element / pow / hitType)
        if (player.getHealth() <= 0) {
            player.setAlive(false); // set it to dead
        }
    }


    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    ////////////////// Getters and Setters ///////////////////
    //////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getCRinPercentage() {
        return CRinPercentage;
    }

    public void setCRinPercentage(double CRinPercentage) {
        this.CRinPercentage = CRinPercentage;
    }

    public double getNumberOftick() {
        return numberOftick;
    }

    public void setNumberOftick(double numberOftick) {
        this.numberOftick = numberOftick;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxhp() {
        return maxhp;
    }

    public float getCc() {
        return cc;
    }

    public void setCc(float cc) {
        this.cc = cc;
    }

    public int getCdmg() {
        return cdmg;
    }

    public void setCdmg(int cdmg) {
        this.cdmg = cdmg;
    }

    public int getEff() {
        return eff;
    }

    public void setEff(int eff) {
        this.eff = eff;
    }

    public int getEffres() {
        return effres;
    }

    public void setEffres(int effres) {
        this.effres = effres;
    }

    public int getDual() {
        return dual;
    }

    public void setDual(int dual) {
        this.dual = dual;
    }

    public int getElement(){
        return this.element;
    }

    public double getShield() { return this.shield; }

    public void setShield(double shield) { this.shield = shield; }

    public ArrayList<TempEffect> getTickDamageList() {
        return TickDamageList;
    }

    public Map<Integer,TempEffect> getBuffsList() {
        return BuffsList;
    }

    public Map<Integer, TempEffect> getDebuffsList() {
        return DebuffsList;
    }

    public void skillAI (int damageShare, int enemyDefense, int damageReduction, Map<String, ArrayList<TempEffect>> buffs, Map<String , Player> liste){
    }


}
