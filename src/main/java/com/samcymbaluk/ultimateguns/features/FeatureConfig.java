package com.samcymbaluk.ultimateguns.features;

import java.util.Collections;
import java.util.List;

public class FeatureConfig {

    private List<String> enabledWorlds = Collections.singletonList("*");

    public List<String> getEnabledWorlds() {
        return enabledWorlds;
    }

}
