/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.world

import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import me.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.*
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.block.BlockUtils
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import java.awt.Color

@ModuleInfo(name = "CivBreak", description = "Allows you to break blocks instantly.", category = ModuleCategory.WORLD)
class CivBreak : Module() {

    private var blockPos: WBlockPos? = null
    private var enumFacing: IEnumFacing? = null

    private val range = FloatValue("Range", 5F, 1F, 6F)
    private val rotationsValue = BoolValue("Rotations", true)
    private val visualSwingValue = BoolValue("VisualSwing", true)

    private val airResetValue = BoolValue("Air-Reset", true)
    private val rangeResetValue = BoolValue("Range-Reset", true)


    @EventTarget
    fun onBlockClick(event: ClickBlockEvent) {
        if (classProvider.isBlockBedrock(event.clickedBlock?.let { BlockUtils.getBlock(it) }))
            return

        blockPos = event.clickedBlock ?: return
        enumFacing = event.WEnumFacing ?: return

        // Break
        mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, blockPos!!, enumFacing!!))
        mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, blockPos!!, enumFacing!!))
    }

    @EventTarget
    fun onUpdate(event: MotionEvent) {
        val pos = blockPos ?: return

        if (airResetValue.get() && classProvider.isBlockAir(BlockUtils.getBlock(pos)) ||
                rangeResetValue.get() && BlockUtils.getCenterDistance(pos) > range.get()) {
            blockPos = null
            return
        }

        if (classProvider.isBlockAir(BlockUtils.getBlock(pos)) || BlockUtils.getCenterDistance(pos) > range.get())
            return

        when (event.eventState) {
            EventState.PRE -> if (rotationsValue.get())
                RotationUtils.setTargetRotation((RotationUtils.faceBlock(pos) ?: return).rotation)

            EventState.POST -> {
                if (visualSwingValue.get())
                    mc.thePlayer!!.swingItem()
                else
                    mc.netHandler.addToSendQueue(classProvider.createCPacketAnimation())

                // Break
                mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK,
                        blockPos!!, enumFacing!!))
                mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK,
                        blockPos!!, enumFacing!!))
                mc.playerController.clickBlock(blockPos!!, enumFacing!!)
            }
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        RenderUtils.drawBlockBox(blockPos ?: return, Color.RED, true)
    }
}