package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class FragFeature extends PluginFeature {

    private FragListener fragListener;

    public FragFeature() {
        fragListener = new FragListener(this);
    }
}
