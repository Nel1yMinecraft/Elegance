/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.JumpEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.block.BlockUtils.collideBlock
import me.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "ReverseStep", description = "Allows you to step down blocks faster.", category = ModuleCategory.MOVEMENT)
class ReverseStep : Module() {
    private val motionValue = FloatValue("Motion", 1f, 0.21f, 1f)
    private var jumped = false

    @EventTarget(ignoreCondition = true)
    fun onUpdate(event: UpdateEvent?) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.onGround)
            jumped = false

        if (thePlayer.motionY > 0)
            jumped = true

        if (!state)
            return

        if (collideBlock(thePlayer.entityBoundingBox, classProvider::isBlockLiquid) || collideBlock(classProvider.createAxisAlignedBB(thePlayer.entityBoundingBox.maxX, thePlayer.entityBoundingBox.maxY, thePlayer.entityBoundingBox.maxZ, thePlayer.entityBoundingBox.minX, thePlayer.entityBoundingBox.minY - 0.01, thePlayer.entityBoundingBox.minZ), classProvider::isBlockLiquid)) return

        if (!mc.gameSettings.keyBindJump.isKeyDown && !thePlayer.onGround && !thePlayer.movementInput.jump && thePlayer.motionY <= 0.0 && thePlayer.fallDistance <= 1f && !jumped)
            thePlayer.motionY = (-motionValue.get()).toDouble()
    }

    @EventTarget(ignoreCondition = true)
    fun onJump(event: JumpEvent?) {
        jumped = true
    }

}