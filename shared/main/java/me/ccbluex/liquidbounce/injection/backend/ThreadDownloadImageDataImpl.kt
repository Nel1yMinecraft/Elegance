/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.client.render.IThreadDownloadImageData
import net.minecraft.client.renderer.ThreadDownloadImageData

class ThreadDownloadImageDataImpl<T : ThreadDownloadImageData>(wrapped: T) : AbstractTextureImpl<T>(wrapped), IThreadDownloadImageData {
    override fun equals(other: Any?): Boolean {
        return other is ThreadDownloadImageDataImpl<*> && other.wrapped == this.wrapped
    }
}

inline fun IThreadDownloadImageData.unwrap(): ThreadDownloadImageData = (this as ThreadDownloadImageDataImpl<*>).wrapped
inline fun ThreadDownloadImageData.wrap(): IThreadDownloadImageData = ThreadDownloadImageDataImpl(this)