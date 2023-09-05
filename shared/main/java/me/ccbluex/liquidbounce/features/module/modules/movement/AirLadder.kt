/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.block.BlockUtils

@ModuleInfo(name = "AirLadder", description = "Allows you to climb up ladders/vines without touching them.", category = ModuleCategory.MOVEMENT)
class AirLadder : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (classProvider.isBlockLadder(BlockUtils.getBlock(WBlockPos(thePlayer.posX, thePlayer.posY + 1, thePlayer.posZ))) && thePlayer.isCollidedHorizontally ||
                classProvider.isBlockVine(BlockUtils.getBlock(WBlockPos(thePlayer.posX, thePlayer.posY, thePlayer.posZ))) ||
                classProvider.isBlockVine(BlockUtils.getBlock(WBlockPos(thePlayer.posX, thePlayer.posY + 1, thePlayer.posZ)))) {
            thePlayer.motionY = 0.15
            thePlayer.motionX = 0.0
            thePlayer.motionZ = 0.0
        }
    }
}