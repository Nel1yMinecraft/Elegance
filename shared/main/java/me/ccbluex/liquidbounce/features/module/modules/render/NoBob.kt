/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.render

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "NoBob", description = "Disables the view bobbing effect.", category = ModuleCategory.RENDER)
class NoBob : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        mc.thePlayer?.distanceWalkedModified = 0f
    }
}
