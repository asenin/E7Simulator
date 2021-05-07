package com.wyvernrunner.wicket.simulator;

public class PlayerFactory {

    public Player create(String name) {
        if (name.equals("SSB")) {
            return new Hero("SSB", 201, true, 2622, 871, 10186, 83, 260, 96, 14, 5);
        } else {
            return new Hero("Chloe", 208, true, 2347, 660, 7189, 40, 233, 66, 59, 5);

        }
    }
}
