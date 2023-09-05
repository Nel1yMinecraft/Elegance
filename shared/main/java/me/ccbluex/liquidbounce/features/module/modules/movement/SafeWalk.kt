/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.MoveEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "SafeWalk", description = "Prevents you from falling down as if you were sneaking.", category = ModuleCategory.MOVEMENT)
class SafeWalk : Module() {

    private val airSafeValue = BoolValue("AirSafe", false)

    @EventTarget
    fun onMove(event: MoveEvent) {
        if (airSafeValue.get() || mc.thePlayer!!.onGround)
            event.isSafeWalk = true
    }
}
