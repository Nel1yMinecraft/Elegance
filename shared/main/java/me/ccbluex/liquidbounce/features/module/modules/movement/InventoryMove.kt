/*
 * LiquidBounce Base
 * God SkidBounce
 * Conding
 */
package me.ccbluex.liquidbounce.features.module.modules.movement

import me.ccbluex.liquidbounce.event.ClickWindowEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.MovementUtils
import me.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "InvMove", description = "Allows you to walk while an inventory is opened.", category = ModuleCategory.MOVEMENT)
class InventoryMove : Module() {

    private val undetectable = BoolValue("Undetectable", false)
    val aacAdditionProValue = BoolValue("AACAdditionPro", false)
    private val noMoveClicksValue = BoolValue("NoMoveClicks", false)

    private val affectedBindings = arrayOf(
        mc.gameSettings.keyBindForward,
        mc.gameSettings.keyBindBack,
        mc.gameSettings.keyBindRight,
        mc.gameSettings.keyBindLeft,
        mc.gameSettings.keyBindJump,
        mc.gameSettings.keyBindSprint
    )

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        tick()
    }

    fun tick() {
        if (!classProvider.isGuiChat(mc.currentScreen) && !classProvider.isGuiIngameMenu(mc.currentScreen) && (!undetectable.get() || !classProvider.isGuiContainer(mc.currentScreen))) {
            for (affectedBinding in affectedBindings) {
                affectedBinding.pressed = mc.gameSettings.isKeyDown(affectedBinding)
            }
        }
    }

    @EventTarget
    fun onClick(event: ClickWindowEvent) {
        if (noMoveClicksValue.get() && MovementUtils.isMoving)
            event.cancelEvent()
    }

    override fun onDisable() {
        val isIngame = mc.currentScreen != null

        for (affectedBinding in affectedBindings) {
            if (!mc.gameSettings.isKeyDown(affectedBinding) || isIngame)
                affectedBinding.pressed = false
        }
    }

    override val tag: String?
        get() = if (aacAdditionProValue.get()) "AACAdditionPro" else null
}
