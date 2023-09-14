package me.nelly

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.cape.CapeAPI
import me.ccbluex.liquidbounce.event.EventManager
import me.ccbluex.liquidbounce.features.command.CommandManager
import me.ccbluex.liquidbounce.features.module.ModuleManager
import me.ccbluex.liquidbounce.features.special.AntiForge
import me.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import me.ccbluex.liquidbounce.features.special.ClientRichPresence
import me.ccbluex.liquidbounce.features.special.DonatorCape
import me.ccbluex.liquidbounce.file.FileManager
import me.ccbluex.liquidbounce.script.ScriptManager
import me.ccbluex.liquidbounce.script.remapper.Remapper
import me.ccbluex.liquidbounce.tabs.BlocksTab
import me.ccbluex.liquidbounce.tabs.ExploitsTab
import me.ccbluex.liquidbounce.tabs.HeadsTab
import me.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import me.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import me.ccbluex.liquidbounce.ui.client.hud.HUD
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.ClassUtils
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.ccbluex.liquidbounce.utils.InventoryUtils
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.minecraft.client.Minecraft
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.security.MessageDigest
import javax.swing.JOptionPane

class Verify : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Verification"
        
        val usernameField = TextField()
        val passwordField = PasswordField()
        val verifyButton = Button("验证")
        verifyButton.setOnAction {
            val username = usernameField.text
            val password = passwordField.text
            val ipAddress = getIPAddress()

            val actualResponse: String = HttpUtils.get("https://gitcode.net/m0_62964839/ruixuesense/-/raw/master/test")

            if (actualResponse.contains(encryptText("$username:$password$ipAddress"))) {
                print("Tokens校验成功")
            } else {
                print("Tokens校验失败")
                JOptionPane.showInputDialog(
                    null, "你的Tokens是: " + this.encryptText("$username:$password$ipAddress"), "你的Tokens是: " + encryptText("$username:$passwordField$ipAddress"))
                Minecraft.getMinecraft().shutdown()
            }
        }
        
        val vbox = VBox(10.0, Label("Username"), usernameField, Label("Password"), passwordField, verifyButton)
        val scene = Scene(vbox, 300.0, 200.0)
        primaryStage.scene = scene
        
        primaryStage.show()
    }

        private fun getIPAddress(): String {
        val url = URL("https://api.ipify.org")
        val connection = url.openConnection()
        val inputStream = connection.getInputStream()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))

        val ipAddress = bufferedReader.readLine()
        
        bufferedReader.close()
        return ipAddress
    }
    
    private fun encryptText(text: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = text.toByteArray()
        val hashedBytes = messageDigest.digest(bytes)
        
        val stringBuilder = StringBuilder()
        for (hashedByte in hashedBytes) {
            val hex = Integer.toHexString(0xff and hashedByte.toInt())
            if (hex.length == 1) {
                stringBuilder.append('0')
            }
            stringBuilder.append(hex)
        }
        
        return stringBuilder.toString()
    }

    companion object {
        @JvmStatic
        fun start() {
            launch(Verify::class.java)
        }

        fun antisb() {
            val usernameField = TextField()
            val passwordField = PasswordField()
            val username = usernameField.text
            val password = passwordField.text

            if (username == null) {
                Minecraft.getMinecraft().shutdown()
            }
            if (password == null) {
                Minecraft.getMinecraft().shutdown()
            }
        }
    }
}
