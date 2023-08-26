/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/liquidbounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState.*
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.EaseUtils
import net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider
import net.ccbluex.liquidbounce.utils.blur.BlurUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.ShadowRenderUtils
import net.ccbluex.liquidbounce.utils.render.Stencil
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max


/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications")
class Notifications(
    x: Double = 0.0,
    y: Double = 0.0,
    scale: Float = 1F,
    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)
) : Element(x, y, scale, side) {


    private val backGroundAlphaValue = IntegerValue("BackGroundAlpha", 170, 0, 255)
    private val titleShadow = BoolValue("TitleShadow", false)
    private val contentShadow = BoolValue("ContentShadow", true)
    private val whiteText = BoolValue("WhiteTextColor", true)
    private val modeColored = BoolValue("CustomModeColored", true)
    private val blurValue = FloatValue("Blur", 1F, 0.1F, 10F)

    companion object {
        val styleValue = ListValue("Mode", arrayOf("Classic", "FDP", "Modern", "Tenacity", "Skid"), "Modern")
    }

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Notification", "This is an example notification.", NotifyType.INFO)

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        // bypass java.util.ConcurrentModificationException
        LiquidBounce.hud.notifications.map { it }.forEachIndexed { index, notify ->
            GL11.glPushMatrix()

            if (notify.drawNotification(
                    index,
                    Fonts.C32,
                    backGroundAlphaValue.get(),
                    blurValue.get(),
                    this.renderX.toFloat(),
                    this.renderY.toFloat(),
                    scale,
                    contentShadow.get(),
                    titleShadow.get(),
                    whiteText.get(),
                    modeColored.get(),
                    Companion
                )
            ) {
                LiquidBounce.hud.notifications.remove(notify)
            }

            GL11.glPopMatrix()
        }

        if (classProvider.isGuiHudDesigner(mc.currentScreen)) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification)) {
                LiquidBounce.hud.addNotification(exampleNotification)
            }

            exampleNotification.fadeState = STAY
            exampleNotification.displayTime = System.currentTimeMillis()

            return Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(), 0F, 0F)
        }

        return null
    }

}


class Notification(
    val title: String,
    val content: String,
    val type: NotifyType,
    val time: Int = 1500,
    private val animeTime: Int = 500
) {
    var width = 100
    val height = 30

    private val classicHeight = 30
    var x = 0F
    private var textLengthtitle = 0
    private var textLengthcontent = 0
    private var textLength = 0f

    init {
        textLengthtitle = Fonts.font35.getStringWidth(title)
        textLengthcontent = Fonts.font35.getStringWidth(content)
        textLength = textLengthcontent.toFloat() + textLengthtitle.toFloat()
    }

    var fadeState = IN
    private var nowY = -height
    var displayTime = System.currentTimeMillis()
    private var animeXTime = System.currentTimeMillis()
    private var animeYTime = System.currentTimeMillis()

    /**
     * Draw notification
     */
    fun drawNotification(
        index: Int, font: IFontRenderer, alpha: Int, blurRadius: Float, x: Float, y: Float, scale: Float,
        contentShadow: Boolean,
        titleShadow: Boolean,
        whiteText: Boolean,
        modeColored: Boolean,
        parent: Notifications.Companion

    ): Boolean {
        this.width = 100.coerceAtLeast(
            font.getStringWidth(content)
                .coerceAtLeast(font.getStringWidth(title)) + 15
        )
        val realY = -(index + 1) * height
        val nowTime = System.currentTimeMillis()
        var transY = nowY.toDouble()
        font.getStringWidth("$title: $content")

        val textColor: Int = if (whiteText) {
            Color(255, 255, 255).rgb
        } else {
            Color(10, 10, 10).rgb
        }

        // Y-Axis Animation
        if (nowY != realY) {
            var pct = (nowTime - animeYTime) / animeTime.toDouble()
            if (pct > 1) {
                nowY = realY
                pct = 1.0
            } else {
                pct = EaseUtils.easeOutExpo(pct)
            }
            transY += (realY - nowY) * pct
        } else {
            animeYTime = nowTime
        }

        // X-Axis Animation
        var pct = (nowTime - animeXTime) / animeTime.toDouble()
        when (fadeState) {
            IN -> {
                if (pct > 1) {
                    fadeState = STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = EaseUtils.easeOutExpo(pct)
            }

            STAY -> {
                pct = 1.0
                if ((nowTime - animeXTime) > time) {
                    fadeState = OUT
                    animeXTime = nowTime
                }
            }

            OUT -> {
                if (pct > 1) {
                    fadeState = END
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = 1 - EaseUtils.easeInExpo(pct)
            }

            END -> {
                return true
            }
        }
        val transX = width - (width * pct) - width
        GL11.glTranslated(transX, transY, 0.0)
        // draw notify
        val style = parent.styleValue.get()
        val nTypeWarning = type.renderColor == Color(0xF5FD00)
        val nTypeInfo = type.renderColor == Color(0x6490A7)
        val nTypeSuccess = type.renderColor == Color(0x60E092)
        val nTypeError = type.renderColor == Color(0xFF2F2F)


        if (style.equals("Modern")) {

            if (blurRadius != 0f) {
                BlurUtils.draw(
                    4 + (x + transX).toFloat() * scale,
                    (y + transY).toFloat() * scale,
                    (width * scale),
                    (27f - 5f) * scale,
                    blurRadius
                )
            }
            var colorRed = type.renderColor.red
            var colorGreen = type.renderColor.green
            var colorBlue = type.renderColor.blue

            if (modeColored) {
                //success
                if (colorRed == 60) colorRed = 36
                if (colorGreen == 224) colorGreen = 211
                if (colorBlue == 92) colorBlue = 99

                //error
                if (colorRed == 255) colorRed = 248
                if (colorGreen == 47) colorGreen = 72
                if (colorBlue == 47) colorBlue = 72

                //warning
                if (colorRed == 245) colorRed = 251
                if (colorGreen == 253) colorGreen = 189
                if (colorBlue == 0) colorBlue = 23

                //info
                if (colorRed == 64) colorRed = 242
                if (colorGreen == 90) colorGreen = 242
                if (colorBlue == 167) colorBlue = 242
            }


            val colors = Color(colorRed, colorGreen, colorBlue, alpha / 3)

                RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)

            RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
            ShadowRenderUtils.drawShadowWithCustomAlpha(3f, 0F, width.toFloat() - 3f, 27f - 5f, 240f)
            RenderUtils.drawRoundedCornerRect(
                3f,
                0F,
                max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)) + 5f, 0F),
                27f - 5f,
                2f,
                Color(0, 0, 0, 26).rgb
            )
            Fonts.C27.drawString(title, 5.5F, 3.5F, textColor, titleShadow)
            font.drawString(content, 5.5F, 11.5F, textColor, contentShadow)
            return false
        }

        if (style.equals("FDP")) {

            if (blurRadius != 0f) {
                BlurUtils.draw(
                    4 + (x + transX).toFloat() * scale,
                    (y + transY).toFloat() * scale,
                    (width * scale),
                    (27f - 5f) * scale,
                    blurRadius
                )
            }

            val colors = Color(0, 0, 0, alpha / 4)

                when (fadeState) {
                    IN -> {
                        RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                        RenderUtils.drawRoundedCornerRect(3F, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                    }

                    STAY -> {
                        RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                        RenderUtils.drawRoundedCornerRect(3F, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                    }

                    OUT -> {
                        RenderUtils.drawRoundedCornerRect(4F, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                        RenderUtils.drawRoundedCornerRect(5F, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                    }

                    END -> return false
                }
                RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
                RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
            RenderUtils.drawRoundedCornerRect(3f, 0F, width.toFloat(), 27f - 5f, 2f, colors.rgb)
            ShadowRenderUtils.drawShadowWithCustomAlpha(3f, 0F, width.toFloat() - 3f, 27f - 5f, 240f)
            RenderUtils.drawRoundedCornerRect(
                3f,
                0F,
                max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)) + 5f, 0F),
                27f - 5f,
                2f,
                Color(0, 0, 0, 40).rgb
            )
            Fonts.C27.drawString(title, 5.5F, 3.5F, textColor, titleShadow)
            font.drawString(content, 5.5F, 11.5F, textColor, contentShadow)
            return false
        }

        // lbtl means liquidbounce text length
        /* if(style.equals("LiquidBounce")) {
            RenderUtils.drawRect(-1F, 0F, lbtl + 9F, -20F, Color(0, 0, 0, alpha))
            Fonts.C25.DisplayFont2(Fonts.C25, title + ": " + content, -4F, 3F, textColor, titleShadow)
            RenderUtils.drawRect(-1F + max(lbtl + 5F - (lbtl+ 5F) * ((nowTime - displayTime) / (animeTime * 2F + time)), 0F), 0F, 4F + max(lbtl + 5F - (lbtl+ 5F) * ((nowTime - displayTime) / (animeTime * 2F + time)), 0F), -20F, Color(0, 0, 0, alpha))
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        } yo so dg, WTF */


        /*
        if(style.equals("Simple")) {
            RenderUtils.customRoundedinf(-x + 8F + lbtl, -y, -x - 2F, -18F - y, 0F, 3F, 3F, 0F, Color(0,0,0, alpha).rgb)
            RenderUtils.customRoundedinf(-x - 2F, -y, -x - 5F, -18F - y, 3F, 0F, 0F, 3F, type.renderColor)
            Fonts.font40.drawString("$title: $content", -x + 3, -13F - y, -1)
            }  */

        if (style.equals("Skid")) {

            val colors = Color(type.renderColor.red, type.renderColor.green, type.renderColor.blue, alpha / 3)
            ShadowRenderUtils.drawShadowWithCustomAlpha(2f, 0F, width.toFloat(), 27f - 5f, 250f) // oops
            RenderUtils.drawRect(2F, 0F, 4F, 27f - 5F, colors.rgb)
            RenderUtils.drawRect(3F, 0F, width.toFloat() + 5f, 27f - 5f, Color(0, 0, 0, 150))
            RenderUtils.drawGradientSidewaysH(3.0, 0.0, 20.0, 27f - 5.0, colors.rgb, Color(0, 0, 0, 0).rgb)
            RenderUtils.drawRect(
                2f,
                27f - 6f,
                max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)) + 5f, 0F),
                27f - 5f,
                Color(52, 97, 237).rgb
            )

            Fonts.C27.drawString(title, 6.5F, 3.2F, textColor, titleShadow)
            font.drawString(content, 6.5F, 8.7F, textColor, contentShadow)
            return false
        }

        if (style.equals("Tenacity")) {
            val fontRenderer = Fonts.font35
            val thisWidth = 100.coerceAtLeast(
                fontRenderer.getStringWidth(this.title).coerceAtLeast(fontRenderer.getStringWidth(this.content)) + 40
            )
            val error =
                classProvider.createResourceLocation("liquidbounce/notifications/tenacity/cross.png")
            val successful =
                classProvider.createResourceLocation("liquidbounce/notifications/tenacity/tick.png")
            val warn =
                classProvider.createResourceLocation("liquidbounce/notifications/tenacity/warning.png")
            val info =
                classProvider.createResourceLocation("liquidbounce/notifications/tenacity/info.png")
            when (type.renderColor) {
                Color(0xFF2F2F) -> {
                    RenderUtils.drawRoundedCornerRect(
                        -18F,
                        1F,
                        thisWidth.toFloat(),
                        height.toFloat() - 2F,
                        5f,
                        Color(180, 0, 0, 190).rgb
                    )
                    RenderUtils.drawImage(error, -13, 5, 18, 18)
                    Fonts.font35.drawString(title, 9F, 16F, Color(255, 255, 255, 255).rgb)
                    Fonts.font40.drawString(content, 9F, 6F, Color(255, 255, 255, 255).rgb)
                }

                Color(0x60E092) -> {
                    RenderUtils.drawRoundedCornerRect(
                        -16F,
                        1F,
                        thisWidth.toFloat(),
                        height.toFloat() - 2F,
                        5f,
                        Color(0, 180, 0, 190).rgb
                    )
                    RenderUtils.drawImage(successful, -13, 5, 18, 18)
                    Fonts.font35.drawString(title, 9F, 16F, Color(255, 255, 255, 255).rgb)
                    Fonts.font40.drawString(content, 9F, 6F, Color(255, 255, 255, 255).rgb)
                }

                Color(0xF5FD00) -> {
                    RenderUtils.drawRoundedCornerRect(
                        -16F,
                        1F,
                        thisWidth.toFloat(),
                        height.toFloat() - 2F,
                        5f,
                        Color(0, 0, 0, 190).rgb
                    )
                    RenderUtils.drawImage(warn, -13, 5, 18, 18)
                    Fonts.font35.drawString(title, 9F, 16F, Color(255, 255, 255, 255).rgb)
                    Fonts.font40.drawString(content, 9F, 6F, Color(255, 255, 255, 255).rgb)
                }

                else -> {
                    RenderUtils.drawRoundedCornerRect(
                        -16F,
                        1F,
                        thisWidth.toFloat(),
                        height.toFloat() - 2F,
                        5f,
                        Color(0, 0, 0, 190).rgb
                    )
                    RenderUtils.drawImage(info, -13, 5, 18, 18)
                    Fonts.font35.drawString(title, 9F, 16F, Color(255, 255, 255, 255).rgb)
                    Fonts.font40.drawString(content, 9F, 6F, Color(255, 255, 255, 255).rgb)
                }
            }
            return false
        }

        if (style.equals("Classic")) {
            if (blurRadius != 0f)
                BlurUtils.draw(
                    (x + transX).toFloat() * scale,
                    (y + transY).toFloat() * scale,
                    width * scale,
                    classicHeight * scale,
                    blurRadius
                )

            RenderUtils.drawRect(0F, 0F, width.toFloat(), classicHeight.toFloat(), Color(0, 0, 0, alpha))
            ShadowRenderUtils.drawShadowWithCustomAlpha(0F, 0F, width.toFloat(), classicHeight.toFloat(), 240f)
            RenderUtils.drawRect(
                0F,
                classicHeight - 2F,
                max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)), 0F),
                classicHeight.toFloat(),
                type.renderColor
            )
            font.drawString(title, 5F, 4F, textColor, false)
            font.drawString(content, 5F, 17F, textColor, false)
            return false
        }



        if (style.equals("Intellij")) {
            val imgSuccess =
                classProvider.createResourceLocation("liquidbounce/notifications/intellij/checkmark.png")
            val imgError = classProvider.createResourceLocation("liquidbounce/notifications/intellij/error.png")
            val imgWarning = classProvider.createResourceLocation("liquidbounce/notifications/intellij/warning.png")
            val imgInfo = classProvider.createResourceLocation("liquidbounce/notifications/intellij/info.png")

            (x + 1 + 26F) - (x - 8 - textLength)
            val kek = -x - 1 - 20F

            GlStateManager.resetColor()

            Stencil.write(true)
            if (nTypeError) {
                RenderUtils.drawRoundedRect(-x + 9 + textLength, 1f, kek - 1, -28F - 1, 0, Color(115, 69, 75).rgb)
                RenderUtils.drawRoundedRect(-x + 8 + textLength, 0f, kek, -28F, 0, Color(89, 61, 65).rgb)
                Fonts.M30.drawString(title, -x + 6, -25F, Color(249, 130, 108).rgb, true)
            }
            if (nTypeInfo) {
                RenderUtils.drawRoundedRect(-x + 9 + textLength, 1f, kek - 1, -28F - 1, 0, Color(70, 94, 115).rgb)
                RenderUtils.drawRoundedRect(-x + 8 + textLength, 0f, kek, -28F, 0, Color(61, 72, 87).rgb)
                Fonts.M30.drawString(title, -x + 6, -25F, Color(119, 145, 147).rgb, true)
            }
            if (nTypeSuccess) {
                RenderUtils.drawRoundedRect(-x + 9 + textLength, 1f, kek - 1, -28F - 1, 0, Color(67, 104, 67).rgb)
                RenderUtils.drawRoundedRect(-x + 8 + textLength, 0f, kek, -28F, 0, Color(55, 78, 55).rgb)
                Fonts.M30.drawString(title, -x + 6, -25F, Color(10, 142, 2).rgb, true)
            }
            if (nTypeWarning) {
                RenderUtils.drawRoundedRect(-x + 9 + textLength, 1f, kek - 1, -28F - 1, 0, Color(103, 103, 63).rgb)
                RenderUtils.drawRoundedRect(-x + 8 + textLength, 0f, kek, -28F, 0, Color(80, 80, 57).rgb)
                Fonts.M30.drawString(title, -x + 6, -25F, Color(175, 163, 0).rgb, true)
            }

            Stencil.erase(true)

            GlStateManager.resetColor()

            Stencil.dispose()

            GL11.glPushMatrix()
            GlStateManager.disableAlpha()
            GlStateManager.resetColor()
            GL11.glColor4f(1F, 1F, 1F, 1F)
            RenderUtils.drawImage(
                if (nTypeSuccess) {
                    imgSuccess
                } else if (nTypeError) {
                    imgError
                } else if (nTypeWarning) {
                    imgWarning
                } else {
                    imgInfo
                },
                (kek + 5).toInt(), (-25F - y).toInt(), 7, 7
            )
            GlStateManager.enableAlpha()
            GL11.glPopMatrix()


            Fonts.M30.drawString(content, -x + 6, -13F, -1, true)
            return false
        }
        if (style.equals("CoolSense")) {
            val image =
                classProvider.createResourceLocation("liquidbounce/notifications/cool/" + type.name.toLowerCase() + ".png")

            GL11.glTranslated(width - (width * pct), 0.0, 0.0)
            GL11.glTranslatef(-width.toFloat(), 0F, 0F)

            ShadowRenderUtils.drawShadowWithCustomAlpha(0F, 0F, width.toFloat(), classicHeight.toFloat(), 240f)
            RenderUtils.drawRect(0F, 0F, width.toFloat(), height.toFloat(), type.renderColor)
            RenderUtils.drawRect(0F, 0F, width.toFloat(), height.toFloat(), Color(0, 0, 0, 150))
            RenderUtils.drawRect(
                0F,
                classicHeight - 2F,
                max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)), 0F),
                classicHeight.toFloat(),
                type.renderColor
            )
            Fonts.SF_35.drawString(title, 9F, 4F, -1)
            Fonts.wqy30.drawString(content, 9F, 17F, -1)
            RenderUtils.drawImage(image, -17, 3, 22, 22)
            classProvider.getGlStateManager().resetColor()
            return false
            /*
            *         if (style.equals("Classic")) {
            if (blurRadius != 0f)
                BlurUtils.draw(
                    (x + transX).toFloat() * scale,
                    (y + transY).toFloat() * scale,
                    width * scale,
                    classicHeight * scale,
                    blurRadius
                )

            RenderUtils.drawRect(0F, 0F, width.toFloat(), classicHeight.toFloat(), Color(0, 0, 0, alpha))
            ShadowRenderUtils.drawShadowWithCustomAlpha(0F, 0F, width.toFloat(), classicHeight.toFloat(), 240f)
            RenderUtils.drawRect(
                0F,
                classicHeight - 2F,
                max(width - width * ((nowTime - displayTime) / (animeTime * 2F + time)), 0F),
                classicHeight.toFloat(),
                type.renderColor
            )
            font.drawString(title, 5F, 4F, textColor, false)
            font.drawString(content, 5F, 17F, textColor, false)
            return false
        }
        * */
        }
        return false
    }

}
enum class NotifyType(var renderColor: Color) {
    SUCCESS(Color(0x60E092)),
    ERROR(Color(0xFF2F2F)),
    WARNING(Color(0xF5FD00)),
    INFO(Color(0x6490A7));
}

enum class FadeState { IN, STAY, OUT, END }


