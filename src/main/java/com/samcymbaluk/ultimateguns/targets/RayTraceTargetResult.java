package com.samcymbaluk.ultimateguns.targets;

import org.bukkit.util.RayTraceResult;

public class RayTraceTargetResult {

    private RayTraceResult rayTraceResult;
    private Target target;

    public RayTraceTargetResult(RayTraceResult rayTraceResult, Target target) {
        this.rayTraceResult = rayTraceResult;
        this.target = target;
    }

    public RayTraceResult getRayTraceResult() {
        return rayTraceResult;
    }

    public Target getTarget() {
        return target;
    }
}
