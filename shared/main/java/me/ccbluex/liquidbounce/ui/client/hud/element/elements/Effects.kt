package me.ccbluex.liquidbounce.ui.client.hud.element.elements

import me.ccbluex.liquidbounce.api.minecraft.potion.IPotion
import me.ccbluex.liquidbounce.ui.client.hud.element.Border
import me.ccbluex.liquidbounce.ui.client.hud.element.Element
import me.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.ccbluex.liquidbounce.utils.HanaBiColors
import me.ccbluex.liquidbounce.utils.Translate
import me.ccbluex.liquidbounce.utils.blur.BlurBuffer
import me.ccbluex.liquidbounce.utils.render.PotionData
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*
import kotlin.math.max


@ElementInfo(name = "Effects")
class Effects : Element() {
    private val potionMap = HashMap<IPotion, PotionData?>()

    /**
     * Draw the entity.
     */
    override fun drawElement(): Border {
        GlStateManager.pushMatrix()
        var y = 0
        for (potionEffect in mc.thePlayer!!.activePotionEffects) {
            val potion: IPotion = functions.getPotionById(potionEffect.potionID)
            val name: String = functions.formatI18n(potion.name)
            var potionData: PotionData?
            if (potionMap.containsKey(potion) && potionMap[potion]!!.level === potionEffect.amplifier) potionData =
                potionMap[potion] else potionMap[potion] =
                PotionData(potion, Translate(0f, -40f + y), potionEffect.amplifier).also({ potionData = it })
            var flag = true
            for (checkEffect in mc.thePlayer!!.activePotionEffects) {
                if (checkEffect.amplifier == potionData!!.level) {
                    flag = false
                    break
                }
            }
            if (flag) potionMap.remove(potion)
            var potionTime: Int
            var potionMaxTime: Int
            try {
                potionTime = potionEffect.getDurationString().split(":").get(0).toInt()
                potionMaxTime = potionEffect.getDurationString().split(":").get(1).toInt()
            } catch (ignored: Exception) {
                potionTime = 100
                potionMaxTime = 1000
            }
            val lifeTime = potionTime * 60 + potionMaxTime
            if (potionData!!.getMaxTimer() === 0 || lifeTime > potionData!!.getMaxTimer() as Double) potionData!!.maxTimer =
                lifeTime
            var state = 0.0f
            if (lifeTime >= 0.0) state = (lifeTime / (potionData!!.getMaxTimer() as Float).toDouble() * 100.0).toFloat()
            val position = Math.round(potionData!!.translate.getY() + 5)
            state = max(state.toDouble(), 2.0).toFloat()
            potionData!!.translate.interpolate(0f, y.toFloat(), 0.1)
            potionData!!.animationX =
                RenderUtils.getAnimationState(
                    potionData!!.getAnimationX().toDouble(),
                    1.2f * state.toDouble(),
                    (Math.max(10.0f, Math.abs(potionData!!.animationX - 1.2f * state) * 15.0f) * 0.3f).toDouble()
                ).toFloat()
            RenderUtils.drawRectPotion(
                0f,
                potionData!!.translate.getY(),
                120f,
                potionData!!.translate.y + 30f,
                ClientUtils.reAlpha(HanaBiColors.GREY.c, 0.1f)
            )
            RenderUtils.drawRectPotion(
                0f,
                potionData!!.translate.getY(),
                potionData!!.animationX,
                potionData!!.translate.getY() + 30f,
                ClientUtils.reAlpha(
                    Color(34, 24, 20).brighter().rgb, 0.3f
                )
            )
            RenderUtils.drawShadowWithCustomAlpha(0f, Math.round(potionData!!.translate.getY()).toFloat(), 120f, 30f, 200f)
            if (blur.get()) {
                GL11.glTranslated(-renderX, -renderY, 0.0)
                BlurBuffer.blurArea(
                    renderX.toFloat(),
                    renderY.toFloat() + Math.round(potionData!!.translate.getY()),
                    120f,
                    30f
                )
                GL11.glTranslated(renderX, renderY, 0.0)
            }
            val posY: Float = potionData!!.translate.getY() + 13f
            Fonts.font35.drawString(
                name + " " + intToRomanByGreedy(potionEffect.amplifier + 1),
                29f,
                posY - mc.fontRendererObj.fontHeight,
                ClientUtils.reAlpha(HanaBiColors.WHITE.c, 0.8f)
            )
            Fonts.font35.drawString(
                potionEffect.getDurationString(),
                29f,
                posY + 4.0f,
                ClientUtils.reAlpha(Color(200, 200, 200).rgb, 0.5f)
            )
            if (potion.hasStatusIcon) {
                GlStateManager.pushMatrix()
                GL11.glDisable(2929)
                GL11.glEnable(3042)
                GL11.glDepthMask(false)
                OpenGlHelper.glBlendFunc(770, 771, 1, 0)
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                val statusIconIndex: Int = potion.statusIconIndex
                mc.textureManager
                    .bindTexture(classProvider.createResourceLocation("textures/gui/container/inventory.png"))
                mc2.ingameGUI.drawTexturedModalRect(
                    6f,
                    (position + 40).toFloat(),
                    statusIconIndex % 8 * 18,
                    198 + statusIconIndex / 8 * 18,
                    18,
                    18
                )
                GL11.glDepthMask(true)
                GL11.glDisable(3042)
                GL11.glEnable(2929)
                GlStateManager.popMatrix()
            }
            y -= 35
        }
        GlStateManager.popMatrix()
        return Border(0f, 0f, 120f, 30f)
    }

    private fun intToRomanByGreedy(num: Int): String {
        var num = num
        val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
        val symbols = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
        val stringBuilder = StringBuilder()
        var i = 0
        while (i < values.size && num >= 0) {
            while (values[i] <= num) {
                num -= values[i]
                stringBuilder.append(symbols[i])
            }
            i++
        }
        return stringBuilder.toString()
    }

    companion object {
        private val blur: BoolValue = BoolValue("Blur", false)
    }
}
