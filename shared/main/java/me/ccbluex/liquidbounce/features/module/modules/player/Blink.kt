/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.player

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityOtherPlayerMP
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.IntegerValue
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

@ModuleInfo(name = "Blink", description = "Suspends all movement packets.", category = ModuleCategory.PLAYER)
class Blink : Module() {
    private val packets = LinkedBlockingQueue<IPacket>()
    private var fakePlayer: IEntityOtherPlayerMP? = null
    private var disableLogger = false
    private val positions = LinkedList<DoubleArray>()
    private val pulseValue = BoolValue("Pulse", false)
    private val pulseDelayValue = IntegerValue("PulseDelay", 1000, 500, 5000)
    private val pulseTimer = MSTimer()

    override fun onEnable() {
        val thePlayer = mc.thePlayer ?: return

        if (!pulseValue.get()) {
            val faker: IEntityOtherPlayerMP = classProvider.createEntityOtherPlayerMP(mc.theWorld!!, thePlayer.gameProfile)


            faker.rotationYawHead = thePlayer.rotationYawHead;
            faker.renderYawOffset = thePlayer.renderYawOffset;
            faker.copyLocationAndAnglesFrom(thePlayer)
            faker.rotationYawHead = thePlayer.rotationYawHead
            mc.theWorld!!.addEntityToWorld(-1337, faker)


            fakePlayer = faker
        }
        synchronized(positions) {
            positions.add(doubleArrayOf(thePlayer.posX, thePlayer.entityBoundingBox.minY + thePlayer.eyeHeight / 2, thePlayer.posZ))
            positions.add(doubleArrayOf(thePlayer.posX, thePlayer.entityBoundingBox.minY, thePlayer.posZ))
        }
        pulseTimer.reset()
    }

    override fun onDisable() {
        if (mc.thePlayer == null)
            return

        blink()

        val faker = fakePlayer

        if (faker != null) {
            mc.theWorld?.removeEntityFromWorld(faker.entityId)
            fakePlayer = null
        }
        val breadcrumbs = LiquidBounce.moduleManager.getModule(Breadcrumbs::class.java) as Breadcrumbs?
        breadcrumbs!!.state = false
    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet: IPacket = event.packet

        if (mc.thePlayer == null || disableLogger)
            return

        if (classProvider.isCPacketPlayer(packet)) // Cancel all movement stuff
            event.cancelEvent()

        if (classProvider.isCPacketPlayerPosition(packet) || classProvider.isCPacketPlayerPosLook(packet) ||
                classProvider.isCPacketPlayerBlockPlacement(packet) ||
                classProvider.isCPacketAnimation(packet) ||
                classProvider.isCPacketEntityAction(packet) || classProvider.isCPacketUseEntity(packet)) {
            event.cancelEvent()
            packets.add(packet)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val thePlayer = mc.thePlayer ?: return

        synchronized(positions) { positions.add(doubleArrayOf(thePlayer.posX, thePlayer.entityBoundingBox.minY, thePlayer.posZ)) }
        if (pulseValue.get() && pulseTimer.hasTimePassed(pulseDelayValue.get().toLong())) {
            blink()
            pulseTimer.reset()
        }
        val breadcrumbs = LiquidBounce.moduleManager.getModule(Breadcrumbs::class.java) as Breadcrumbs?
        breadcrumbs!!.state = true
    }

    override val tag: String
        get() = packets.size.toString()

    private fun blink() {
        try {
            disableLogger = true

            while (!packets.isEmpty()) {
                mc.netHandler.networkManager.sendPacket(packets.take())
            }

            disableLogger = false
        } catch (e: Exception) {
            e.printStackTrace()
            disableLogger = false
        }
        synchronized(positions) { positions.clear() }
    }
}