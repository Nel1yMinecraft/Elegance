/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketKeepAlive
import net.minecraft.network.play.client.CPacketKeepAlive

class CPacketKeepAliveImpl<T : CPacketKeepAlive>(wrapped: T) : PacketImpl<T>(wrapped), ICPacketKeepAlive {

}

inline fun ICPacketKeepAlive.unwrap(): CPacketKeepAlive = (this as CPacketKeepAliveImpl<*>).wrapped
inline fun CPacketKeepAlive.wrap(): ICPacketKeepAlive = CPacketKeepAliveImpl(this)