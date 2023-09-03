package me.nelly

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance.mc2
import net.minecraft.command.ICommand
import net.minecraft.network.play.client.CPacketTabComplete
import net.minecraftforge.client.event.ClientChatReceivedEvent
@ModuleInfo(name = "HytPlugin",  description = "get hyt plugins", category = ModuleCategory.MISC)
class HytPlugin : Module()
var tabCompletionActive = false

    @EventTarget
    fun onEntityJoinWorld() {
        // 只在玩家进入世界时开启补全功能
        tabCompletionActive = true

        fun onUpdate(event: ClientChatReceivedEvent) {
            var message = event.message.unformattedText
            if (tabCompletionActive) {
                message = "/ver"
                val command = message.substring(5)
                val packet = CPacketTabComplete(command, mc2.player.position, false)
                mc2.player.connection.sendPacket(packet)
                ClientUtils.displayChatMessage(command)


                // 阻止原始指令的发送
                event.isCanceled = true
            }
        }
    }
