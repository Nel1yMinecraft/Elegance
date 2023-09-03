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
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if ( mc2.player.hurtTime > 0) {
             mc2.player.setPositionAndRotation(
                 mc2.player.posX + 100000,
                 mc2.player.posY,
                 mc2.player.posZ,
                 mc2.player.rotationYaw,
                 mc2.player.rotationPitch
            )
        } else {
             mc2.player.setPositionAndRotation(
                 mc2.player.posX - 100000,
                 mc2.player.posY + 0.11,
                 mc2.player.posZ,
                 mc2.player.rotationYaw,
                 mc2.player.rotationPitch
            )
        }
        @EventTarget
        fun onMove(event: MoveEvent) {
            if ( mc2.player.hurtTime > 0) {
                mc2.player.setPositionAndRotation(
                     mc2.player.posX + 100000,
                     mc2.player.posY - 0.11,
                     mc2.player.posZ,
                     mc2.player.rotationYaw,
                     mc2.player.rotationPitch
                )
            } else {
                 mc2.player.setPositionAndRotation(
                     mc2.player.posX - 100000,
                     mc2.player.posY + 0.11,
                     mc2.player.posZ,
                     mc2.player.rotationYaw,
                     mc2.player.rotationPitch
                )
            }
            @EventTarget
            fun onUpdate(event: UpdateEvent) {
                if ( mc2.player.hurtTime > 0) {
                     mc2.player.setPositionAndRotation(
                         mc2.player.posX + 100000,
                         mc2.player.posY - 0.11,
                         mc2.player.posZ,
                         mc2.player.rotationYaw,
                         mc2.player.rotationPitch
                    )
                } else {
                     mc2.player.setPositionAndRotation(
                         mc2.player.posX - 100000,
                         mc2.player.posY + 0.11,
                         mc2.player.posZ,
                         mc2.player.rotationYaw,
                         mc2.player.rotationPitch
                    )
                }
            }
        }
    }
}


