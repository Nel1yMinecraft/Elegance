package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.hyt

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

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