package me.nelly

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.datatransfer.StringSelection
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.util.*

class GuiLogin : GuiScreen() {
    private lateinit var loginButton: GuiButton
    override fun initGui() {
        buttonList.clear()

        val centerX = width / 2
        val centerY = height / 2

        val buttonWidth = 162
        val buttonHeight = fontRenderer.FONT_HEIGHT + 17

        // 创建圆角按钮
        loginButton = GuiButton(0, centerX - buttonWidth / 2, centerY + 40, buttonWidth, buttonHeight, "Login")
        buttonList.add(loginButton)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        RenderUtils.drawImage(ResourceLocation("liquidbounce/splash.png"), 0, 0, width, height)

        // 绘制圆角按钮
        drawRoundedButton(loginButton)

        // 绘制按钮文字
        drawCenteredString(fontRenderer, loginButton.displayString, loginButton.x + loginButton.width / 2, loginButton.y + (loginButton.height - 8) / 2, 0xFFFFFF)

    }

    override fun actionPerformed(button: GuiButton) {
        if (button === loginButton) {
            val currentIP = getIP()

            if (this.getweb("https://gitcode.net/m0_74037382/emperor/-/raw/master/Guilogin")!!.contains(currentIP!!.trim())) {
                LiquidBounce.showNotification("验证成功", ".",TrayIcon.MessageType.INFO)
                mc.displayGuiScreen(GuiMainMenu())
            } else {
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(currentIP), null)
                LiquidBounce.showNotification("", "验证失败，IP已复制到剪贴板",TrayIcon.MessageType.INFO)
                mc.shutdown()
            }
        }
    }

    /**
     * 获取网页文本内容
     */
    private fun getweb(urlString: String): String? {
        var content: String? = null
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                content = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()
            }

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return content
    }

    /**
     * 获取IP地址
     */
    fun getIP(): String? {
        var ip: String? = null
        try {
            val address = InetAddress.getLocalHost()
            ip = address.hostAddress
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ip
    }

    /**
     * 绘制圆角按钮
     */
    private fun drawRoundedButton(button: GuiButton) {
        val cornerRadius = 5

        // 绘制按钮背景
        drawRoundedRect(button.x, button.y, button.x + button.width, button.y + button.height, cornerRadius, 0xFF35506F.toInt())

        // 绘制按钮边框
        drawRoundedRectOutline(button.x, button.y, button.x + button.width, button.y + button.height, cornerRadius, 2, 0xFFFFFFFF.toInt())
    }

    /**
     * 绘制圆角矩形
     */
    private fun drawRoundedRect(left: Int, top: Int, right: Int, bottom: Int, radius: Int, color: Int) {
        drawGradientRect(left + radius, top, right - radius, bottom, color, color)
        drawGradientRect(left, top + radius, left + radius, bottom - radius, color, color)
        drawGradientRect(right - radius, top + radius, right, bottom - radius, color, color)

        drawArc(left + radius, top + radius, radius, 0, 90, color)
        drawArc(right - radius, top + radius, radius, 270, 360, color)
        drawArc(right - radius, bottom - radius, radius, 180, 270, color)
        drawArc(left + radius, bottom - radius, radius, 90, 180, color)
    }

    /**
     * 绘制圆角矩形的边框
     */
    private fun drawRoundedRectOutline(left: Int, top: Int, right: Int, bottom: Int, radius: Int, thickness: Int, color: Int) {
        for (i in 0 until thickness) {
            drawArc(left + radius - i, top + radius - i, radius, 135, 315, color)
            drawArc(right - radius - i, top + radius - i, radius, 45, 225, color)
            drawArc(right - radius - i, bottom - radius - i, radius, -45, 135, color)
            drawArc(left + radius - i, bottom - radius - i, radius, -135, 45, color)

            drawLine(left + radius - i, top + i, right - radius + i, top + i, color)
            drawLine(right - i, top + radius - radius + i, right - i, bottom - radius + radius - i, color)
            drawLine(right - radius + i, bottom - i, left + radius - i, bottom - i, color)
            drawLine(left + i, bottom - radius + radius - i, left + i, top + radius - radius + i, color)
        }
    }

    /**
     * 绘制圆弧
     */
    private fun drawArc(cx: Int, cy: Int, radius: Int, startAngle: Int, endAngle: Int, color: Int) {
        for (angle in startAngle until endAngle) {
            val x = cx + radius * Math.cos(angle.toRadians())
            val y = cy + radius * Math.sin(angle.toRadians())
            drawPixel(x.toInt(), y.toInt(), color)
        }
    }

    /**
     * 绘制直线
     */
    private fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        drawRect(x1, y1, x2, y2, color)
    }

    /**
     * 绘制像素点
     */
    private fun drawPixel(x: Int, y: Int, color: Int) {
        drawRect(x, y, x + 1, y + 1, color)
    }

    /**
     * 编码字符串
     */
    private fun encode(inputData: String?): String? {
        if (inputData.isNullOrEmpty()) {
            return null
        }
        val encodedBytes = Base64.getEncoder().encode(inputData.toByteArray())
        return String(encodedBytes)
    }

    private fun Int.toRadians() = this * Math.PI / 180
}