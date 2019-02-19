package com.samcymbaluk.ultimateguns.util;

import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public interface ProjectileCallback {

    Vector handleImpact(RayTraceResult impact, Vector path);

    void handleStep(Location start, Vector path);
}
