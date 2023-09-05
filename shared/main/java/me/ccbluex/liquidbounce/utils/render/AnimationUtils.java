/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.utils.render;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class AnimationUtils {
    // Skid for LiquidBounce-Plus
    public static double animate(double target, double current, double speed) {
        if (current == target) return current;

        boolean larger = target > current;
        if (speed < 0.0D) {
            speed = 0.0D;
        } else if (speed > 1.0D) {
            speed = 1.0D;
        }

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1D) {
            factor = 0.1D;
        }

        if (larger) {
            current += factor;
            if (current >= target) current = target;
        } else if (target < current) {
            current -= factor;
            if (current <= target) current = target;
        }

        return current;
    }
    public static float changer(float current, float add, float min, float max) {
        current += add;
        if (current > max) {
            current = max;
        }
        if (current < min) {
            current = min;
        }

        return current;
    }
    /**
     * In-out-easing function
     * https://github.com/jesusgollonet/processing-penner-easing
     *
     * @param t Current iteration
     * @param d Total iterations
     * @return Eased value
     */
    public static float easeOut(float t, float d) {
        return (t = t / d - 1) * t * t + 1;
    }

    /**
     * Source: https://easings.net/#easeOutElastic
     *
     * @return A value larger than 0
     */
    public static float easeOutElastic(float x) {
        double c4 = (2 * Math.PI) / 3.0f;

        return x == 0
                ? 0
                : (float) (x == 1
                ? 1
                : pow(2, -10 * x) * sin((x * 10 - 0.75) * c4) + 1);

    }
}
