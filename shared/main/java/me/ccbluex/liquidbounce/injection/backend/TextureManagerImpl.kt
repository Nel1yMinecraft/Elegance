/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture
import me.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager
import me.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.minecraft.client.renderer.texture.TextureManager

class TextureManagerImpl(val wrapped: TextureManager) : ITextureManager {
    override fun loadTexture(textureLocation: IResourceLocation, textureObj: IAbstractTexture): Boolean = wrapped.loadTexture(textureLocation.unwrap(), textureObj.unwrap())

    override fun bindTexture(image: IResourceLocation) = wrapped.bindTexture(image.unwrap())

    override fun equals(other: Any?): Boolean {
        return other is TextureManagerImpl && other.wrapped == this.wrapped
    }
}

inline fun ITextureManager.unwrap(): TextureManager = (this as TextureManagerImpl).wrapped
inline fun TextureManager.wrap(): ITextureManager = TextureManagerImpl(this)