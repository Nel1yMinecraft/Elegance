/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.`fun`

import me.ccbluex.liquidbounce.api.minecraft.client.entity.player.WEnumPlayerModelParts
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.utils.timer.MSTimer
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.IntegerValue
import kotlin.random.Random

@ModuleInfo(name = "SkinDerp", description = "Makes your skin blink (Requires multi-layer skin).", category = ModuleCategory.FUN)
class SkinDerp : Module() {

    private val delayValue = IntegerValue("Delay", 0, 0, 1000)
    private val hatValue = BoolValue("Hat", true)
    private val jacketValue = BoolValue("Jacket", true)
    private val leftPantsValue = BoolValue("LeftPants", true)
    private val rightPantsValue = BoolValue("RightPants", true)
    private val leftSleeveValue = BoolValue("LeftSleeve", true)
    private val rightSleeveValue = BoolValue("RightSleeve", true)

    private var prevModelParts = emptySet<WEnumPlayerModelParts>()

    private val timer = MSTimer()

    override fun onEnable() {
        prevModelParts = mc.gameSettings.modelParts

        super.onEnable()
    }

    override fun onDisable() {
        // Disable all current model parts

        for (modelPart in mc.gameSettings.modelParts)
            mc.gameSettings.setModelPartEnabled(modelPart, false)

        // Enable all old model parts
        for (modelPart in prevModelParts)
            mc.gameSettings.setModelPartEnabled(modelPart, true)

        super.onDisable()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (timer.hasTimePassed(delayValue.get().toLong())) {
            if (hatValue.get())
                mc.gameSettings.setModelPartEnabled(WEnumPlayerModelParts.HAT, Random.nextBoolean())
            if (jacketValue.get())
                mc.gameSettings.setModelPartEnabled(WEnumPlayerModelParts.JACKET, Random.nextBoolean())
            if (leftPantsValue.get())
                mc.gameSettings.setModelPartEnabled(WEnumPlayerModelParts.LEFT_PANTS_LEG, Random.nextBoolean())
            if (rightPantsValue.get())
                mc.gameSettings.setModelPartEnabled(WEnumPlayerModelParts.RIGHT_PANTS_LEG, Random.nextBoolean())
            if (leftSleeveValue.get())
                mc.gameSettings.setModelPartEnabled(WEnumPlayerModelParts.LEFT_SLEEVE, Random.nextBoolean())
            if (rightSleeveValue.get())
                mc.gameSettings.setModelPartEnabled(WEnumPlayerModelParts.RIGHT_SLEEVE, Random.nextBoolean())
            timer.reset()
        }
    }

}
