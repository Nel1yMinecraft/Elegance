/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package me.ccbluex.liquidbounce.utils.render;

import me.ccbluex.liquidbounce.api.enums.EnumFacingType;
import me.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import me.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;

/**
 * values() will cause performance issues, so we store them in a static array.
 * We use ASM to replace values() with our own array. [me.ccbluex.liquidbounce.injection.transformers.OptimizeTransformer]
 * https://stackoverflow.com/questions/2446135/is-there-a-performance-hit-when-using-enum-values-vs-string-arrays
 * <p>
 * in my tests, this is 10 times faster than using values()
 * I access them 1145141919 times and save EnumFacing.name into a local variable in my test
 * EnumFacings.values() cost 122 ms
 * StaticStorage.facings() cost 15 ms
 *
 * @author liulihaocai
 */
public class StaticStorage {

    private static final EnumFacingType[] facings = EnumFacingType.values();
    private static final WEnumChatFormatting[] chatFormatting = WEnumChatFormatting.values();
    private static final EnumParticleTypes[] particleTypes = EnumParticleTypes.values();
    public static IScaledResolution scaledResolution;

    public static EnumFacingType[] facings() {
        return facings;
    }

    public static WEnumChatFormatting[] chatFormatting() {
        return chatFormatting;
    }

    public static EnumParticleTypes[] particleTypes() {
        return particleTypes;
    }


}
