package me.nelly

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.ClientUtils
import net.minecraft.network.play.client.CPacketTabComplete

@ModuleInfo(name = "HytPlugin",  description = "get hyt plugins", category = ModuleCategory.MISC)
class HytPlugin : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val message: String = "/ver"
        val command = message.substring(5)
        val packet = CPacketTabComplete(command, mc2.player.position, false)
        mc2.player.connection.sendPacket(packet)
        ClientUtils.displayChatMessage(command)
    }
}

