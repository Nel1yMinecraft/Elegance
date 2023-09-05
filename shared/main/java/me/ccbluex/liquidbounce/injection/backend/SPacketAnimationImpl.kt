/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation
import net.minecraft.network.play.server.SPacketAnimation

class SPacketAnimationImpl<T : SPacketAnimation>(wrapped: T) : PacketImpl<T>(wrapped), ISPacketAnimation {
    override val animationType: Int
        get() = wrapped.animationType
    override val entityID: Int
        get() = wrapped.entityID
}

inline fun ISPacketAnimation.unwrap(): SPacketAnimation = (this as SPacketAnimationImpl<*>).wrapped
inline fun SPacketAnimation.wrap(): ISPacketAnimation = SPacketAnimationImpl(this)