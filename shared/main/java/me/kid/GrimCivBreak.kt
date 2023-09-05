/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.kid

import me.ccbluex.liquidbounce.api.enums.EnumFacingType
import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.*
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.block.BlockUtils
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.util.math.BlockPos
import java.awt.Color

@ModuleInfo(name = "GrimCivBreak", description = "GrimCivBreak", category = ModuleCategory.WORLD)
class GrimCivBreak : Module() {
    private val breakDamage = FloatValue("BreakDamage",0f,0f,2f)
    private val range = FloatValue("Range", 5F, 1F, 6F)
    private var blockPos: WBlockPos? = null
    private var blockPos2: BlockPos? = null
    private var breaking = false
    private var speed = 0f

    override fun onEnable() {
        blockPos = null
        blockPos2 = null
        breaking = false
        speed = 0f
    }

    @EventTarget
    fun onBlockClick(event: ClickBlockEvent) {
        if (classProvider.isBlockBedrock(event.clickedBlock?.let { BlockUtils.getBlock(it) }))
            return

        blockPos = event.clickedBlock ?: return
        blockPos2 = event.clickedBlock2 ?: return
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent){
            if(breaking){
                speed += try {
                    blockPos!!.getBlock()!!.getPlayerRelativeBlockHardness(mc.thePlayer!!, mc.theWorld!!, blockPos!!)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    return
                }
                if(speed > breakDamage.get() && BlockUtils.getCenterDistance(blockPos!!) <= range.get()){
                    try {
                        mc2.world.setBlockState(blockPos2!!, Blocks.AIR.defaultState, 11)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        return
                    }
                    mc.netHandler.addToSendQueue(
                        classProvider.createCPacketPlayerDigging(
                            ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK,
                            blockPos!!,
                            classProvider.getEnumFacing(EnumFacingType.DOWN)
                        )
                    )
                }
            }

    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (packet is CPacketPlayerDigging) {
            val digPacket: CPacketPlayerDigging = packet
            if (digPacket.action == CPacketPlayerDigging.Action.START_DESTROY_BLOCK)
                breaking = true
            speed = 0f
        }

        if (packet is CPacketPlayerDigging) {
            val digPacket: CPacketPlayerDigging = packet
            if (digPacket.action == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
                blockPos = null
            breaking = false
            speed = 0f
            mc2.player.isSprinting
        }
    }

    @EventTarget fun onMotion(event: MotionEvent){
        when(event.eventState){
            EventState.POST ->{
                if(breaking){
                    mc.netHandler.addToSendQueue(classProvider.createCPacketAnimation())
                }
            }
        }
    }
    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        RenderUtils.drawBlockBox(blockPos ?: return, Color.RED,Color.BLACK, true)
    }
}