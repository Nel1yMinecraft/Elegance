/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.special

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.Listenable
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.utils.MinecraftInstance
import java.util.*

class BungeeCordSpoof : MinecraftInstance(), Listenable {
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (classProvider.isCPacketHandshake(packet) && enabled && packet.asCPacketHandshake().requestedState.isHandshake) {
            val handshake = packet.asCPacketHandshake()

            handshake.ip = handshake.ip + "\u0000" + String.format("%d.%d.%d.%d", getRandomIpPart(), getRandomIpPart(), getRandomIpPart(), getRandomIpPart()) + "\u0000" + mc.session.playerId.replace("-", "")
        }
    }

    private fun getRandomIpPart(): String = RANDOM.nextInt(256).toString()

    override fun handleEvents(): Boolean {
        return true
    }

    companion object {
        private val RANDOM = Random()

        @JvmField
        var enabled = false
    }
}