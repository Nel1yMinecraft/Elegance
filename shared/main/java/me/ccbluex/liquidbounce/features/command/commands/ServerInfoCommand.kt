/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.command.commands

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.Listenable
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.command.Command

class ServerInfoCommand : Command("serverinfo"), Listenable {
    init {
        LiquidBounce.eventManager.registerListener(this)
    }

    private var ip = ""
    private var port = 0

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (mc.currentServerData == null) {
            chat("This command does not work in single player.")
            return
        }

        val data = mc.currentServerData ?: return

        chat("Server infos:")
        chat("§7Name: §8${data.serverName}")
        chat("§7IP: §8$ip:$port")
        chat("§7Players: §8${data.populationInfo}")
        chat("§7MOTD: §8${data.serverMOTD}")
        chat("§7ServerVersion: §8${data.gameVersion}")
        chat("§7ProtocolVersion: §8${data.version}")
        chat("§7Ping: §8${data.pingToServer}")
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (classProvider.isCPacketHandshake(packet)) {
            val handshake = packet.asCPacketHandshake()

            ip = handshake.ip
            port = handshake.port
        }
    }

    override fun handleEvents() = true
}