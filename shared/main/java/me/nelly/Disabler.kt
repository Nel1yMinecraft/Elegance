package me.nelly

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketPlayer


@ModuleInfo(name = "Disabler", description = "Disable AntiCheat", category = ModuleCategory.MOVEMENT)
class Disabler : Module() {
    private val modeValue = ListValue("Mode", arrayOf("GrimPost", ""), "GrimPost")
    val time = MSTimer()

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        when (modeValue.get().toLowerCase()) {
            "grimpost" -> {
                val serverData = Minecraft.getMinecraft().currentServerData
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