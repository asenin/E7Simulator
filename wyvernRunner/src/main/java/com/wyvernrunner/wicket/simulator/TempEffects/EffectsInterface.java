package com.wyvernrunner.wicket.simulator.TempEffects;

import com.wyvernrunner.wicket.simulator.Player;

public interface EffectsInterface {

    public void applyEffects(Player player);

    public void updateEffect(int dur);

    public void resetEffect(Player player);
}
