/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.player

import me.ccbluex.liquidbounce.api.MinecraftVersion
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "PotionSaver", description = "Freezes all potion effects while you are standing still.", category = ModuleCategory.PLAYER, supportedVersions = [MinecraftVersion.MC_1_8])
class PotionSaver : Module() {

    @EventTarget
    fun onPacket(e: PacketEvent) {
        val packet = e.packet

        if (classProvider.isCPacketPlayer(packet) && !classProvider.isCPacketPlayerPosition(packet) && !classProvider.isCPacketPlayerPosLook(packet) &&
                !classProvider.isCPacketPlayerPosLook(packet) && mc.thePlayer != null && !mc.thePlayer!!.isUsingItem)
            e.cancelEvent()
    }

}