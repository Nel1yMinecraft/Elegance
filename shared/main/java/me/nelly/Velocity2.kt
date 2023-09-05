package me.nelly

import me.ccbluex.liquidbounce.event.*
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.server.*
import net.minecraft.network.play.client.*
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

@ModuleInfo(name = "AntiKB", description = "By nelly", category = ModuleCategory.COMBAT)
class Velocity2 : Module() {
    private val OnlyMove = BoolValue("OnlyMove", false)
    private val OnlyGround = BoolValue("OnlyGround", false)
    private val debugValue = BoolValue("Debug", false)
    private var kb = false
    private var packets = 0

    fun debug(s: String) {
        if (debugValue.get())
            ClientUtils.displayChatMessage(s)
    }

    override fun onEnable() {
        if (mc.thePlayer == null) return
    }

    override fun onDisable() {
        if (mc.thePlayer == null) return
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

        if (packet is SPacketEntityVelocity) {
            event.cancelEvent()
            kb = true
            debug("Velocity2-Cancel S12Packet")
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if ((OnlyMove.get() && !MovementUtils.isMoving) || (OnlyGround.get() && !mc.thePlayer!!.onGround)) {
            return
        }
        if (event.eventState == EventState.PRE && kb) {
            kb = false
            val blockPos = BlockPos(mc2.player.posX, mc2.player.posY, mc2.player.posZ)
            mc2.connection!!.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    blockPos,
                    EnumFacing.NORTH
                )
            )
            debug("Velocity2-Send C07Packet")
        }
    }
}