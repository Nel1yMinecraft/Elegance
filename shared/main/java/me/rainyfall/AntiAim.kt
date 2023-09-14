/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */

package me.rainyfall

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.Rotation
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.misc.RandomUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "AntiAim" ,description = "OMG!!!", category = ModuleCategory.MISC)
class AntiAim : Module() {
    private val yawMode = ListValue("YawMove", arrayOf("Jitter", "Spin", "Back", "BackJitter"), "Spin")
    private val pitchMode = ListValue("PitchMode", arrayOf("Down", "Up", "Jitter", "AnotherJitter"), "Down")
    private val rotateValue = BoolValue("SilentRotate", true)

    private var yaw = 0f
    private var pitch = 0f

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        when (yawMode.get().toLowerCase()) {
            "spin" -> {
                yaw += 20.0f
                if (yaw > 180.0f) {
                    yaw = -180.0f
                } else if (yaw < -180.0f) {
                    yaw = 180.0f
                }
            }

            "jitter" -> {
                yaw = mc.thePlayer!!.rotationYaw + if (mc.thePlayer!!.ticksExisted % 2 == 0) 90F else -90F
            }

            "back" -> {
                yaw = mc.thePlayer!!.rotationYaw + 180f
            }

            "backjitter" -> {
                yaw = mc.thePlayer!!.rotationYaw + 180f + RandomUtils.nextDouble(-3.0, 3.0).toFloat()
            }
        }

        when (pitchMode.get().toLowerCase()) {
            "up" -> {
                pitch = -90.0f
            }

            "down" -> {
                pitch = 90.0f
            }

            "anotherjitter" -> {
                pitch = 60f + RandomUtils.nextDouble(-3.0, 3.0).toFloat()
            }

            "jitter" -> {
                pitch += 30.0f
                if (pitch > 90.0f) {
                    pitch = -90.0f
                } else if (pitch < -90.0f) {
                    pitch = 90.0f
                }
            }
        }

        if (rotateValue.get()) {
            RotationUtils.setTargetRotation(Rotation(yaw, pitch))
        } else {
            mc.thePlayer!!.rotationYaw = yaw
            mc.thePlayer!!.rotationPitch = pitch
        }
    }
}