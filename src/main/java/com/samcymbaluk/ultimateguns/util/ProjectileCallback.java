package com.samcymbaluk.ultimateguns.util;

import com.samcymbaluk.ultimateguns.targets.RayTraceTargetResult;
import com.samcymbaluk.ultimateguns.targets.Target;
import jdk.internal.net.http.common.Pair;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public interface ProjectileCallback {

    /**
     *
     * @param impact The impact information
     * @param path The direction and magnitude (velocity) of the projectile when the impact occurred
     * @param distance The total distance travelled by the projectile up until the impact
     * @param velocity The velocity of the projectile at the time of impact
     * @return A vector representing the magnitude (velocity) and direction the projectile should now have
     */
    Vector handleImpact(RayTraceTargetResult impact, Vector path, double distance, double velocity);

    /**
     *
     * @param start The position of the projectile at the start of this step
     * @param path The path the projectile took this step
     * @param velocity The velocity the projectile was travelling during this step
     */
    void handleStep(Location start, Vector path, double velocity);

    void done(Location end);
}
