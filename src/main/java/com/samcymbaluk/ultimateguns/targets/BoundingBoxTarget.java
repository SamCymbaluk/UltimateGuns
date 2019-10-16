package com.samcymbaluk.ultimateguns.targets;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public abstract class BoundingBoxTarget extends Target {

    @Override
    public RayTraceTargetResult isHit(Location start, Vector direction, double maxDistance) {
        BoundingBox bb = getBoundingBox();
        RayTraceResult rtResult = bb.rayTrace(start.toVector(), direction, maxDistance);
        return rtResult == null ? null : new RayTraceTargetResult(rtResult, this);
    }

    public abstract BoundingBox getBoundingBox();
}
