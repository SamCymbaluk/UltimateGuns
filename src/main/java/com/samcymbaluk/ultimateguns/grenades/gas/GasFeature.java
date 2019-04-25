package com.samcymbaluk.ultimateguns.grenades.gas;

import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class GasFeature extends PluginFeature {

    private GasManager gasManager;
    private GasListener gasListener;

    public GasFeature() {
        this.gasManager = new GasManager();
        this.gasManager.start();

        this.gasListener = new GasListener(this, this.gasManager);
    }
}
