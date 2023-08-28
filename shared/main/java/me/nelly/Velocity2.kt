package me.nelly

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.server.*
import net.minecraft.network.play.client.*

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import javax.swing.text.Position

@ModuleInfo(name = "Velocity2", description = "fix by nelly---by小言 删这个你妈必死无疑", category = ModuleCategory.COMBAT)
class Velocity2 : Module() {
    private val OnlyMove = BoolValue("OnlyMove",false)
    private val OnlyGround = BoolValue("OnlyGround",false)
    private val AutoDisableMode = ListValue("AutoDisableMode",arrayOf("Safe", "Silent"),"Silent")
    private val AutoSilent = IntegerValue("AutoSilentTicks",10,0,10)
    var cancelPackets = 0
    private var resetPersec = 8
    private var updates = 0
    private var S08 = 0
    private val debugValue = BoolValue("Debug",false)
    private val packets = LinkedBlockingQueue<Packet<*>>()
    private var disableLogger = false
    private val inBus = LinkedList<Packet<INetHandlerPlayClient>>()

    fun debug(s: String) {
        if (debugValue.get())
            ClientUtils.displayChatMessage(s)
    }

    override fun onEnable() {
        if (mc.thePlayer == null) return
        inBus.clear()
        cancelPackets = 0
    }
    override fun onDisable(){
        if (mc.thePlayer == null) return
        inBus.clear()
        cancelPackets = 0
        blink()
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        if((OnlyMove.get()&&!MovementUtils.isMoving )||(OnlyGround.get()&&!mc.thePlayer!!.onGround)){
            return
        }



        val packet = event.packet.unwrap()

        if(S08>0){
            S08--
            debug("Off $S08")
            return
        }

        if(packet is SPacketPlayerPosLook){
            if(AutoDisableMode.get().equals("silent", ignoreCase = true)){
                S08 = AutoSilent.get()
            }
            if(AutoDisableMode.get().equals("safe", ignoreCase = true)){
                state = false
            }
        }

        if (packet is SPacketEntityVelocity) {
            if (mc.thePlayer == null || (mc.theWorld?.getEntityByID(packet.entityID) ?: return) != mc.thePlayer) {
                return
            }
            event.cancelEvent()
            cancelPackets = 6
        }

        if(cancelPackets > 0){
            if(MovementUtils.isMoving   || !OnlyMove.get()){
                if (mc.thePlayer == null || disableLogger) return
                if (packet is CPacketPlayer) // Cancel all movement stuff
                    event.cancelEvent()
                if (packet is Position || packet is CPacketPlayer ||
                    packet is ICPacketPlayerBlockPlacement ||
                    packet is CPacketAnimation||
                    packet is CPacketEntityAction|| packet is CPacketUseEntity) {
                    event.cancelEvent()
                    packets.add(packet)
                }
                if(packet::class.java.simpleName.startsWith("S", true)) {
                    if(packet is SPacketEntityVelocity && (mc.theWorld?.getEntityByID(packet.entityID) ?: return) == mc.thePlayer){return}
                    event.cancelEvent()
                    inBus.add(packet as Packet<INetHandlerPlayClient>)
                }
            }
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if((OnlyMove.get()&&!MovementUtils.isMoving)||(OnlyGround.get()&&!mc.thePlayer!!.onGround)){return}
        updates++
        if (resetPersec > 0) {
            if (updates >= 0) {
                updates = 0
                if (cancelPackets > 0){
                    cancelPackets--
                }
            }
        }
        if(cancelPackets == 0){
            blink()
        }
    }
    private fun blink() {
        try {
            disableLogger = true
            while (!packets.isEmpty()) {
                mc2.connection!!.networkManager.sendPacket(packets.take())
            }
            while (!inBus.isEmpty()) {
                inBus.poll()?.processPacket(mc2!!.connection)
            }
            disableLogger = false
        } catch (e: Exception) {
            e.printStackTrace()
            disableLogger = false
        }
    }
}