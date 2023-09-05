/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.player

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.features.module.modules.exploit.Ghost
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "AutoRespawn", description = "Automatically respawns you after dying.", category = ModuleCategory.PLAYER)
class AutoRespawn : Module() {

    private val instantValue = BoolValue("Instant", true)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer

        if (thePlayer == null || LiquidBounce.moduleManager[Ghost::class.java]!!.state)
            return

        if (if (instantValue.get()) thePlayer.health == 0F || thePlayer.isDead else classProvider.isGuiGameOver(mc.currentScreen)
                        && (mc.currentScreen!!.asGuiGameOver()).enableButtonsTimer >= 20) {
            thePlayer.respawnPlayer()
            mc.displayGuiScreen(null)
        }
    }
}