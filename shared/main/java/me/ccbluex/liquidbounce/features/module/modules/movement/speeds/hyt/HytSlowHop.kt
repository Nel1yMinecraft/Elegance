package me.ccbluex.liquidbounce.features.module.modules.movement.speeds.hyt

import me.ccbluex.liquidbounce.event.MoveEvent
import me.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import me.ccbluex.liquidbounce.utils.MovementUtils

class HytSlowHop : SpeedMode("HytSlowHop"){
    override fun onMotion() {
    }

    override fun onUpdate() {
        mc.gameSettings.keyBindJump.pressed = mc.gameSettings.isKeyDown(mc.gameSettings.keyBindJump)
        if (MovementUtils.isMoving) {
            if (mc.thePlayer!!.onGround) {
                mc.gameSettings.keyBindJump.pressed = false
                mc.thePlayer!!.jump()
            }

            if (mc.thePlayer!!.motionY > 0.003) {
                mc.thePlayer!!.motionX *= 1.00015
                mc.thePlayer!!.motionZ *= 1.00015
            }
        }

    }

    override fun onMove(event: MoveEvent) {}
}