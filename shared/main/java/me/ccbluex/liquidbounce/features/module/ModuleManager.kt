/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module

import me.caijiplayer.HytAntiVoid
import me.nelly.*
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.KeyEvent
import me.ccbluex.liquidbounce.event.Listenable
import me.ccbluex.liquidbounce.features.module.modules.`fun`.Derp
import me.ccbluex.liquidbounce.features.module.modules.`fun`.SkinDerp
import me.ccbluex.liquidbounce.features.module.modules.combat.*
import me.ccbluex.liquidbounce.features.module.modules.exploit.*
import me.ccbluex.liquidbounce.features.module.modules.misc.*
import me.ccbluex.liquidbounce.features.module.modules.movement.*
import me.ccbluex.liquidbounce.features.module.modules.player.*
import me.ccbluex.liquidbounce.features.module.modules.render.*
import me.ccbluex.liquidbounce.features.module.modules.world.*
import me.ccbluex.liquidbounce.features.module.modules.world.Timer
import me.ccbluex.liquidbounce.utils.ClientUtils
import me.kid.GrimCivBreak
import me.nelly.fdp.module.AutoReport
import me.paimon.NameTags
import me.paimon.StrafeFix
import me.rainyfall.*
import me.nelly.fdp.clientspoof.ClientSpoofer
import me.nelly.fdp.module.InfiniteAura
import me.nelly.fdp.module.PotionSpoof
import java.util.*


class ModuleManager : Listenable {

    val modules = TreeSet<Module> { module1, module2 -> module1.name.compareTo(module2.name) }
    private val moduleClassMap = hashMapOf<Class<*>, Module>()
    var toggleSoundMode = 0
    var toggleChatMode = 0
    var toggleMessageMode = 0

    init {
        LiquidBounce.eventManager.registerListener(this)
    }

    /**
     * Register all modules
     */
    fun registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...")

        registerModules(
            AutoArmor::class.java,
            AutoBow::class.java,
            AutoLeave::class.java,
            AutoPot::class.java,
            AutoSoup::class.java,
            AutoWeapon::class.java,
            BowAimbot::class.java,
            Criticals::class.java,
            KillAura::class.java,
            Trigger::class.java,
            Velocity::class.java,
            Fly::class.java,
            ClickGUI::class.java,
            HighJump::class.java,
            InventoryMove::class.java,
            NoSlow::class.java,
            LiquidWalk::class.java,
            SafeWalk::class.java,
            WallClimb::class.java,
            Strafe::class.java,
            Sprint::class.java,
            Teams::class.java,
            NoRotateSet::class.java,
            ChestStealer::class.java,
            Scaffold::class.java,
            Scaffold2::class.java,
            Scaffold3::class.java,
            Scaffold4::class.java,
            CivBreak::class.java,
            Tower::class.java,
            FastBreak::class.java,
            FastPlace::class.java,
            ESP::class.java,
            Speed::class.java,
            Tracers::class.java,
            NameTags::class.java,
            FastUse::class.java,
            Teleport::class.java,
            Fullbright::class.java,
            ItemESP::class.java,
            StorageESP::class.java,
            Projectiles::class.java,
            NoClip::class.java,
            Nuker::class.java,
            PingSpoof::class.java,
            FastClimb::class.java,
            Step::class.java,
            AutoRespawn::class.java,
            AutoTool::class.java,
            NoWeb::class.java,
            Spammer::class.java,
            IceSpeed::class.java,
            Zoot::class.java,
            Regen::class.java,
            NoFall::class.java,
            Blink::class.java,
            NameProtect::class.java,
            NoHurtCam::class.java,
            Ghost::class.java,
            MidClick::class.java,
            XRay::class.java,
            Timer::class.java,
            Sneak::class.java,
            SkinDerp::class.java,
            GhostHand::class.java,
            AutoWalk::class.java,
            AutoBreak::class.java,
            FreeCam::class.java,
            Aimbot::class.java,
            Eagle::class.java,
            HitBox::class.java,
            AntiCactus::class.java,
            Plugins::class.java,
            AntiHunger::class.java,
            ConsoleSpammer::class.java,
            LongJump::class.java,
            Parkour::class.java,
            LadderJump::class.java,
            FastBow::class.java,
            MultiActions::class.java,
            AirJump::class.java,
            AutoClicker::class.java,
            NoBob::class.java,
            BlockOverlay::class.java,
            NoFriends::class.java,
            BlockESP::class.java,
            Chams::class.java,
            Clip::class.java,
            Phase::class.java,
            ServerCrasher::class.java,
            NoFOV::class.java,
            FastStairs::class.java,
            SwingAnimation::class.java,
            Derp::class.java,
            ReverseStep::class.java,
            TNTBlock::class.java,
            InventoryCleaner::class.java,
            TrueSight::class.java,
            LiquidChat::class.java,
            AntiBlind::class.java,
            NoSwing::class.java,
            BedGodMode::class.java,
            BugUp::class.java,
            Breadcrumbs::class.java,
            AbortBreaking::class.java,
            PotionSaver::class.java,
            CameraClip::class.java,
            WaterSpeed::class.java,
            Ignite::class.java,
            SlimeJump::class.java,
            MoreCarry::class.java,
            NoPitchLimit::class.java,
            Kick::class.java,
            Liquids::class.java,
            AtAllProvider::class.java,
            AirLadder::class.java,
            GodMode::class.java,
            TeleportHit::class.java,
            ForceUnicodeChat::class.java,
            ItemTeleport::class.java,
            BufferSpeed::class.java,
            SuperKnockback::class.java,
            ProphuntESP::class.java,
            AutoFish::class.java,
            Damage::class.java,
            Freeze::class.java,
            KeepContainer::class.java,
            VehicleOneHit::class.java,
            Reach::class.java,
            Rotations::class.java,
            NoJumpDelay::class.java,
            BlockWalk::class.java,
            AntiAFK::class.java,
            PerfectHorseJump::class.java,
            HUD::class.java,
            TNTESP::class.java,
            ComponentOnHover::class.java,
            KeepAlive::class.java,
            ResourcePackSpoof::class.java,
            NoSlowBreak::class.java,
            PortalMenu::class.java,
            Animations::class.java,
            StrafeFix::class.java,
            GroundTelly::class.java,
            Velocity2::class.java,
            AuraRangeHelper::class.java,
            AutoGG::class.java,
            Disabler::class.java,
            AntiFakePlayer::class.java,
            Title::class.java,
            HytAntiVoid::class.java,
            HytPlugin::class.java,
            CustomAntiKb::class.java,
            HudDesigner::class.java,
            DMGParticle::class.java,
            AutoReport::class.java,
            TNTTime::class.java,
            AutoL::class.java,
            Recording::class.java,
            GrimCivBreak::class.java,
            TargetStrafe::class.java,
       //     LegitAura::class.java,
            AntiAim::class.java,
            InfiniteAura::class.java,
            PotionSpoof::class.java
        )

        registerModule(NoScoreboard)
        registerModule(Fucker)
        registerModule(ChestAura)
        registerModule(AntiBot)
        LiquidBounce.eventManager.registerListener(ClientSpoofer())

        ClientUtils.getLogger().info("[ModuleManager] Loaded ${modules.size} modules.")
    }

    /**
     * Register [module]
     */
    fun registerModule(module: Module) {
        if (!module.isSupported)
            return

        modules += module
        moduleClassMap[module.javaClass] = module

        generateCommand(module)
        LiquidBounce.eventManager.registerListener(module)
    }

    /**
     * Register [moduleClass]
     */
    private fun registerModule(moduleClass: Class<out Module>) {
        try {
            registerModule(moduleClass.newInstance())
        } catch (e: Throwable) {
            ClientUtils.getLogger().error("Failed to load module: ${moduleClass.name} (${e.javaClass.name}: ${e.message})")
        }
    }

    /**
     * Register a list of modules
     */
    @SafeVarargs
    fun registerModules(vararg modules: Class<out Module>) {
        modules.forEach(this::registerModule)
    }

    /**
     * Unregister module
     */
    fun unregisterModule(module: Module) {
        modules.remove(module)
        moduleClassMap.remove(module::class.java)
        LiquidBounce.eventManager.unregisterListener(module)
    }

    /**
     * Generate command for [module]
     */
    internal fun generateCommand(module: Module) {
        val values = module.values

        if (values.isEmpty())
            return

        LiquidBounce.commandManager.registerCommand(ModuleCommand(module, values))
    }

    /**
     * Legacy stuff
     *
     * TODO: Remove later when everything is translated to Kotlin
     */

    /**
     * Get module by [moduleClass]
     */
    fun getModule(moduleClass: Class<*>) = moduleClassMap[moduleClass]!!

    operator fun get(clazz: Class<*>) = getModule(clazz)

    /**
     * Get module by [moduleName]
     */
    fun getModule(moduleName: String?) = modules.find { it.name.equals(moduleName, ignoreCase = true) }

    /**
     * Module related events
     */

    /**
     * Handle incoming key presses
     */
    @EventTarget
    private fun onKey(event: KeyEvent) = modules.filter { it.keyBind == event.key }.forEach { it.toggle() }

    override fun handleEvents() = true
}
