/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat

import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.ListValue
import java.util.*

@ModuleInfo(name = "AutoLeave", description = "Automatically makes you leave the server whenever your health is low.", category = ModuleCategory.COMBAT)
class AutoLeave : Module() {
    private val healthValue = FloatValue("Health", 8f, 0f, 20f)
    private val modeValue = ListValue("Mode", arrayOf("Quit", "InvalidPacket", "SelfHurt", "IllegalChat"), "Quit")

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.health <= healthValue.get() && !thePlayer.capabilities.isCreativeMode && !mc.isIntegratedServerRunning) {
            when (modeValue.get().toLowerCase()) {
                "quit" -> mc.theWorld!!.sendQuittingDisconnectingPacket()
                "invalidpacket" -> mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, !thePlayer.onGround))
                "selfhurt" -> mc.netHandler.addToSendQueue(classProvider.createCPacketUseEntity(thePlayer, ICPacketUseEntity.WAction.ATTACK))
                "illegalchat" -> thePlayer.sendChatMessage(Random().nextInt().toString() + "§§§" + Random().nextInt())
            }

            state = false
        }
    }
}