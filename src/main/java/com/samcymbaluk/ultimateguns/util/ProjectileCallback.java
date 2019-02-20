package com.samcymbaluk.ultimateguns.util;

import com.samcymbaluk.ultimateguns.targets.Target;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public interface ProjectileCallback {

    /**
     *
     * @param impact The impact information
     * @param target The impacted target information
     * @param path The magnitude (velocity) and direction of the projectile when the impact occurred
     * @return A vector representing the magnitude (velocity) and direction the projectile should now have
     */
    Vector handleImpact(RayTraceResult impact, Target target, Vector path);

    /**
     *
     * @param start The position of the projectile at the start of this step
     * @param path The path the projectile took this step
     */
    void handleStep(Location start, Vector path);

    void done(Location end);
}
