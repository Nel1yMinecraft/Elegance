/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.player

import me.ccbluex.liquidbounce.api.enums.BlockType
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "Eagle", description = "Makes you eagle (aka. FastBridge).", category = ModuleCategory.PLAYER)
class Eagle : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        mc.gameSettings.keyBindSneak.pressed = mc.theWorld!!.getBlockState(WBlockPos(thePlayer.posX, thePlayer.posY - 1.0, thePlayer.posZ)).block == classProvider.getBlockEnum(BlockType.AIR)
    }

    override fun onDisable() {
        if (mc.thePlayer == null)
            return

        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak))
            mc.gameSettings.keyBindSneak.pressed = false
    }
}
