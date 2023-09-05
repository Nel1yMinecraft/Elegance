/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement.speeds

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.MoveEvent
import me.ccbluex.liquidbounce.features.module.modules.movement.Speed
import me.ccbluex.liquidbounce.utils.MinecraftInstance

abstract class SpeedMode(val modeName: String) : MinecraftInstance() {
    val isActive: Boolean
        get() {
            val speed = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed?
            return speed != null && !mc.thePlayer!!.sneaking && speed.state && speed.modeValue.get() == modeName
        }

    abstract fun onMotion()
    abstract fun onUpdate()
    abstract fun onMove(event: MoveEvent)
    open fun onTick() {}
    open fun onEnable() {}
    open fun onDisable() {}

}