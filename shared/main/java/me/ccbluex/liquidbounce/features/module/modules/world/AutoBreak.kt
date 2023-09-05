/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.world

import me.ccbluex.liquidbounce.api.enums.BlockType
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "AutoBreak", description = "Automatically breaks the block you are looking at.", category = ModuleCategory.WORLD)
class AutoBreak : Module() {

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.objectMouseOver == null || mc.objectMouseOver!!.blockPos == null || mc.theWorld == null)
            return

        mc.gameSettings.keyBindAttack.pressed = mc.theWorld!!.getBlockState(mc.objectMouseOver!!.blockPos!!).block != classProvider.getBlockEnum(BlockType.AIR)
    }

    override fun onDisable() {
        if (!mc.gameSettings.isKeyDown(mc.gameSettings.keyBindAttack))
            mc.gameSettings.keyBindAttack.pressed = false
    }
}
