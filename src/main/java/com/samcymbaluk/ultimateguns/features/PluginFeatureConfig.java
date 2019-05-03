package com.samcymbaluk.ultimateguns.features;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class PluginFeatureConfig {

    private Set<String> enabledWorlds = new HashSet<>(Collections.singletonList("*"));

    public Set<String> getEnabledWorlds() {
        return enabledWorlds;
    }
}
