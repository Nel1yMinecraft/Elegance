
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.enums.EnumFacingType
import me.ccbluex.liquidbounce.api.enums.WEnumHand
import me.ccbluex.liquidbounce.api.minecraft.item.IItem
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.*
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.utils.createUseItemPacket
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.IntegerValue
import me.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.*

@ModuleInfo(name = "NoSlowDown",description = "No-Slow",
    category = ModuleCategory.MOVEMENT)
class NoSlow : Module() {
    private val msTimer = MSTimer()
    private val modeValue =
        ListValue("PacketMode", arrayOf("Vanilla", "AAC", "AAC5", "HytPacket"), "Vanilla")
    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val customOnGround = BoolValue("CustomOnGround", false)
    private val customDelayValue = IntegerValue("CustomDelay", 60, 10, 200)

    // Soulsand
    val soulsandValue = BoolValue("Soulsand", false)

    var pendingFlagApplyPacket = false
    var blocking = false
    var c09paket = false

    override fun onDisable() {
        msTimer.reset()
        pendingFlagApplyPacket = false
    }

    private fun sendPacket(
        event: MotionEvent,
        sendC07: Boolean,
        sendC08: Boolean,
        delay: Boolean,
        delayValue: Long,
        onGround: Boolean
    ) {
        val digging = classProvider.createCPacketPlayerDigging(
            ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
            WBlockPos(-1, -1, -1),
            classProvider.getEnumFacing(EnumFacingType.DOWN)
        )
        val blockMain = createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.MAIN_HAND)
        val blockOFF = createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.OFF_HAND)
        if (onGround && !mc.thePlayer!!.onGround) {
            return
        }
        if (sendC07 && event.eventState == EventState.PRE) {
            if (delay && msTimer.hasTimePassed(delayValue)) {
                mc.netHandler.addToSendQueue(digging)
            } else if (!delay) {
                mc.netHandler.addToSendQueue(digging)
            }
        }
        if (sendC08 && event.eventState == EventState.POST) {
            if (delay && msTimer.hasTimePassed(delayValue)) {
                mc.netHandler.addToSendQueue(blockMain)
                mc.netHandler.addToSendQueue(blockOFF)

                msTimer.reset()
            } else if (!delay) {
                mc.netHandler.addToSendQueue(blockMain)
                mc.netHandler.addToSendQueue(blockOFF)

            }
        }
    }

    val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura

    fun isBlock(): Boolean {
        return mc.thePlayer!!.isBlocking || killAura.blockingStatus
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (!MovementUtils.isMoving) {
            return
        }

        when (modeValue.get().toLowerCase()) {
            "anticheat" -> {
                if (mc.thePlayer!!.isUsingItem || (mc.thePlayer!!.isBlocking || isBlock())) {
                    this.sendPacket(event, true, false, false, 0, false)
                    if (mc.thePlayer!!.ticksExisted % 2 == 0) {
                        this.sendPacket(event, false, true, false, 0, false)
                    }
                }

            }

            "aac" -> {
                if (mc.thePlayer!!.ticksExisted % 3 == 0) {
                    sendPacket(event, true, false, false, 0, false)
                } else {
                    sendPacket(event, false, true, false, 0, false)
                }
            }

            "aac5" -> {
                if (mc.thePlayer!!.isUsingItem || mc.thePlayer!!.isBlocking || isBlock()) {
                    mc.netHandler.addToSendQueue(
                        createUseItemPacket(
                            mc.thePlayer!!.inventory.getCurrentItemInHand(),
                            WEnumHand.MAIN_HAND
                        )
                    )
                    mc.netHandler.addToSendQueue(
                        createUseItemPacket(
                            mc.thePlayer!!.inventory.getCurrentItemInHand(),
                            WEnumHand.OFF_HAND
                        )
                    )
                }
            }

            "custom" -> {
                sendPacket(event, true, true, true, customDelayValue.get().toLong(), customOnGround.get())
            }

            "ncp" -> {
                sendPacket(event, true, true, false, 0, false)
            }

            "hytpacket" -> {
                consumeForwardMultiplier.set(0.2)
                consumeStrafeMultiplier.set(0.2)
                if (event.eventState == EventState.PRE &&  mc.thePlayer!!.isBlocking || isBlock() && blocking) {
                    mc.netHandler.addToSendQueue(
                        classProvider.createCPacketPlayerDigging(
                            ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                            WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)
                        )
                    )
                    mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.getCurrentItemInHand() as IItemStack))
                }
            }
        }
    }
    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer!!.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }
    @EventTarget
    fun onUpdate(event : UpdateEvent){
        if (msTimer.hasTimePassed(100)){
            c09paket = true
            msTimer.reset()
        }
    }
    private fun getMultiplier(item: IItem?, isForward: Boolean): Float {
        return when {
            classProvider.isItemFood(item) || classProvider.isItemPotion(item) || classProvider.isItemBucketMilk(item) -> {
                if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
            }
            classProvider.isItemSword(item) -> {
                if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
            }
            classProvider.isItemBow(item) -> {
                if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
            }
            else -> 0.2F
        }
    }
    override val tag: String
        get() = modeValue.get()
}