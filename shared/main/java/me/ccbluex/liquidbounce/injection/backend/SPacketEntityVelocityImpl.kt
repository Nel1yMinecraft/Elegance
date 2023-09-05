/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity
import net.minecraft.network.play.server.SPacketEntityVelocity

class SPacketEntityVelocityImpl<T : SPacketEntityVelocity>(wrapped: T) : PacketImpl<T>(wrapped), ISPacketEntityVelocity {
    override var motionX: Int
        get() = wrapped.motionX
        set(value) {
            wrapped.motionX = value
        }
    override var motionY: Int
        get() = wrapped.motionY
        set(value) {
            wrapped.motionY = value
        }
    override var motionZ: Int
        get() = wrapped.motionZ
        set(value) {
            wrapped.motionZ = value
        }
    override val entityID: Int
        get() = wrapped.entityID

}

inline fun ISPacketEntityVelocity.unwrap(): SPacketEntityVelocity = (this as SPacketEntityVelocityImpl<*>).wrapped
inline fun SPacketEntityVelocity.wrap(): ISPacketEntityVelocity = SPacketEntityVelocityImpl(this)