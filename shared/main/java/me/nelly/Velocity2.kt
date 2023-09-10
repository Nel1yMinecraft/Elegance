package me.nelly

import me.ccbluex.liquidbounce.event.EventState
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.MotionEvent
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.block.BlockSlab
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketPlayerPosLook
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

@ModuleInfo(name = "Velocity2", description = "By nelly 改我/删我=死全家(除本人)", category = ModuleCategory.COMBAT)
class Velocity2 : Module() {
    private val OnlyMove = BoolValue("OnlyMove", false)
    private val OnlyGround = BoolValue("OnlyGround", false)
    private var packets = 0

    private fun isPlayerOnSlab(player: EntityPlayer): Boolean {
        val playerPos = BlockPos(player.posX, player.posY, player.posZ)

        val block = player.world.getBlockState(playerPos).block
        val boundingBox = player.entityBoundingBox

        return block is BlockSlab && player.posY - playerPos.y <= boundingBox.minY + 0.1
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if ((OnlyMove.get() && !MovementUtils.isMoving) || (OnlyGround.get() && !mc.thePlayer!!.onGround)) {
            return
        }

        val packet = event.packet.unwrap()

        if (packets > 0) {
            packets--
            return
        }

        if (packet is SPacketPlayerPosLook) {
            packets = 10
        }

        if (packet is SPacketEntityVelocity && mc2.player.hurtTime > 0) {
            event.cancelEvent()
            ClientUtils.displayChatMessage("Velocity2-Cancel S12Packet")
        }
    }
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if ((OnlyMove.get() && !MovementUtils.isMoving) || (OnlyGround.get() && !mc.thePlayer!!.onGround)) {
            return
        }
        if (event.eventState == EventState.PRE && !mc2.playerController.isHittingBlock && mc2.player.hurtTime > 0 && !isPlayerOnSlab(mc2.player)) {
            val blockPos = BlockPos(mc2.player.posX, mc2.player.posY, mc2.player.posZ)
            mc2.connection!!.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    blockPos,
                    EnumFacing.NORTH
                )
            )
            ClientUtils.displayChatMessage("Velocity2-Send C07Packet")
        }
    }
}