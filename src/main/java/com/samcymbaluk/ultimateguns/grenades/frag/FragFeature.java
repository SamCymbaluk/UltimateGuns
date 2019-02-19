package com.samcymbaluk.ultimateguns.grenades.frag;

import com.samcymbaluk.ultimateguns.features.PluginFeature;

public class FragFeature extends PluginFeature {

    public FragFeature() {
        new FragListener(this);
    }
}
