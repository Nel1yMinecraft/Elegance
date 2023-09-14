/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat

import me.nelly.PacketUtils
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import me.ccbluex.liquidbounce.event.AttackEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.utils.Rotation
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.IntegerValue
import me.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.CPacketEntityAction

@ModuleInfo(name = "MoreKb", category = ModuleCategory.COMBAT, description = "Superkb")
class SuperKnockback : Module() {
    private val onlyplayer = BoolValue("OnlyPlayer", true)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val modeValue = ListValue("Mode", arrayOf("Wtap", "Legit", "Silent", "SprintReset", "SneakPacket"), "Silent")
    private val onlyMoveValue = BoolValue("OnlyMove", true)
    private val onlyMoveForwardValue = BoolValue("OnlyMoveForward", true)
    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val delayValue = IntegerValue("Delay", 0, 0, 500)

    private var ticks = 0

    val timer = MSTimer()

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (event.targetEntity is IEntityLivingBase && !onlyplayer.get()) {
            if (event.targetEntity.hurtTime > hurtTimeValue.get() || !timer.hasTimePassed(delayValue.get().toLong()) ||
                (!MovementUtils.isMoving && onlyMoveValue.get()) || (!mc.thePlayer!!.onGround && onlyGroundValue.get())
            ) {
                return
            }

            if (onlyMoveForwardValue.get() && RotationUtils.getRotationDifference(
                    Rotation(
                        MovementUtils.movingYaw,
                        mc.thePlayer!!.rotationPitch
                    ), Rotation(mc.thePlayer!!.rotationYaw, mc.thePlayer!!.rotationPitch)
                ) > 35
            ) {
                return
            }

            when (modeValue.get().toLowerCase()) {

                "wtap" -> ticks = 2


                "legit" -> {
                    ticks = 2
                }

                "silent" -> {
                    ticks = 1
                }

                "sprintreset" -> {
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                }

                "sneakpacket" -> {
                    if (mc.thePlayer!!.sprinting) {
                        mc.thePlayer!!.sprinting = true
                    }
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SNEAKING))
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SNEAKING))
                    mc.thePlayer!!.serverSprintState = true
                }
            }
            timer.reset()
        }
        if (event.targetEntity is IEntityLivingBase) {
            if (event.targetEntity.hurtTime > hurtTimeValue.get() || !timer.hasTimePassed(delayValue.get().toLong()) ||
                (!MovementUtils.isMoving && onlyMoveValue.get()) || (!mc.thePlayer!!.onGround && onlyGroundValue.get())
            ) {
                return
            }

            if (onlyMoveForwardValue.get() && RotationUtils.getRotationDifference(
                    Rotation(
                        MovementUtils.movingYaw,
                        mc.thePlayer!!.rotationPitch
                    ), Rotation(mc.thePlayer!!.rotationYaw, mc.thePlayer!!.rotationPitch)
                ) > 35
            ) {
                return
            }

            when (modeValue.get().toLowerCase()) {

                "wtap" -> ticks = 2


                "legit" -> {
                    ticks = 2
                }

                "silent" -> {
                    ticks = 1
                }

                "sprintreset" -> {
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                }

                "sneakpacket" -> {
                    if (mc.thePlayer!!.sprinting) {
                        mc.thePlayer!!.sprinting = true
                    }
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SNEAKING))
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                    PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SNEAKING))
                    mc.thePlayer!!.serverSprintState = true
                }
            }
            timer.reset()
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (modeValue.equals("Legit")) {
            if (ticks == 2) {
                mc.gameSettings.keyBindForward.pressed = false
                ticks = 1
            } else if (ticks == 1) {
                mc.gameSettings.keyBindForward.pressed = true
                ticks = 0
            }
        }
        if (modeValue.equals("Wtap")) {
            if (ticks == 2) {
                mc.thePlayer!!.sprinting = false
                ticks = 1
            } else if (ticks == 1) {
                mc.thePlayer!!.sprinting = true
                ticks = 0
            }
        }
        if (modeValue.equals("Silent")) {
            if (ticks == 1) {
                PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.STOP_SPRINTING))
                ticks = 2
            } else if (ticks == 2) {
                PacketUtils.send(CPacketEntityAction(mc2.player, CPacketEntityAction.Action.START_SPRINTING))
                ticks = 0
            }
        }
    }

    override val tag: String
        get() = modeValue.get()
}