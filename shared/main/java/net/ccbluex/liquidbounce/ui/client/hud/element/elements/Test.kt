package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import me.nelly.NellyUtils
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11

@ElementInfo(name = "Test")
class Test : Element() {
    override fun drawElement(): Border {
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_BLEND)
        NellyUtils.drawRect(50f, 50f, 100f, 100f, 10f, 0.2f, 1f, 1f, 0f, 0f, 0.5f, true, 2f, 1f, 1f, 1f, 1f)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        return Border(50f, 50f, 100f, 100f)
    }
}