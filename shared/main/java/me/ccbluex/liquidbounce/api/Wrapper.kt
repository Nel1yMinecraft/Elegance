/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api

import me.ccbluex.liquidbounce.api.minecraft.client.IMinecraft

interface Wrapper {
    val classProvider: IClassProvider
    val minecraft: IMinecraft
    val functions: IExtractedFunctions


}