/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.network.play.client

import me.ccbluex.liquidbounce.api.minecraft.network.IPacket

interface ICPacketResourcePackStatus : IPacket {
    enum class WAction {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;
    }

}