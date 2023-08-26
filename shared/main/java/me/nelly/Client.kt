package me.nelly

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

class Client {
    companion object {
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
    }
}