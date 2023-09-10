package me.ccbluex.liquidbounce.features.module.modules.render

import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.client.gui.GuiOverlayDebug
import net.minecraftforge.client.event.GuiScreenEvent
@ModuleInfo("NoMCScoreboard","Nel1y",ModuleCategory.RENDER)
class NoMCScoreboard : Module(){
    @EventTarget
    fun onUpdate(event: GuiScreenEvent.DrawScreenEvent.Post) {
        if (event.gui is GuiOverlayDebug) {
            event.isCanceled = true
        }
    }
}
