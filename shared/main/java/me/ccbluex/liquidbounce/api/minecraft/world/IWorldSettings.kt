/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.world

interface IWorldSettings {

    enum class WGameType {
        NOT_SET,
        SURVIVAL,
        CREATIVE,
        ADVENTUR,
        SPECTATOR
    }

}