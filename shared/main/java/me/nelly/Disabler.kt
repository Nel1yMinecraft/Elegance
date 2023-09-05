package me.nelly

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketPlayer


@ModuleInfo(name = "Disabler", description = "Disable AntiCheat", category = ModuleCategory.EXPLOIT)
class Disabler : Module() {
    private val modeValue = ListValue("Mode", arrayOf("GrimPost", ""), "GrimPost")
    val time = MSTimer()

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        when (modeValue.get().toLowerCase()) {
            "grimpost" -> {
                val serverData = mc.currentServerData
                if (serverData != null) {
                    val pingTime = serverData.pingToServer
                    if (packet is CPacketPlayer)
                        time.hasTimePassed(pingTime)
                    PacketUtils.send(CPacketConfirmTransaction())
                }
            }
        }
    }
}