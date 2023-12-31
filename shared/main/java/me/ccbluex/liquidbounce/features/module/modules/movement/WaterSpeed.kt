/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.block.BlockUtils.getBlock
import me.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "WaterSpeed", description = "Allows you to swim faster.", category = ModuleCategory.MOVEMENT)
class WaterSpeed : Module() {
    private val speedValue = FloatValue("Speed", 1.2f, 1.1f, 1.5f)

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.isInWater && classProvider.isBlockLiquid(getBlock(thePlayer.position))) {
            val speed = speedValue.get()

            thePlayer.motionX *= speed
            thePlayer.motionZ *= speed
        }
    }
}