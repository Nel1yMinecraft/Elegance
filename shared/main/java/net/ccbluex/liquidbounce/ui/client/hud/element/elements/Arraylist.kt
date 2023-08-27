/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.client.hud.element.Side.Horizontal
import net.ccbluex.liquidbounce.ui.client.hud.element.Side.Vertical
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ShadowUtils
import net.ccbluex.liquidbounce.utils.render.AnimationUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowFontShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.RainbowShader
import net.ccbluex.liquidbounce.value.*
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * CustomHUD Arraylist element
 *
 * Shows a list of enabled modules
 */
@ElementInfo(name = "Arraylist", single = true)
class Arraylist(x: Double = 1.0, y: Double = 2.0, scale: Float = 1F,
                side: Side = Side(Horizontal.RIGHT, Vertical.UP)) : Element(x, y, scale, side) {
    private val rainbowX = FloatValue("Rainbow-X", -1000F, -2000F, 2000F)
    private val rainbowY = FloatValue("Rainbow-Y", -1000F, -2000F, 2000F)
    private val colorModeValue = ListValue("Text-Color", arrayOf("Custom", "Random", "Rainbow"), "Custom")
    private val colorRedValue = IntegerValue("Text-R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("Text-G", 111, 0, 255)
    private val colorBlueValue = IntegerValue("Text-B", 255, 0, 255)
    private val rectColorModeValue = ListValue("Rect-Color", arrayOf("Custom", "Random", "Rainbow"), "Rainbow")
    private val rectColorRedValue = IntegerValue("Rect-R", 255, 0, 255)
    private val rectColorGreenValue = IntegerValue("Rect-G", 255, 0, 255)
    private val rectColorBlueValue = IntegerValue("Rect-B", 255, 0, 255)
    private val rectColorBlueAlpha = IntegerValue("Rect-Alpha", 255, 0, 255)
    private val saturationValue = FloatValue("Random-Saturation", 0.9f, 0f, 1f)
    private val brightnessValue = FloatValue("Random-Brightness", 1f, 0f, 1f)
    private val tags = BoolValue("Tags", true)
    private val shadow = BoolValue("ShadowText", true)
    private val shadowShaderValue = BoolValue("Shadow", false).displayable { shadow.get() }
    private val shadowStrength = IntegerValue("Shadow-Strength", 1, 1, 30).displayable { shadow.get() }
    private val shadowColorMode =
        ListValue("Shadow-Color", arrayOf("Background", "Text", "Custom"), "Background").displayable { shadow.get() }
    private val shadowColorRedValue = IntegerValue("Shadow-Red", 0, 0, 255).displayable { shadow.get() }
    private val shadowColorGreenValue = IntegerValue("Shadow-Green", 111, 0, 255).displayable { shadow.get() }
    private val shadowColorBlueValue = IntegerValue("Shadow-Blue", 255, 0, 255).displayable { shadow.get() }
    private val colorAlphaValue = IntegerValue("Alpha", 255, 0, 255)
    private val skyDistanceValue = IntegerValue("Sky-Distance", 2, 0, 4)
    private val cRainbowSecValue = IntegerValue("CRainbow-Seconds", 2, 1, 10)
    private val cRainbowDistValue = IntegerValue("CRainbow-Distance", 2, 1, 6)
    private val liquidSlowlyDistanceValue = IntegerValue("LiquidSlowly-Distance", 90, 1, 90)
    private val fadeDistanceValue = IntegerValue("Fade-Distance", 50, 1, 100)
    private val backgroundColorModeValue = ListValue("Background-Color", arrayOf("Custom", "Random", "Rainbow"), "Custom")
    private val backgroundColorRedValue = IntegerValue("Background-R", 0, 0, 255)
    private val backgroundColorGreenValue = IntegerValue("Background-G", 0, 0, 255)
    private val backgroundColorBlueValue = IntegerValue("Background-B", 0, 0, 255)
    private val backgroundColorAlphaValue = IntegerValue("Background-Alpha", 0, 0, 255)
    private val rectValue = ListValue("Rect", arrayOf("None", "Left", "Right"), "None")
    private val upperCaseValue = BoolValue("UpperCase", false)
    private val spaceValue = FloatValue("Space", 0F, 0F, 5F)
    private val textHeightValue = FloatValue("TextHeight", 11F, 1F, 20F)
    private val textYValue = FloatValue("TextY", 1F, 0F, 20F)
    private val tagsArrayColor = BoolValue("TagsArrayColor", false)
    private val fontValue = FontValue("Font", Fonts.font40)

    private var x2 = 0
    private var y2 = 0F

    private var modules = emptyList<Module>()
    val counter = intArrayOf(0)

    override fun drawElement(): Border? {
        val fontRenderer = fontValue.get()

        AWTFontRenderer.assumeNonVolatile = true

        // Slide animation - update every render
        val delta = RenderUtils.deltaTime

        for (module in LiquidBounce.moduleManager.modules) {
            if (!module.array || (!module.state && module.slide == 0F)) continue

            var displayString = if (!tags.get())
                module.name
            else if (tagsArrayColor.get())
                module.colorlessTagName
            else module.tagName

            if (upperCaseValue.get())
                displayString = displayString.toUpperCase()

            val width = fontRenderer.getStringWidth(displayString)

            if (module.state) {
                if (module.slide < width) {
                    module.slide = AnimationUtils.easeOut(module.slideStep, width.toFloat()) * width
                    module.slideStep += delta / 4F
                }
            } else if (module.slide > 0) {
                module.slide = AnimationUtils.easeOut(module.slideStep, width.toFloat()) * width
                module.slideStep -= delta / 4F
            }

            module.slide = module.slide.coerceIn(0F, width.toFloat())
            module.slideStep = module.slideStep.coerceIn(0F, width.toFloat())
        }

        // Draw arraylist
        val colorMode = colorModeValue.get()
        val rectColorMode = rectColorModeValue.get()
        val backgroundColorMode = backgroundColorModeValue.get()
        val customColor = Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), 1).rgb
        val rectCustomColor = Color(rectColorRedValue.get(), rectColorGreenValue.get(), rectColorBlueValue.get(),
                rectColorBlueAlpha.get()).rgb
        val space = spaceValue.get()
        val textHeight = textHeightValue.get()
        val textY = textYValue.get()
        val rectMode = rectValue.get()
        val backgroundCustomColor = Color(backgroundColorRedValue.get(), backgroundColorGreenValue.get(),
                backgroundColorBlueValue.get(), backgroundColorAlphaValue.get()).rgb
        val textShadow = shadow.get()
        val textSpacer = textHeight + space
        val saturation = saturationValue.get()
        val brightness = brightnessValue.get()

        when (side.horizontal) {
            Horizontal.RIGHT, Horizontal.MIDDLE -> {
                if (shadowShaderValue.get()) {
                    GL11.glTranslated(-renderX, -renderY, 0.0)
                    GL11.glPushMatrix()
                    ShadowUtils.shadow(shadowStrength.get().toFloat(), {
                        GL11.glPushMatrix()
                        GL11.glTranslated(renderX, renderY, 0.0)
                        modules.forEachIndexed { index, module ->
                            val xPos = -module.slide - 2
                            RenderUtils.newDrawRect(
                                xPos - 2,
                                module.higt,
                                0F,
                                module.higt + textHeight,
                                when (shadowColorMode.get().toLowerCase()) {
                                    "background" -> Color(
                                        backgroundColorRedValue.get(),
                                        backgroundColorGreenValue.get(),
                                        backgroundColorBlueValue.get()
                                    ).rgb

                                    "text" -> {
                                        val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb

                                        var Sky = RenderUtils.SkyRainbow(
                                            counter[0] * (skyDistanceValue.get() * 50),
                                            saturationValue.get(),
                                            brightnessValue.get()
                                        )
                                        var CRainbow = RenderUtils.getRainbowOpaque(
                                            cRainbowSecValue.get(),
                                            saturationValue.get(),
                                            brightnessValue.get(),
                                            counter[0] * (50 * cRainbowDistValue.get())
                                        )
                                        var FadeColor = ColorUtils.fade(
                                            Color(
                                                colorRedValue.get(),
                                                colorGreenValue.get(),
                                                colorBlueValue.get(),
                                                colorAlphaValue.get()
                                            ), index * fadeDistanceValue.get(), 100
                                        ).rgb
                                        counter[0] = counter[0] - 1

                                        val test = ColorUtils.LiquidSlowly(
                                            System.nanoTime(),
                                            index * liquidSlowlyDistanceValue.get(),
                                            saturationValue.get(),
                                            brightnessValue.get()
                                        )?.rgb
                                        var LiquidSlowly: Int = test!!

                                        when {
                                            colorMode.equals("Random", ignoreCase = true) -> moduleColor
                                            colorMode.equals("Sky", ignoreCase = true) -> Sky
                                            colorMode.equals("CRainbow", ignoreCase = true) -> CRainbow
                                            colorMode.equals("LiquidSlowly", ignoreCase = true) -> LiquidSlowly
                                            colorMode.equals("Fade", ignoreCase = true) -> FadeColor

                                            else -> customColor
                                        }
                                    }

                                    else -> Color(
                                        shadowColorRedValue.get(),
                                        shadowColorGreenValue.get(),
                                        shadowColorBlueValue.get()
                                    ).rgb
                                }
                            )
                        }
                        GL11.glPopMatrix()
                        counter[0] = 0
                    }, {
                    })
                    GL11.glPopMatrix()
                    GL11.glTranslated(renderX, renderY, 0.0)
                }
                modules.forEachIndexed { index, module ->
                    var displayString = if (!tags.get())
                        module.name
                    else if (tagsArrayColor.get())
                        module.colorlessTagName
                    else module.tagName

                    if (upperCaseValue.get())
                        displayString = displayString.toUpperCase()

                    val xPos = -module.slide - 2
                    val yPos = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) *
                            if (side.vertical == Vertical.DOWN) index + 1 else index
                    val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb

                    val backgroundRectRainbow = backgroundColorMode.equals("Rainbow", ignoreCase = true)

                    RainbowShader.begin(backgroundRectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        RenderUtils.drawRect(
                                xPos - if (rectMode.equals("right", true)) 5 else 2,
                                yPos,
                                if (rectMode.equals("right", true)) -3F else 0F,
                                yPos + textHeight, when {
                            backgroundRectRainbow -> 0xFF shl 24
                            backgroundColorMode.equals("Random", ignoreCase = true) -> moduleColor
                            else -> backgroundCustomColor
                        }
                        )
                    }

                    val rainbow = colorMode.equals("Rainbow", ignoreCase = true)

                    classProvider.getGlStateManager().resetColor()
                    RainbowFontShader.begin(rainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        fontRenderer.drawString(displayString, xPos - if (rectMode.equals("right", true)) 3 else 0, yPos + textY, when {
                            rainbow -> 0
                            colorMode.equals("Random", ignoreCase = true) -> moduleColor
                            else -> customColor
                        }, textShadow)
                    }

                    if (!rectMode.equals("none", true)) {
                        val rectRainbow = rectColorMode.equals("Rainbow", ignoreCase = true)

                        RainbowShader.begin(rectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                            val rectColor = when {
                                rectRainbow -> 0
                                rectColorMode.equals("Random", ignoreCase = true) -> moduleColor
                                else -> rectCustomColor
                            }

                            when {
                                rectMode.equals("left", true) -> RenderUtils.drawRect(xPos - 5, yPos, xPos - 2, yPos + textHeight,
                                        rectColor)
                                rectMode.equals("right", true) -> RenderUtils.drawRect(-3F, yPos, 0F,
                                        yPos + textHeight, rectColor)
                            }
                        }
                    }
                }
            }

            Horizontal.LEFT -> {
                modules.forEachIndexed { index, module ->
                    var displayString = if (!tags.get())
                        module.name
                    else if (tagsArrayColor.get())
                        module.colorlessTagName
                    else module.tagName

                    if (upperCaseValue.get())
                        displayString = displayString.toUpperCase()

                    val width = fontRenderer.getStringWidth(displayString)
                    val xPos = -(width - module.slide) + if (rectMode.equals("left", true)) 5 else 2
                    val yPos = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) *
                            if (side.vertical == Vertical.DOWN) index + 1 else index
                    val moduleColor = Color.getHSBColor(module.hue, saturation, brightness).rgb

                    val backgroundRectRainbow = backgroundColorMode.equals("Rainbow", ignoreCase = true)

                    RainbowShader.begin(backgroundRectRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        RenderUtils.drawRect(
                                0F,
                                yPos,
                                xPos + width + if (rectMode.equals("right", true)) 5 else 2,
                                yPos + textHeight, when {
                            backgroundRectRainbow -> 0
                            backgroundColorMode.equals("Random", ignoreCase = true) -> moduleColor
                            else -> backgroundCustomColor
                        }
                        )
                    }

                    val rainbow = colorMode.equals("Rainbow", ignoreCase = true)

                    classProvider.getGlStateManager().resetColor()
                    RainbowFontShader.begin(rainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        fontRenderer.drawString(displayString, xPos, yPos + textY, when {
                            rainbow -> 0
                            colorMode.equals("Random", ignoreCase = true) -> moduleColor
                            else -> customColor
                        }, textShadow)
                    }

                    val rectColorRainbow = rectColorMode.equals("Rainbow", ignoreCase = true)

                    RainbowShader.begin(rectColorRainbow, if (rainbowX.get() == 0.0F) 0.0F else 1.0F / rainbowX.get(), if (rainbowY.get() == 0.0F) 0.0F else 1.0F / rainbowY.get(), System.currentTimeMillis() % 10000 / 10000F).use {
                        if (!rectMode.equals("none", true)) {
                            val rectColor = when {
                                rectColorRainbow -> 0
                                rectColorMode.equals("Random", ignoreCase = true) -> moduleColor
                                else -> rectCustomColor
                            }

                            when {
                                rectMode.equals("left", true) -> RenderUtils.drawRect(0F,
                                        yPos - 1, 3F, yPos + textHeight, rectColor)
                                rectMode.equals("right", true) ->
                                    RenderUtils.drawRect(xPos + width + 2, yPos, xPos + width + 2 + 3,
                                            yPos + textHeight, rectColor)
                            }
                        }
                    }
                }
            }
        }

        // Draw border
        if (classProvider.isGuiHudDesigner(mc.currentScreen)) {
            x2 = Int.MIN_VALUE

            if (modules.isEmpty()) {
                return if (side.horizontal == Horizontal.LEFT)
                    Border(0F, -1F, 20F, 20F)
                else
                    Border(0F, -1F, -20F, 20F)
            }

            for (module in modules) {
                when (side.horizontal) {
                    Horizontal.RIGHT, Horizontal.MIDDLE -> {
                        val xPos = -module.slide.toInt() - 2
                        if (x2 == Int.MIN_VALUE || xPos < x2) x2 = xPos
                    }
                    Horizontal.LEFT -> {
                        val xPos = module.slide.toInt() + 14
                        if (x2 == Int.MIN_VALUE || xPos > x2) x2 = xPos
                    }
                }
            }
            y2 = (if (side.vertical == Vertical.DOWN) -textSpacer else textSpacer) * modules.size

            return Border(0F, 0F, x2 - 7F, y2 - if (side.vertical == Vertical.DOWN) 1F else 0F)
        }

        AWTFontRenderer.assumeNonVolatile = false
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        return null
    }

    override fun updateElement() {
        modules = LiquidBounce.moduleManager.modules
                .filter { it.array && it.slide > 0 }
                .sortedBy { -fontValue.get().getStringWidth(if (upperCaseValue.get()) (if (!tags.get()) it.name else if (tagsArrayColor.get()) it.colorlessTagName else it.tagName).toUpperCase() else if (!tags.get()) it.name else if (tagsArrayColor.get()) it.colorlessTagName else it.tagName) }
    }
}