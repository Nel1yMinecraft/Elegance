/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.IClassProvider
import me.ccbluex.liquidbounce.api.IExtractedFunctions
import me.ccbluex.liquidbounce.api.Wrapper
import me.ccbluex.liquidbounce.api.minecraft.client.IMinecraft
import net.minecraft.client.Minecraft

object WrapperImpl : Wrapper {
    override val classProvider: IClassProvider = ClassProviderImpl
    override val minecraft: IMinecraft
        get() = MinecraftImpl(Minecraft.getMinecraft())
    override val functions: IExtractedFunctions = ExtractedFunctionsImpl
}