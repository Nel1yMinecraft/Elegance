/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.command.commands

import me.ccbluex.liquidbounce.features.command.Command

class PingCommand : Command("ping") {

    override fun execute(args: Array<String>) {
        chat("§3Your ping is §a${mc.netHandler.getPlayerInfo(mc.thePlayer!!.uniqueID)!!.responseTime}ms§3.")
    }

}