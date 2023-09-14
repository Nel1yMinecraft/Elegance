package me.nelly.fdp.clientspoof

import io.netty.buffer.Unpooled
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.Listenable
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.MinecraftInstance
import me.ccbluex.liquidbounce.utils.render.ColorUtils
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.CPacketCustomPayload

class ClientSpoofer : MinecraftInstance(), Listenable {

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        val clientSpoof = LiquidBounce.moduleManager.getModule(ClientSpoof::class.java) as ClientSpoof

        if (enabled && !mc2.isIntegratedServerRunning) {
            try {
                if (packet is CPacketCustomPayload) {
                    val payload = PacketBuffer(Unpooled.buffer())
                    val payloadData = when (clientSpoof.modeValue.get()) {
                        "Vanilla" -> "vanilla"
                        "LabyMod" -> "LMC"
                        "Germ" -> "Forge-germmod"
                        "Rise" -> "Rise"
                        "CheatBreaker" -> "CB"
                        "Lunar" -> "Lunar-Client"
                        "PvPLounge" -> "PLC18"
                        "Custom" -> ColorUtils.translateAlternateColorCodes(clientSpoof.customClientName.get())
                        else -> null
                    }
                    payloadData?.let { payload.writeString(it); packet.data = payload }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun handleEvents(): Boolean {
        return true
    }

    companion object {
        const val enabled = true
    }
}
