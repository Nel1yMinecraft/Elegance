/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.client.renderer

import me.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup
import me.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation

interface IEntityRenderer {
    val shaderGroup: IShaderGroup?

    fun disableLightmap()
    fun isShaderActive(): Boolean
    fun loadShader(resourceLocation: IResourceLocation)
    fun stopUseShader()
    fun setupCameraTransform(partialTicks: Float, i: Int)
    fun setupOverlayRendering()
}