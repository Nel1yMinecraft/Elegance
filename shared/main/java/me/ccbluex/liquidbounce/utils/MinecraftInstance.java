/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.utils;

import me.ccbluex.liquidbounce.LiquidBounce;
import me.ccbluex.liquidbounce.api.IClassProvider;
import me.ccbluex.liquidbounce.api.IExtractedFunctions;
import me.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.minecraft.client.Minecraft;

public class MinecraftInstance {
    public static final IMinecraft mc = LiquidBounce.wrapper.getMinecraft();
    public static final Minecraft mc2 = Minecraft.getMinecraft();
    public static final IClassProvider classProvider = LiquidBounce.INSTANCE.getWrapper().getClassProvider();
    public static final IExtractedFunctions functions = LiquidBounce.INSTANCE.getWrapper().getFunctions();
}
