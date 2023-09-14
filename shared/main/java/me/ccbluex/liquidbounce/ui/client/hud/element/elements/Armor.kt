/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.ui.client.hud.element.elements

import me.ccbluex.liquidbounce.api.enums.MaterialType
import me.ccbluex.liquidbounce.ui.client.hud.element.Border
import me.ccbluex.liquidbounce.ui.client.hud.element.Element
import me.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import me.ccbluex.liquidbounce.ui.client.hud.element.Side
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.FloatValue
import me.ccbluex.liquidbounce.value.IntegerValue
import me.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@ElementInfo(name = "Armor")
class Armor(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F,
            side: Side = Side(Side.Horizontal.MIDDLE, Side.Vertical.DOWN)) : Element(x, y, scale, side) {
    val uimode = ListValue("UI", arrayOf("LB", "Tomk"), "LB")
    private val modeValue = ListValue("Alignment", arrayOf("Horizontal", "Vertical"), "Horizontal")
    private val radiusValue = FloatValue("Radius", 4.25f, 0f, 10f)
    private val FShadow = BoolValue("Font-Shadow", true)
    private val alphaValue = IntegerValue("Alpha", 120, 0, 255)

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        if (uimode.contains("LB")) {
            if (mc.playerController.isNotCreative) {
                GL11.glPushMatrix()

                val renderItem = mc.renderItem
                val isInsideWater = mc.thePlayer!!.isInsideOfMaterial(classProvider.getMaterialEnum(MaterialType.WATER))

                var x = 1
                var y = if (isInsideWater) -10 else 0

                val mode = modeValue.get()

                for (index in 3 downTo 0) {
                    val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue

                    renderItem.renderItemIntoGUI(stack, x, y)
                    renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
                    if (mode.equals("Horizontal", true))
                        x += 18
                    else if (mode.equals("Vertical", true))
                        y += 18
                }

                classProvider.getGlStateManager().enableAlpha()
                classProvider.getGlStateManager().disableBlend()
                classProvider.getGlStateManager().disableLighting()
                classProvider.getGlStateManager().disableCull()
                GL11.glPopMatrix()
            }
        }
        if (uimode.contains("Tomk")) {
            GL11.glPushMatrix()

            val renderItem = mc.renderItem
            val isInsideWater = mc.thePlayer!!.isInsideOfMaterial(classProvider.getMaterialEnum(MaterialType.WATER))

            var x = 1
            var y = if (isInsideWater) -10 else 0

            val mode = modeValue.get()

            //Rect
            RenderUtils.drawRoundedRect(
                0F,
                0F,
                50F,
                76F,
                radiusValue.get().toInt(),
                Color(11, 11, 12, this.alphaValue.get()).rgb
            )
            RenderUtils.drawRoundedRect(
                x + 22f,
                y + 4f,
                x + 21f,
                y + 16.5f,
                0,
                Color(100, 100, 101, this.alphaValue.get() + 20).rgb
            )
            RenderUtils.drawRoundedRect(
                x + 22f,
                y + 23.5f,
                x + 21f,
                y + 35.5f,
                0,
                Color(100, 100, 101, this.alphaValue.get() + 20).rgb
            )
            RenderUtils.drawRoundedRect(
                x + 22f,
                y + 41.5f,
                x + 21f,
                y + 53.5f,
                0,
                Color(100, 100, 101, this.alphaValue.get() + 20).rgb
            )
            RenderUtils.drawRoundedRect(
                x + 22f,
                y + 60.5f,
                x + 21f,
                y + 72.5f,
                0,
                Color(100, 100, 101, this.alphaValue.get() + 20).rgb
            )
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glScalef(1F, 1F, 1F)
            GL11.glPushMatrix()
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.fastRoundedRect(0F, 0F, 50F, 76F, radiusValue.get())
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()

        }

        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)

        Gui.drawRect(0, 0, 0, 0, -1)
        for (index in 3 downTo 0) {
            val stack = mc.thePlayer!!.inventory.armorInventory[index] ?: continue

            mc.renderItem.renderItemAndEffectIntoGUI(stack, (x + 3).toInt(), (y + 3).toInt())
            val itemDamage = stack.maxDamage - stack.itemDamage
            GlStateManager.pushMatrix()
            GlStateManager.scale(0.5F, 0.5F, 0.5F)
            Gui.drawRect(0, 0, 0, 0, -1)
            GlStateManager.popMatrix()
            var ms = Math.round(itemDamage * 1f / stack.maxDamage * 100f).toFloat()
            var s = StringBuilder().append(DecimalFormat().format(java.lang.Float.valueOf(ms))).append("%")
                .toString()
            Fonts.font25.drawString(s, (x + 26).toFloat(), (y + 6.7).toFloat(), -1, FShadow.get())
            //Rect Shadow
            RenderUtils.drawRound(
                ((((x + 25f).toFloat()))), (y + 13.5f).toFloat(), (itemDamage * 1f / stack.maxDamage * 20f), 1.0f, 2.5f,
                Color(255, 255, 255)
            )
            RenderUtils.drawRound(
                (((x + 25f).toFloat())), (y + 13.8f).toFloat(), (itemDamage * 1f / stack.maxDamage * 20f), 1.1f, 2.5f,
                Color(255, 255, 255, 210)
            )
            RenderUtils.drawRound(
                (x + 25.3f).toFloat(), (y + 13.5f).toFloat(), (itemDamage * 1f / stack.maxDamage * 20f), 1.1f, 2.5f,
                Color(255, 255, 255, 210)
            )
            y += 18
        }

        classProvider.getGlStateManager().enableAlpha()
        classProvider.getGlStateManager().disableBlend()
        classProvider.getGlStateManager().disableLighting()
        classProvider.getGlStateManager().disableCull()
        GL11.glPopMatrix()

        return if (modeValue.get().equals("Horizontal", true))
            Border(0F, 0F, 72F, 17F)
        else
            Border(0F, 0F, 18F, 72F)
    }
}