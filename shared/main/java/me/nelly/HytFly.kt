package me.nelly

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura


@ModuleInfo(
    name = "HytHurtFly", description = "HytFly",
    category = ModuleCategory.MOVEMENT
)
class HytFly : Module() {
    val ka = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (mc.thePlayer!!.hurtTime > 0) {
            mc.thePlayer!!.setPositionAndRotation(
                mc.thePlayer!!.posX + 100000,
                mc.thePlayer!!.posY,
                mc.thePlayer!!.posZ,
                mc.thePlayer!!.rotationYaw,
                mc.thePlayer!!.rotationPitch
            )
        } else {
            mc.thePlayer!!.setPositionAndRotation(
                mc.thePlayer!!.posX - 100000,
                mc.thePlayer!!.posY - 0.11,
                mc.thePlayer!!.posZ,
                mc.thePlayer!!.rotationYaw,
                mc.thePlayer!!.rotationPitch
            )
        }
        @EventTarget
        fun onMove(event: MoveEvent) {
            if (mc.thePlayer!!.hurtTime > 0) {
                mc.thePlayer!!.setPositionAndRotation(
                    mc.thePlayer!!.posX + 100000,
                    mc.thePlayer!!.posY - 0.11,
                    mc.thePlayer!!.posZ,
                    mc.thePlayer!!.rotationYaw,
                    mc.thePlayer!!.rotationPitch
                )
            } else {
                mc.thePlayer!!.setPositionAndRotation(
                    mc.thePlayer!!.posX - 100000,
                    mc.thePlayer!!.posY - 0.11,
                    mc.thePlayer!!.posZ,
                    mc.thePlayer!!.rotationYaw,
                    mc.thePlayer!!.rotationPitch
                )
            }
            @EventTarget
            fun onUpdate(event: UpdateEvent) {
                if (mc.thePlayer!!.hurtTime > 0) {
                    mc.thePlayer!!.setPositionAndRotation(
                        mc.thePlayer!!.posX + 100000,
                        mc.thePlayer!!.posY - 0.11,
                        mc.thePlayer!!.posZ,
                        mc.thePlayer!!.rotationYaw,
                        mc.thePlayer!!.rotationPitch
                    )
                } else {
                    mc.thePlayer!!.setPositionAndRotation(
                        mc.thePlayer!!.posX - 100000,
                        mc.thePlayer!!.posY - 0.11,
                        mc.thePlayer!!.posZ,
                        mc.thePlayer!!.rotationYaw,
                        mc.thePlayer!!.rotationPitch
                    )
                }
            }
            @EventTarget
            fun onAttack(event: AttackEvent) {
                // Add theplayer kb
                if(mc.thePlayer!!.hurtTime > 0) {
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(ka.target!!, ICPacketEntityAction.WAction.STOP_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(ka.target!!, ICPacketEntityAction.WAction.START_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(ka.target!!, ICPacketEntityAction.WAction.STOP_SPRINTING))
                    mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(ka.target!!, ICPacketEntityAction.WAction.START_SPRINTING))
                }
            }
        }
    }
}


