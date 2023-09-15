/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package me.nelly.fdp.module

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.event.AttackEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
import me.ccbluex.liquidbounce.features.module.modules.misc.Teams
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.*
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.SPacketCustomPayload
import org.lwjgl.input.Mouse

@ModuleInfo(name = "AutoReport", category = ModuleCategory.PLAYER, description = "Fix by nelly")
class AutoReport : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Hit", "All"), "Hit")
    private val commandValue = TextValue("Command", "/report %name%")
    private val tipValue = BoolValue("Tip", true)
    private val allDelayValue = IntegerValue("AllDelay", 10000, 1, 60000)
    private val blockBooksValue = BoolValue("BlockBooks", false) // 绕过Hypixel /report举报弹出书
    private val bypasshytbox = BoolValue("BypassHytBox", false) // 绕过花雨庭 /report举报弹出菜单(箱子)
    private val reported = mutableListOf<String>()
    private val delayTimer = MSTimer()

    override fun onEnable() {
        reported.clear()
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val entity = event.targetEntity ?: return
        if (isTarget(entity)) {
            doReport(entity as EntityPlayer)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (modeValue.equals("All") && delayTimer.hasTimePassed(allDelayValue.get().toLong())) {
            mc.netHandler.playerInfoMap.forEach {
                val name = it.gameProfile.name
                if (name != mc.session.username && !isFriend(name)) {
                    if (doReport(name) && allDelayValue.get() != 0) {
                        return@forEach
                    }
                }
            }
            delayTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (blockBooksValue.get() && event.packet.unwrap() is SPacketCustomPayload) {
            event.cancelEvent()
        }
        if (bypasshytbox.get() && event.packet.unwrap() is SPacketCustomPayload) {
            val guiContainer = mc.currentScreen
            if (guiContainer is GuiChest) {
                val container = guiContainer.inventorySlots

                if (Mouse.isButtonDown(0)) {
                    for (slot in container.inventorySlots) {
                        mc.playerController.windowClick(
                            container.windowId,
                            slot.slotNumber,
                            0,
                            0,
                            mc.thePlayer!!
                        )
                    }
                }
            }
        }
    }

    fun doReport(player: EntityPlayer) = doReport(player.name)

    fun doReport(name: String): Boolean {
        // pass this if reported
        if (reported.contains(name)) {
            return false
        }

        reported.add(name)
        mc.thePlayer!!.sendChatMessage(commandValue.get().replace("%name%", name))
        if (tipValue.get()) {
            ClientUtils.displayChatMessage("$name reported!")
        }
        return true
    }

    private fun isTarget(entity: IEntity): Boolean {
        if (entity is EntityPlayer) {
            if (entity == mc.thePlayer) {
                return false
            }

            if (AntiBot.isBot(entity.asEntityLivingBase())) {
                return false
            }

            if (isFriend(entity.name)) {
                return false
            }

            if (entity.isSpectator) {
                return false
            }

            val teams = LiquidBounce.moduleManager[Teams::class.java] as Teams
            return !teams.state || !teams.isInYourTeam(entity.asEntityLivingBase())
        }

        return false
    }

    private fun isFriend(name: String?): Boolean {
        return LiquidBounce.fileManager.friendsConfig.isFriend(name)
    }

    override val tag: String
        get() = modeValue.get()
}