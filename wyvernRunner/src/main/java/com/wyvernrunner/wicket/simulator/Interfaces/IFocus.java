package com.wyvernrunner.wicket.simulator.Interfaces;

import com.wyvernrunner.wicket.simulator.Player;

import java.util.ArrayList;
import java.util.Map;

public interface IFocus {

    int getFocus();

    void setFocus(int focus);

    void triggerFocus(Player currentTarget, Map<String, Player> playerList, double tickValue, ArrayList<String> listA, ArrayList<String> listE1);
}
