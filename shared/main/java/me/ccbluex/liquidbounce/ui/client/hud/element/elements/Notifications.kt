/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/liquidbounce/
 */
package me.ccbluex.liquidbounce.ui.client.hud.element.elements

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import me.ccbluex.liquidbounce.features.module.modules.render.HUD
import me.ccbluex.liquidbounce.ui.client.hud.element.Border
import me.ccbluex.liquidbounce.ui.client.hud.element.Element
import me.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import me.ccbluex.liquidbounce.ui.client.hud.element.Side
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.FadeState.*
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.ColorUtil
import me.ccbluex.liquidbounce.utils.EaseUtils
import me.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.utils.render.ShadowRenderUtils
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.IntegerValue
import me.ccbluex.liquidbounce.value.ListValue
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
    private val borderRadius = IntegerValue("BorderRadius", 10, 1, 50)

    companion object {
        val styleValue = ListValue("Mode", arrayOf("Tenacity","FDP2","FDP"), "FDP")
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
                    borderRadius.get(),
                    backGroundAlphaValue.get(),
                    contentShadow.get(),
                    titleShadow.get(),
                    whiteText.get(),
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
        index: Int, font: IFontRenderer,
        alpha: Int,
        radius: Int,
        contentShadow: Boolean,
        titleShadow: Boolean,
        whiteText: Boolean,
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

        if (style.equals("FDP2")) {

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

        if (style.contains("FDP")) {
            val colors = Color(type.renderColor.red, type.renderColor.green, type.renderColor.blue, alpha / 3)
            ShadowRenderUtils.drawShadowWithCustomAlpha(2f, 0F, width.toFloat(), 27f - 5f, 250f)
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

            Fonts.C27.drawString("$title (Wait ${time / 1000})", 6.5F, 3.2F, textColor, titleShadow)
            font.drawString(content, 6.5F, 8.7F, textColor, contentShadow)
            return false
        }

        if (style.contains("Tenacity")) {
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


