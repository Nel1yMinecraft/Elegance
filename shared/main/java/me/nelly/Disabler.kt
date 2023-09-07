package me.nelly

import me.ccbluex.liquidbounce.api.minecraft.network.IPacket
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.*
import net.minecraft.network.play.server.SPacketChangeGameState

@ModuleInfo(name = "Disabler", description = "Disable AntiCheat", category = ModuleCategory.EXPLOIT)
class Disabler : Module() {
    private val modeValue = ListValue("Mode", arrayOf("GrimPost", "GM2", "GM1"), "GrimPost")
    var lastC03 = false
    var lastIsTeleport = false

    override fun onEnable() {
        if (modeValue.get().contains("GrimPost")) {
            lastC03 = false
            lastIsTeleport = false
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        when (modeValue.get().toLowerCase()) {
            "grimpost" -> {
                if (packet is CPacketPlayer && !lastIsTeleport) {
                    lastC03 = true
                } else if (lastC03 && (packet is CPacketPlayerAbilities
                            || packet is CPacketUseEntity || packet is CPacketPlayerTryUseItemOnBlock
                            || packet is CPacketPlayerTryUseItem || packet is CPacketPlayerDigging
                            || packet is CPacketClickWindow || packet is CPacketAnimation || packet is CPacketEntityAction
                            )
                ) {
                    val connection = mc.unwrap().connection ?: return
                    connection.sendPacket(CPacketConfirmTransaction(1, 0, true))
                } else if (packet is CPacketConfirmTransaction) {
                    lastC03 = false
                }
                if (packet is CPacketConfirmTeleport) {
                    lastIsTeleport = true
                } else if (packet is CPacketPlayer.PositionRotation && lastIsTeleport) {
                    lastIsTeleport = false
                }
            }

            "gm2" -> {
                mc2.connection!!.networkManager.sendPacket(SPacketChangeGameState(3, 0f))
            }

            "gm1" -> {
                mc2.connection!!.networkManager.sendPacket(SPacketChangeGameState(8, 0f))

            }
        }
    }
}
