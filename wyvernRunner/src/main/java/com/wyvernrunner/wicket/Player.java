package com.wyvernrunner.wicket;

import java.util.HashMap;
import java.util.Map;

public abstract class Player implements Main.action{


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
    private boolean front;

    ////////////////////////////

    public Player(String name, double speed, boolean alive, double attack, double defense, double health, float cc, int cdmg, int eff, int effres, int dual) {
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
    }

    public void playerPlayed() {
        System.out.println(name + " played !"); // affichage console preuve d'avoir joué
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

    public boolean getFront(){
        return front;
    }

}
