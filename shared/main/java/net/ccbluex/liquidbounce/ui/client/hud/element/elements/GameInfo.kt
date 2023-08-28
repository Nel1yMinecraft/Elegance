package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.misc.Recorder
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ColorUtil
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.*
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "GameInfo")
class GameInfo(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private var fontValue = FontValue("Font", Fonts.SF_35)
    val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    val radius = IntegerValue("radius",1,1,10)
    override fun drawElement(): Border {
        var gradientColor1 = Color(HUD.r.get(),HUD.g.get(),HUD.b.get(),HUD.a.get())
        var gradientColor2 = Color(HUD.r.get(),HUD.g.get(),HUD.b.get(),HUD.a.get())
        var gradientColor3 = Color(HUD.r2.get(),HUD.g2.get(),HUD.b2.get(),HUD.a2.get())
        var gradientColor4 = Color(HUD.r2.get(),HUD.g2.get(),HUD.b2.get(),HUD.a2.get())
        val fontRenderer = fontValue.get()
        val y2 = fontRenderer.fontHeight * 5 + 11.0.toInt()
        val x2 = 140.0.toInt()
            //drawShadow
            RenderUtils.drawGradientRound(-2f, -2f, x2.toFloat(), y2.toFloat(), radius.get().toFloat(), ColorUtil.applyOpacity(gradientColor4, .85f), gradientColor1, gradientColor3, gradientColor2)
        fontRenderer.drawCenteredString("GameInfo", 31.5F, 3f, Color.WHITE.rgb, true)
        fontRenderer.drawStringWithShadow("PlayTimes: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}", 2, (fontRenderer.fontHeight + 8f).toInt(), Color.WHITE.rgb)
        fontRenderer.drawStringWithShadow("killCounts: " + Recorder.killCounts, 2, (fontRenderer.fontHeight * 2 + 8f).toInt(), Color.WHITE.rgb)
        fontRenderer.drawStringWithShadow("Wins: " + Recorder.win, 2,
            (fontRenderer.fontHeight * 3 + 8f).toInt(), Color.WHITE.rgb)
        fontRenderer.drawStringWithShadow("TotalPlayed: " +Recorder.totalPlayed , 2,
            (fontRenderer.fontHeight * 4 + 8f).toInt(), Color.WHITE.rgb)
        return Border(-2f, -2f, x2.toFloat(), y2.toFloat())
    }
}
