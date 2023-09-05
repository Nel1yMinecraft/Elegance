package me.ccbluex.liquidbounce.features.module.modules.render

import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
import org.lwjgl.input.Keyboard

@ModuleInfo(
    name = "HudDesigner", description = "HudDesigner",
    category = ModuleCategory.RENDER,
    canEnable = false,
    keyBind = Keyboard.KEY_N
)
class HudDesigner : Module() {
    override fun onEnable() {
        mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiHudDesigner()))
    }
}