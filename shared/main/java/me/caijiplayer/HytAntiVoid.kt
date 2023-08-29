package me.caijiplayer;

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold2
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold3
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold4
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.server.SPacketPlayerPosLook
/*
是这样
*去掉了没用的，加了点有用的
*那个autoScaffold好不好用取决于使用者
*回弹来自fdp
 */
@ModuleInfo(name = "HytAntiVoid",  description = "fix by nelly", category = ModuleCategory.MOVEMENT)
class HytAntiVoid : Module() {
    private val pullbackTime = IntegerValue("PullbackTime", 850, 800, 1800)
    private val autoScaffoldValue = BoolValue("AutoScaffold", false)
    private val debug = BoolValue("Debug", false)
    var timer: TimeHelper = TimeHelper()
    var lastGroundPos = DoubleArray(3)
    var packets = ArrayList<IPacket>()
    private val X = IntegerValue("ticks", 20, -0, 100)
    var Y = X.get()
    private val scaffoldModule =
        ListValue("ScaffoldModule", arrayOf("Scaffold", "Scaffold2", "Scaffold3", "Scaffold4"), "Scaffold")
    @EventTarget
    open fun isInVoid(): Boolean {
        for (i in 0..128) {
            if (MovementUtils.onGround(i.toDouble())) {
                return false
            }
        }
        return true
    }
    @EventTarget
    fun onPacket(e: PacketEvent) {
        var packet = e.packet.unwrap()
        if (!LiquidBounce.moduleManager.get(Fly::class.java)!!.state && !LiquidBounce.moduleManager.get(Scaffold::class.java)!!.state) {
            if (!packets.isEmpty() && mc.thePlayer!!.ticksExisted < 100) packets.clear()
            if (packet is CPacketPlayer) {
                if (isInVoid()) {
                    e.cancelEvent()
                    packets.add(e.packet)
                    if (timer.delay(pullbackTime.get().toLong())) {
                        e.cancelEvent()
                    }
                } else {
                    lastGroundPos[0] = mc.thePlayer!!.posX
                    lastGroundPos[1] = mc.thePlayer!!.posY
                    lastGroundPos[2] = mc.thePlayer!!.posZ
                    if (!packets.isEmpty()) {
                        val var3: Iterator<*> = packets.iterator()
                        debug("[HytAntiVoid] SendPacket......  " + packets.size)
                        while (var3.hasNext()) {
                            mc.netHandler.addToSendQueue(classProvider.createCPacketPlayer(false))
                        }
                        packets.clear()
                    }
                    timer.reset()
                }
            }
        }
    }
    private fun scaffoldChange(state: Boolean) {
        when (scaffoldModule.get().toLowerCase()) {
            "scaffold" -> LiquidBounce.moduleManager.getModule(Scaffold::class.java).state = state
            "scaffold2" -> LiquidBounce.moduleManager.getModule(Scaffold2::class.java).state = state
            "scaffold3" -> LiquidBounce.moduleManager.getModule(Scaffold3::class.java).state = state
            "scaffold4" -> LiquidBounce.moduleManager.getModule(Scaffold4::class.java).state = state
        }
    }
    @EventTarget
    fun onUpdate(event:UpdateEvent) {
        if(autoScaffoldValue.get())
            if (Y != 0){
            scaffoldChange(true)
            Y -= 1
        }else{
            scaffoldChange(false)
        }
    }

    @EventTarget
    fun onRevPacket(e: PacketEvent) {
        if (e.packet.unwrap() is SPacketPlayerPosLook && packets.size > 1) {
            debug("[HytAntiVoid] has lag, clar packets")
            packets.clear()
        }
    }
    private fun debug(str: String) {
        if (debug.get()) {
            ClientUtils.displayChatMessage(str)
        }
    }

    override val tag: String
        get() = pullbackTime.get().toString()
}