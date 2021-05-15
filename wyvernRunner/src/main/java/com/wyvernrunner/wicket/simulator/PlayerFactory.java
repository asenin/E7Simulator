package com.wyvernrunner.wicket.simulator;

public class PlayerFactory {

    public Player create(String name) {
        switch (name) {
            case "SSB":
                return new Hero("SSB", 201, true, 2622, 871, 10186, 83, 260, 96, 14, 5);
            case "Chloe":
                return new Hero("Chloe", 208, true, 2347, 660, 7189, 40, 233, 66, 59, 5);
            case "Krau":
                return new Hero("Krau", 200, true, 1400, 1500, 25000, 35, 160, 65, 120, 5);
            case "Alexa":
                return new Hero("Alexa", 197, true, 1663, 733, 7484, 87, 272, 54, 0, 5);
        }
        return null;
    }
}
