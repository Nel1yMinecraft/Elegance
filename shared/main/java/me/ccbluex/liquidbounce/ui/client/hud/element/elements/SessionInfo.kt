package me.ccbluex.liquidbounce.ui.client.hud.element.elements

import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP
import me.ccbluex.liquidbounce.features.module.modules.misc.Recorder
import me.ccbluex.liquidbounce.features.module.modules.render.HUD
import me.ccbluex.liquidbounce.ui.client.hud.element.Border
import me.ccbluex.liquidbounce.ui.client.hud.element.Element
import me.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.ColorUtil
import me.ccbluex.liquidbounce.utils.ServerUtils
import me.ccbluex.liquidbounce.utils.SessionUtils
import me.ccbluex.liquidbounce.utils.ShadowUtils
import me.ccbluex.liquidbounce.utils.render.ColorUtils.LiquidSlowly
import me.ccbluex.liquidbounce.utils.render.ColorUtils.fade
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import me.ccbluex.liquidbounce.utils.render.ShadowRenderUtils
import me.ccbluex.liquidbounce.value.*
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

@ElementInfo(name = "SessionInfo")
class SessionInfo(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private val colorModeValue =
        ListValue("Color", arrayOf("Custom", "Sky", "CRainbow", "LiquidSlowly", "Fade","Gradinet"), "Custom")
    private val modeValue = ListValue("Mode", arrayOf("1", "2", "3","4"), "1")
    val colorRedValue = IntegerValue("Red", 255, 0, 255)
    val colorGreenValue = IntegerValue("Green", 255, 0, 255)
    val colorBlueValue = IntegerValue("Blue", 255, 0, 255)
    private val radiusValue = FloatValue("Radius", 4.25f, 0f, 10f)
    private val textRed = IntegerValue("Gradinet-Red", 0, 0, 255)
    private val textGreen = IntegerValue("Gradinet-Green", 0, 0, 255)
    private val textBlue = IntegerValue("Gradinet-Blue", 255, 0, 255)
    private val textRed2 = IntegerValue("Gradinet-Red-2", 25, 0, 255)
    private val textGreen2 = IntegerValue("Gradinet-Green-2", 45, 0, 255)
    private val textBlue2 = IntegerValue("Gradinet-Blue-2", 150, 0, 255)
    private val gidentspeed = IntegerValue("GidentSpeed", 100, 1, 1000)
    val radius = IntegerValue("4-radius",10,0,100).displayable {modeValue.get().contains("4")}
    val lineValue = BoolValue("Line", true)
    private val gradientAmountValue = IntegerValue("Gradient-Amount", 25, 1, 50)
    private val saturationValue = FloatValue("Saturation", 0.9f, 0f, 1f)
    private val brightnessValue = FloatValue("Brightness", 1f, 0f, 1f)
    private val cRainbowSecValue = IntegerValue("CRainbow-Seconds", 2, 1, 10)
    val fontrender = FontValue("Font",Fonts.SF_35)
    val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")


    override fun drawElement(): Border {
        val target: IEntityPlayerSP? = mc.thePlayer
        val convertedTarget = target!!
        val fontrender = fontrender.get()
        if (modeValue.get().equals("1")) {
            for (i in 0..(gradientAmountValue.get() - 1)) {
                RenderUtils.drawCircleRect(0f, 0f, 165f, 63f, 5f, Color(0, 0, 0, 120).rgb)
                RenderUtils.drawRect(
                    0f, 12f, 165f, 13f,
                    getColor()
                )
                Fonts.font40.drawString(
                    "SessionInfo",
                    165 / 2 - Fonts.font40.getStringWidth("SessionInfo") / 2F,
                    3F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Playtime", 2F, 15F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    SessionUtils.getFormatSessionTime(),
                    165 - fontrender.getStringWidth(SessionUtils.getFormatSessionTime()) - 3F,
                    15F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Kills", 2F, 27F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    "${Recorder.killCounts}",
                    165 - fontrender.getStringWidth("${Recorder.killCounts}") - 3F,
                    27F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Name", 2F, 39F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    convertedTarget.name.toString(),
                    165 - fontrender.getStringWidth(convertedTarget.name.toString()) - 3F,
                    39F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Server", 2F, 51F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    ServerUtils.getRemoteIp(),
                    165 - fontrender.getStringWidth(ServerUtils.getRemoteIp()) - 3F,
                    51F,
                    Color(0xFFFFFF).rgb
                )
            }
            return Border(0f, 0f, 165f, 63f)
        }
        if (modeValue.get().equals("2")) {
            for (i in 0..(gradientAmountValue.get() - 1)) {
                RenderUtils.drawRect(0f, 0f, 165f, 63f, Color(0, 0, 0, 120).rgb)
                RenderUtils.drawRect(
                    0f, 0f, 165f, 1f,
                    getColor()
                )
                Fonts.font40.drawString("SessionInfo", 2F, 4F, Color(0xFFFFFF).rgb)
                fontrender.drawString("Playtime", 2F, 15F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    SessionUtils.getFormatSessionTime(),
                    165 - fontrender.getStringWidth(SessionUtils.getFormatSessionTime()) - 3F,
                    15F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Kills", 2F, 27F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    "${Recorder.killCounts}",
                    165 - fontrender.getStringWidth("${Recorder.killCounts}") - 3F,
                    27F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Name", 2F, 39F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    convertedTarget.name.toString(),
                    165 - fontrender.getStringWidth(convertedTarget.name.toString()) - 3F,
                    39F,
                    Color(0xFFFFFF).rgb
                )
                fontrender.drawString("Server", 2F, 51F, Color(0xFFFFFF).rgb)
                fontrender.drawString(
                    ServerUtils.getRemoteIp(),
                    165 - fontrender.getStringWidth(ServerUtils.getRemoteIp()) - 3F,
                    51F,
                    Color(0xFFFFFF).rgb
                )
                return Border(0f, 0f, 165f, 63f)
            }
        }
        if (modeValue.get().equals("3")) {
            val fontRenderer = fontrender
            val y2 = fontRenderer.fontHeight * 4 + 11.0
            val x2 = 140.0


            GlStateManager.pushMatrix()
            GL11.glTranslated(20.5, 15.5, 0.0)
            RenderUtils.drawRoundedRect(
                -6f,
                -15f,
                x2.toFloat() + 4,
                y2.toFloat() - 10 + 10,
                radiusValue.get().toInt(),
                Color(0, 0, 0, 120).rgb
            )
            if (lineValue.get()) {
                val barLength = 142.toDouble()
                for (i in 0..(gradientAmountValue.get() - 1)) {
                    val barStart = i.toDouble() / gradientAmountValue.get().toDouble() * barLength
                    val barEnd = (i + 1).toDouble() / gradientAmountValue.get().toDouble() * barLength
                    getColor()?.let {
                        RenderUtils.drawGradientSideways(
                            -2.0 + barStart, -2.5, -2.0 + barEnd, -1.0,
                            it.rgb,
                            it.rgb
                        )
                    }
                }
            }

            fontrender.drawStringWithShadow("Session Information", (x2.toFloat() / 4).toInt(), -10, Color.WHITE.rgb)
            fontrender.drawStringWithShadow(
                "Play Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}",
                2,
                fontRenderer.fontHeight + -6,
                Color.WHITE.rgb
            )
            fontrender.drawStringWithShadow(
                "Player Killed: ${Recorder.killCounts}",
                2,
                fontRenderer.fontHeight * 2 + -4,
                Color.WHITE.rgb
            )
            fontrender.drawStringWithShadow(
                "GameWons: ${Recorder.win}",
                2,
                fontRenderer.fontHeight * 3 + -2,
                Color.WHITE.rgb
            )
            fontrender.drawStringWithShadow(
                "Grim Bans: ${Recorder.ban}",
                2,
                fontRenderer.fontHeight * 4 + 0,
                Color.WHITE.rgb
            )
        }
        if (modeValue.get().equals("4")) {
            val gradientColor1 = Color(HUD.r.get(), HUD.g.get(), HUD.b.get(), HUD.a.get())
            val gradientColor2 = Color(HUD.r.get(), HUD.g.get(), HUD.b.get(), HUD.a.get())
            val gradientColor3 = Color(HUD.r2.get(), HUD.g2.get(), HUD.b2.get(), HUD.a2.get())
            val gradientColor4 = Color(HUD.r2.get(), HUD.g2.get(), HUD.b2.get(), HUD.a2.get())
            val fontRenderer = fontrender
            val y2 = fontRenderer.fontHeight * 5 + 11.0.toInt()
            val x2 = 140.0.toInt()
            if (lineValue.get()) {
                val barLength = 142.toDouble()
                for (i in 0 until gradientAmountValue.get()) {
                    val barStart = i.toDouble() / gradientAmountValue.get().toDouble() * barLength
                    val barEnd = (i + 1).toDouble() / gradientAmountValue.get().toDouble() * barLength
                    getColor()?.let {
                        RenderUtils.drawGradientSideways(
                            -2.0 + barStart, -2.5, -2.0 + barEnd, -1.0,
                            it.rgb,
                            it.rgb
                        )
                    }
                }
            }
            ShadowRenderUtils.drawShadowWithCustomAlpha(-2f, -2f, x2.toFloat(), y2.toFloat(), 250f) // oops
            RenderUtils.drawGradientRound(
                -2f,
                -2f,
                x2.toFloat(),
                y2.toFloat(),
                radius.get().toFloat(),
                ColorUtil.applyOpacity(gradientColor4, .85f),
                gradientColor1,
                gradientColor3,
                gradientColor2
            )
            fontRenderer.drawCenteredString("GameInfo", 31.5F, 3f, Color.WHITE.rgb, true)
            fontRenderer.drawStringWithShadow(
                "PlayTimes: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}",
                2,
                (fontRenderer.fontHeight + 8f).toInt(),
                Color.WHITE.rgb
            )
            fontRenderer.drawStringWithShadow(
                "killCounts: " + Recorder.killCounts,
                2,
                (fontRenderer.fontHeight * 2 + 8f).toInt(),
                Color.WHITE.rgb
            )
            fontRenderer.drawStringWithShadow(
                "Wins: " + Recorder.win, 2,
                (fontRenderer.fontHeight * 3 + 8f).toInt(), Color.WHITE.rgb
            )
            fontRenderer.drawStringWithShadow(
                "TotalPlayed: " + Recorder.totalPlayed, 2,
                (fontRenderer.fontHeight * 4 + 8f).toInt(), Color.WHITE.rgb
            )
        }
        return Border(14F, 0F, 165F, 63F)
    }

    fun getColor(): Color? {
        when (colorModeValue.get()) {
            "Custom" -> return Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
            "Rainbow" -> return Color(
                RenderUtils.getRainbowOpaque(
                    cRainbowSecValue.get(),
                    saturationValue.get(),
                    brightnessValue.get(),
                    0
                )
            )

            "Sky" -> return RenderUtils.skyRainbow(0, saturationValue.get(), brightnessValue.get())
            "LiquidSlowly" -> return LiquidSlowly(System.nanoTime(), 0, saturationValue.get(), brightnessValue.get())
            "Fade" -> return fade(Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get()), 0, 100)
            "Gradinet" -> return RenderUtils.getGradientOffset(
                Color(
                    textRed.get(),
                    textGreen.get(),
                    textBlue.get(),
                    1
                ),
                Color(
                    textRed2.get(),
                    textGreen2.get(),
                    textBlue2.get(),
                    1
                ),
                (Math.abs(
                    System.currentTimeMillis() / gidentspeed.get()
                        .toDouble() + (0f / fontrender.get().fontHeight)
                ) / 10)
            )
        }
        return null
    }
}
