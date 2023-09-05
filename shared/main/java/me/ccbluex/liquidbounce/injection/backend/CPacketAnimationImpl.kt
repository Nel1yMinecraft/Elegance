/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketAnimation
import net.minecraft.network.play.client.CPacketAnimation

class CPacketAnimationImpl<T : CPacketAnimation>(wrapped: T) : PacketImpl<T>(wrapped), ICPacketAnimation {

}

inline fun ICPacketAnimation.unwrap(): CPacketAnimation = (this as CPacketAnimationImpl<*>).wrapped
inline fun CPacketAnimation.wrap(): ICPacketAnimation = CPacketAnimationImpl(this)