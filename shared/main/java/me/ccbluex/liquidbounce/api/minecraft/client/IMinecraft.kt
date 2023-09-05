/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.client

import me.ccbluex.liquidbounce.api.minecraft.client.audio.ISoundHandler
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP
import me.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import me.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen
import me.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP
import me.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData
import me.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient
import me.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient
import me.ccbluex.liquidbounce.api.minecraft.client.render.entity.IRenderItem
import me.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager
import me.ccbluex.liquidbounce.api.minecraft.client.renderer.IEntityRenderer
import me.ccbluex.liquidbounce.api.minecraft.client.renderer.IRenderGlobal
import me.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings
import me.ccbluex.liquidbounce.api.minecraft.client.shader.IFramebuffer
import me.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager
import me.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition
import me.ccbluex.liquidbounce.api.minecraft.util.ISession
import me.ccbluex.liquidbounce.api.minecraft.util.ITimer
import java.io.File

interface IMinecraft {
    val framebuffer: IFramebuffer
    val isFullScreen: Boolean
    val dataDir: File
    val debugFPS: Int
    val renderGlobal: IRenderGlobal
    val renderItem: IRenderItem
    val displayWidth: Int
    val displayHeight: Int
    val entityRenderer: IEntityRenderer
    var rightClickDelayTimer: Int
    var session: ISession
    val soundHandler: ISoundHandler
    val objectMouseOver: IMovingObjectPosition?
    val timer: ITimer
    val renderManager: IRenderManager
    val playerController: IPlayerControllerMP
    val currentScreen: IGuiScreen?
    var renderViewEntity: IEntity?
    val netHandler: IINetHandlerPlayClient
    val theWorld: IWorldClient?
    val thePlayer: IEntityPlayerSP?
    val textureManager: ITextureManager
    val isIntegratedServerRunning: Boolean
    val currentServerData: IServerData?
    val gameSettings: IGameSettings
    val fontRendererObj: IFontRenderer

    fun displayGuiScreen(screen: IGuiScreen?)
    fun rightClickMouse()
    fun shutdown()
    fun toggleFullscreen()
}