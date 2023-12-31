/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import me.ccbluex.liquidbounce.event.EventState
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.MotionEvent
import me.ccbluex.liquidbounce.event.WorldEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Sneak", description = "Automatically sneaks all the time.", category = ModuleCategory.MOVEMENT)
class Sneak : Module() {

    @JvmField
    val modeValue = ListValue("Mode", arrayOf("Legit", "Vanilla", "Switch", "MineSecure"), "MineSecure")
    @JvmField
    val stopMoveValue = BoolValue("StopMove", false)

    private var sneaking = false

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (stopMoveValue.get() && MovementUtils.isMoving) {
            if (sneaking)
                onDisable()
            return
        }

        when (modeValue.get().toLowerCase()) {
            "legit" -> mc.gameSettings.keyBindSneak.pressed = true
            "vanilla" -> {
                if (sneaking)
                    return

                mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
            }

            "switch" -> {
                when (event.eventState) {
                    EventState.PRE -> {
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SNEAKING))
                    }
                    EventState.POST -> {
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.STOP_SNEAKING))
                        mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
                    }
                }
            }

            "minesecure" -> {
                if (event.eventState == EventState.PRE)
                    return

                mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!, ICPacketEntityAction.WAction.START_SNEAKING))
            }
        }
    }

    @EventTarget
    fun onWorld(worldEvent: WorldEvent) {
        sneaking = false
    }

    override fun onDisable() {
        val player = mc.thePlayer ?: return

        when (modeValue.get().toLowerCase()) {
            "legit" -> {
                if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
                    mc.gameSettings.keyBindSneak.pressed = false
                }
            }
            "vanilla", "switch", "minesecure" -> mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SNEAKING))
        }
        sneaking = false
    }
}