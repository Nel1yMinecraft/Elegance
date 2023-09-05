/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.player

import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.ClickBlockEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "AutoTool", description = "Automatically selects the best tool in your inventory to mine a block.", category = ModuleCategory.PLAYER)
class AutoTool : Module() {

    @EventTarget
    fun onClick(event: ClickBlockEvent) {
        switchSlot(event.clickedBlock ?: return)
    }

    fun switchSlot(blockPos: WBlockPos) {
        var bestSpeed = 1F
        var bestSlot = -1

        val blockState = mc.theWorld!!.getBlockState(blockPos)

        for (i in 0..8) {
            val item = mc.thePlayer!!.inventory.getStackInSlot(i) ?: continue
            val speed = item.getStrVsBlock(blockState)

            if (speed > bestSpeed) {
                bestSpeed = speed
                bestSlot = i
            }
        }

        if (bestSlot != -1)
            mc.thePlayer!!.inventory.currentItem = bestSlot
    }

}