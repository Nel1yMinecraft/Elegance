package me.nelly

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.network.play.client.CPacketAnimation
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.network.play.server.SPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketEntityVelocity
import java.awt.Toolkit
import java.awt.event.InputEvent

@ModuleInfo(name = "CustomAntiKb", description = "CustomAntiKb", category = ModuleCategory.COMBAT)
class CustomAntiKb : Module() {
    val hurttime = FloatValue("Custom-MinHurtTime", 0f, 0f, 10f)
    val motionX = FloatValue("Custom-HurtMotionX", 0.1f, 0.0f, 1.0f)
    val motionY = FloatValue("Custom-HurtMotionY", 0.1f, 0.0f, 1.0f)
    val motionZ = FloatValue("Custom-HurtMotionZ", 0.1f, 0.0f, 1.0f)
    val onlymove = BoolValue("Custom-OnlyMove",true)
    val onlyground = BoolValue("Custom-OnlyGround",false)
    val hurtcancelpacket = BoolValue("HurtCancelPacket", true)
    val cancels12 = BoolValue("HurtCancelS12", true).displayable { hurtcancelpacket.get() }
    val cancelc0f = BoolValue("HurtCancelC0f", false).displayable { hurtcancelpacket.get() }
    val c0fnotattack = BoolValue("HurtCancelC0f-OnlyNoAttack", true).displayable { hurtcancelpacket.get() }
    val cancels32 = BoolValue("HurtCancelS32", false).displayable { hurtcancelpacket.get() }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        val packet2 = event.packet
        val packetEntityVelocity = packet2.asSPacketEntityVelocity()
        if (classProvider.isSPacketEntityVelocity(packet2))
            if ((mc.theWorld?.getEntityByID(packetEntityVelocity.entityID) ?: return) != mc2.player)
                return

        if((onlymove.get()&&!MovementUtils.isMoving )||(onlyground.get()&&!mc.thePlayer!!.onGround)){
            return
        }

        if (hurtcancelpacket.get()) {
            if (cancels12.get() && packet is SPacketEntityVelocity) {
                event.cancelEvent()
            }
            if (cancelc0f.get() && packet is CPacketConfirmTransaction && !c0fnotattack.get()) {
                event.cancelEvent()
            }
            if (cancels32.get() && packet is SPacketConfirmTransaction) {
                event.cancelEvent()
            }
            if (cancelc0f.get() && packet is CPacketConfirmTransaction && c0fnotattack.get() && !islet() && packet is CPacketAnimation && packet is CPacketUseEntity) {
                event.cancelEvent()
            }
            if (mc2.player.hurtTime > hurttime.get()) {
                packetEntityVelocity.motionX = motionX.get().toInt()
                packetEntityVelocity.motionY = motionY.get().toInt()
                packetEntityVelocity.motionZ = motionZ.get().toInt()
            }
        }
    }
    private fun islet(): Boolean {
        val toolkit = Toolkit.getDefaultToolkit()
        val mouseState = toolkit.getSystemEventQueue().peekEvent(InputEvent.MOUSE_EVENT_MASK.toInt())
        if (mouseState is java.awt.event.MouseEvent)
            if (mouseState.button == java.awt.event.MouseEvent.BUTTON1) {
                return true
            } else {
                return false
            }
        return false
    }
    private fun getKbcalculateAverage(): Double {
        return (motionX.get() + motionY.get() + motionZ.get()) / 3.0 * 100
    }

    override val tag: String
        get() = "Knockback:${getKbcalculateAverage().toInt()}%"
}