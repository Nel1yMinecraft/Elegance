/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package me.ccbluex.liquidbounce.utils.render;

import me.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import me.ccbluex.liquidbounce.utils.Translate;

public class PotionData {
    public final IPotion potion;
    public int maxTimer = 0;
    public float animationX = 0;
    public final Translate translate;
    public final int level;
    public PotionData(IPotion potion, Translate translate, int level) {
        this.potion = potion;
        this.translate = translate;
        this.level = level;
    }

    public float getAnimationX() {
        return animationX;
    }

    public IPotion getPotion() {
        return potion;
    }

    public int getMaxTimer() {
        return maxTimer;
    }
}