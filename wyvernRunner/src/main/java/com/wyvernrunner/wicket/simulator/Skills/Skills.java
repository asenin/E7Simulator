package com.wyvernrunner.wicket.simulator.Skills;

public class Skills {

    private double ratio;
    private double pow;
    private double enhanceMod;
    private double element;
    private double flatMod;

    public Skills(double ratio, double pow, double enhanceMod, double element){
        this.ratio = ratio;
        this.pow = pow;
        this.enhanceMod = enhanceMod;
        this.element = element;

    }


    public double getRatio(){
        return this.ratio;
    }

    public double getPow(){
        return this.pow;
    }

    public double getEnhanceMod(){
        return this.getEnhanceMod();
    }

    public double getElement() {
        return this.element;
    }

}
