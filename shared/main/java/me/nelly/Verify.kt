package me.nelly

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.cape.CapeAPI
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.features.special.ClientRichPresence
import net.ccbluex.liquidbounce.features.special.DonatorCape
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper
import net.ccbluex.liquidbounce.tabs.BlocksTab
import net.ccbluex.liquidbounce.tabs.ExploitsTab
import net.ccbluex.liquidbounce.tabs.HeadsTab
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.ClassUtils
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.InventoryUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.misc.HttpUtils
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
        fun asfnioasnoasfonfsanofsanoi() {
            launch(Verify::class.java)
        }

        fun lilililili() {
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

        fun ilililililililililil() {
            ClientUtils.getLogger().info("Starting ${LiquidBounce.CLIENT_NAME} b${LiquidBounce.CLIENT_VERSION}, by ${LiquidBounce.CLIENT_CREATOR}")

            // Create file manager
            LiquidBounce.fileManager = FileManager()

            // Crate event manager
            LiquidBounce.eventManager = EventManager()

            // Register listeners
            LiquidBounce.eventManager.registerListener(RotationUtils())
            LiquidBounce.eventManager.registerListener(AntiForge())
            LiquidBounce.eventManager.registerListener(BungeeCordSpoof())
            LiquidBounce.eventManager.registerListener(DonatorCape())
            LiquidBounce.eventManager.registerListener(InventoryUtils())

            // Init Discord RPC
            LiquidBounce.clientRichPresence = ClientRichPresence()

            // Create command manager
            LiquidBounce.commandManager = CommandManager()

            // Load client fonts
            Fonts.loadFonts()

            // Setup module manager and register modules
            LiquidBounce.moduleManager = ModuleManager()
            LiquidBounce.moduleManager.registerModules()

            try {
                // Remapper
                Remapper.loadSrg()

                // ScriptManager
                LiquidBounce.scriptManager = ScriptManager()
                LiquidBounce.scriptManager.loadScripts()
                LiquidBounce.scriptManager.enableScripts()
            } catch (throwable: Throwable) {
                ClientUtils.getLogger().error("Failed to load scripts.", throwable)
            }

            // Register commands
            LiquidBounce.commandManager.registerCommands()

            // Load configs
            LiquidBounce.fileManager.loadConfigs(
                LiquidBounce.fileManager.modulesConfig, LiquidBounce.fileManager.valuesConfig, LiquidBounce.fileManager.accountsConfig,
                LiquidBounce.fileManager.friendsConfig, LiquidBounce.fileManager.xrayConfig, LiquidBounce.fileManager.shortcutsConfig)

            // ClickGUI
            LiquidBounce.clickGui = ClickGui()
            LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.clickGuiConfig)

            // Tabs (Only for Forge!)
            if (ClassUtils.hasForge()) {
                BlocksTab()
                ExploitsTab()
                HeadsTab()
            }

            // Register capes service
            try {
                CapeAPI.registerCapeService()
            } catch (throwable: Throwable) {
                ClientUtils.getLogger().error("Failed to register cape service", throwable)
            }

            // Set HUD
            LiquidBounce.hud = HUD.createDefault()
            LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.hudConfig)

            // Disable optifine fastrender
            ClientUtils.disableFastRender()

            // Load generators
            GuiAltManager.loadGenerators()
        }

        @JvmStatic
        fun lililili1li() {
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
