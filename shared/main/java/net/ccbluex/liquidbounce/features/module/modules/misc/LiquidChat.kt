/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting
import net.ccbluex.liquidbounce.chat.Client
import net.ccbluex.liquidbounce.chat.packet.packets.*
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.SessionEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue


@ModuleInfo(name = "LiquidChat", description = "Allows you to chat with other LiquidBounce users.", category = ModuleCategory.MISC)
class LiquidChat : Module() {

    val jwtValue = object : BoolValue("JWT", false) {
        override fun onChanged(oldValue: Boolean, newValue: Boolean) {
        }
    }

    companion object {
        var jwtToken = ""
    }

    val client = object : Client() {

        /**
         * Handle connect to web socket
         */
        override fun onConnect() {
        }

        /**
         * Handle connect to web socket
         */
        override fun onConnected() {
        }

        /**
         * Handle handshake
         */
        override fun onHandshake(success: Boolean) {}

        /**
         * Handle disconnect
         */
        override fun onDisconnect() {
        }

        /**
         * Handle logon to web socket with minecraft account
         */
        override fun onLogon() {
        }

        /**
         * Handle incoming packets
         */
        override fun onPacket(packet: Packet) {
        }

        /**
         * Handle error
         */
        override fun onError(cause: Throwable) {
        }
    }

    private var loggedIn = false

    private var loginThread: Thread? = null

    private val connectTimer = MSTimer()

    override fun onDisable() {
        loggedIn = false
        client.disconnect()
    }

    @EventTarget
    fun onSession(sessionEvent: SessionEvent) {
        client.disconnect()
        connect()
    }

    @EventTarget
    fun onUpdate(updateEvent: UpdateEvent) {
    }

    private fun connect() {
    }

    /**
     * Forge Hooks
     *
     * @author Forge
     */

}