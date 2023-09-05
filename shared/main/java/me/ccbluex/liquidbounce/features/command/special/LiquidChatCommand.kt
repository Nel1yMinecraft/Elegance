package me.ccbluex.liquidbounce.features.command.special

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.features.command.Command
import me.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat
import me.ccbluex.liquidbounce.utils.misc.StringUtils

class LiquidChatCommand : Command("chat", "lc", "irc") {

    private val lChat = LiquidBounce.moduleManager.getModule(LiquidChat::class.java) as LiquidChat

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            if (!lChat.state) {
                chat("§cError: §7LiquidChat is disabled!")
                return
            }

            if (!lChat.client.isConnected()) {
                chat("§cError: §LiquidChat is currently not connected to the server!")
                return
            }

            val message = StringUtils.toCompleteString(args, 1)

            lChat.client.sendMessage(message)
        } else
            chatSyntax("chat <message>")
    }
}