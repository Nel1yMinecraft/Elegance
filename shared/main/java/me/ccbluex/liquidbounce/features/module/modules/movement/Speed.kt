/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.event.*
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import me.ccbluex.liquidbounce.features.module.modules.movement.speeds.hyt.HytFastHop
import me.ccbluex.liquidbounce.features.module.modules.movement.speeds.hyt.HytSlowHop
import me.ccbluex.liquidbounce.features.module.modules.movement.speeds.ncp.*
import me.ccbluex.liquidbounce.features.module.modules.movement.speeds.other.*
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.ListValue
import java.util.*

@ModuleInfo(name = "Speed", description = "Allows you to move faster.", category = ModuleCategory.MOVEMENT)
class Speed : Module() {
    private val speedModes = arrayOf(
            NCPBHop(),
            CustomSpeed(),
            HytSlowHop(),
            HytFastHop()
    )

    val modeValue: ListValue = object : ListValue("Mode", modes, "HytSlowHop") {
        override fun onChange(oldValue: String, newValue: String) {
            if (state)
                onDisable()
        }

        override fun onChanged(oldValue: String, newValue: String) {
            if (state)
                onEnable()
        }
    }
    val customSpeedValue = FloatValue("CustomSpeed", 1.6f, 0.2f, 2f)
    val customYValue = FloatValue("CustomY", 0f, 0f, 4f)
    val customTimerValue = FloatValue("CustomTimer", 1f, 0.1f, 2f)
    val customStrafeValue = BoolValue("CustomStrafe", true)
    val resetXZValue = BoolValue("CustomResetXZ", false)
    val resetYValue = BoolValue("CustomResetY", false)

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.sneaking)
            return

        if (MovementUtils.isMoving) {
            thePlayer.sprinting = true
        }

        mode?.onUpdate()
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.sneaking || event.eventState != EventState.PRE)
            return

        if (MovementUtils.isMoving)
            thePlayer.sprinting = true

        mode?.onMotion()
    }

    @EventTarget
    fun onMove(event: MoveEvent?) {
        if (mc.thePlayer!!.sneaking)
            return
        mode?.onMove(event!!)
    }

    @EventTarget
    fun onTick(event: TickEvent?) {
        if (mc.thePlayer!!.sneaking)
            return

        mode?.onTick()
    }

    override fun onEnable() {
        if (mc.thePlayer == null)
            return

        mc.timer.timerSpeed = 1f

        mode?.onEnable()
    }

    override fun onDisable() {
        if (mc.thePlayer == null)
            return

        mc.timer.timerSpeed = 1f

        mode?.onDisable()
    }

    override val tag: String
        get() = modeValue.get()

    private val mode: SpeedMode?
        get() {
            val mode = modeValue.get()

            for (speedMode in speedModes) if (speedMode.modeName.equals(mode, ignoreCase = true))
                return speedMode

            return null
        }

    private val modes: Array<String>
        get() {
            val list: MutableList<String> = ArrayList()
            for (speedMode in speedModes) list.add(speedMode.modeName)
            return list.toTypedArray()
        }
}
