/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce

import me.ccbluex.liquidbounce.api.Wrapper
import me.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import me.ccbluex.liquidbounce.cape.CapeAPI.registerCapeService
import me.ccbluex.liquidbounce.event.ClientShutdownEvent
import me.ccbluex.liquidbounce.event.EventManager
import me.ccbluex.liquidbounce.features.command.CommandManager
import me.ccbluex.liquidbounce.features.module.ModuleManager
import me.ccbluex.liquidbounce.features.special.AntiForge
import me.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import me.ccbluex.liquidbounce.features.special.ClientRichPresence
import me.ccbluex.liquidbounce.features.special.DonatorCape
import me.ccbluex.liquidbounce.file.FileManager
import me.ccbluex.liquidbounce.injection.backend.Backend
import me.ccbluex.liquidbounce.script.ScriptManager
import me.ccbluex.liquidbounce.script.remapper.Remapper.loadSrg
import me.ccbluex.liquidbounce.tabs.BlocksTab
import me.ccbluex.liquidbounce.tabs.ExploitsTab
import me.ccbluex.liquidbounce.tabs.HeadsTab
import me.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import me.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import me.ccbluex.liquidbounce.ui.client.hud.HUD
import me.ccbluex.liquidbounce.ui.client.hud.HUD.Companion.createDefault
import me.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import me.ccbluex.liquidbounce.ui.font.Fonts
import me.ccbluex.liquidbounce.utils.ClassUtils.hasForge
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.ccbluex.liquidbounce.utils.InventoryUtils
import me.ccbluex.liquidbounce.utils.MinecraftInstance
import me.ccbluex.liquidbounce.utils.RotationUtils
import me.ccbluex.liquidbounce.utils.misc.HttpUtils
import me.ccbluex.liquidbounce.utils.sound.TipSoundManager
import org.lwjgl.opengl.Display
import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object LiquidBounce {

    // Client information
    const val CLIENT_NAME = "Elegance"
    const val CLIENT_VERSION = 1.1
    const val MINECRAFT_VERSION = Backend.MINECRAFT_VERSION
    const val CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"

    var isStarting = false

    // Managers
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var tipSoundManager: TipSoundManager

    // HUD & ClickGUI
    lateinit var hud: HUD

    lateinit var clickGui: ClickGui

    // Update information
    var latestVersion = 0
    var playTimeStart: Long = 0

    // Menu Background
    var background: IResourceLocation? = null

    // Discord RPC
    lateinit var clientRichPresence: ClientRichPresence

    lateinit var wrapper: Wrapper
    val yiyan: String = HttpUtils.get("https://tenapi.cn/v2/yiyan")

    val UPDATE_LIST = arrayListOf(
        "Update Logs :",
        "< 1.1 >",
        "[+] Aura",
        "[~] HUD",
        "[~] ....."
    )
    fun showNotification(Title: String, Text: String, type: TrayIcon.MessageType?) {
        val tray = SystemTray.getSystemTray()
        val image = Toolkit.getDefaultToolkit().createImage("icon.png")
        val trayIcon = TrayIcon(image, "Tray Demo")
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "System tray icon demo"
        tray.add(trayIcon)
        trayIcon.displayMessage(Title, Text, type)
    }
    fun isNetworkConnected(): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
    /**
     * Execute if client will be started
     */
    fun startClient() {
        //    Verify.asfnioasnoasfonfsanofsanoi()
        //   Verify.lilililili()
        isStarting = true
        //Verify2.veirfy()
        if(isNetworkConnected()) {
            showNotification("Verify-Ok!", CLIENT_NAME,TrayIcon.MessageType.INFO)
        } else {
            showNotification("Verify-ERROR!", CLIENT_NAME, TrayIcon.MessageType.ERROR)
        }
        ClientUtils.getLogger().info("Starting $CLIENT_NAME $CLIENT_VERSION")
        ClientUtils.getLogger().info("  ______   _                                              \n" +
                " |  ____| | |                                             \n" +
                " | |__    | |   ___    __ _    __ _   _ __     ___    ___ \n" +
                " |  __|   | |  / _ \\  / _` |  / _` | | '_ \\   / __|  / _ \\\n" +
                " | |____  | | |  __/ | (_| | | (_| | | | | | | (__  |  __/\n" +
                " |______| |_|  \\___|  \\__, |  \\__,_| |_| |_|  \\___|  \\___|\n" +
                "                       __/ |                              \n" +
                "                      |___/                               ")

        playTimeStart = System.currentTimeMillis()

        // Create file manager
        fileManager = FileManager()

        // Crate event manager
        eventManager = EventManager()

        //Crate tipsound manager
        tipSoundManager = TipSoundManager()


        // Register listeners
        eventManager.registerListener(RotationUtils())
        eventManager.registerListener(AntiForge())
        eventManager.registerListener(BungeeCordSpoof())
        eventManager.registerListener(DonatorCape())
        eventManager.registerListener(InventoryUtils())

        // Create command manager
        commandManager = CommandManager()

        // Load client fonts
        Fonts.loadFonts()
        FontLoaders.initFonts()
        // Setup module manager and register modules
        moduleManager = ModuleManager()
        moduleManager.registerModules()

        try {
            // Remapper
            loadSrg()

            // ScriptManager
            scriptManager = ScriptManager()
            scriptManager.loadScripts()
            scriptManager.enableScripts()
        } catch (throwable: Throwable) {
            ClientUtils.getLogger().error("Failed to load scripts.", throwable)
        }

        // Register commands
        commandManager.registerCommands()

        // Load configs
        fileManager.loadConfigs(
            fileManager.modulesConfig, fileManager.valuesConfig, fileManager.accountsConfig,
            fileManager.friendsConfig, fileManager.xrayConfig, fileManager.shortcutsConfig)

        // ClickGUI
        clickGui = ClickGui()
        fileManager.loadConfig(fileManager.clickGuiConfig)

        // Tabs (Only for Forge!)
        if (hasForge()) {
            BlocksTab()
            ExploitsTab()
            HeadsTab()
        }

        // Register capes service
        try {
            registerCapeService()
        } catch (throwable: Throwable) {
            ClientUtils.getLogger().error("Failed to register cape service", throwable)
        }

        // Set HUD
        hud = createDefault()
        fileManager.loadConfig(fileManager.hudConfig)

        // Disable optifine fastrender
        ClientUtils.disableFastRender()

        // Load generators
        GuiAltManager.loadGenerators()

        Display.setTitle("$CLIENT_NAME | $CLIENT_VERSION | $yiyan")
        isStarting = false
        ClientUtils.getLogger().info(playTimeStart.toString() + "ms")
    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        // Call client shutdown
        eventManager.callEvent(ClientShutdownEvent())

        // Save all available configs
        fileManager.saveAllConfigs()

    }
}