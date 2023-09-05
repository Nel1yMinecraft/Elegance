/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.util.ITimer
import me.ccbluex.liquidbounce.injection.implementations.IMixinTimer
import net.minecraft.util.Timer

class TimerImpl(val wrapped: Timer) : ITimer {
    override var timerSpeed: Float
        get() = (wrapped as IMixinTimer).timerSpeed
        set(value) {
            (wrapped as IMixinTimer).timerSpeed = value
        }
    override val renderPartialTicks: Float
        get() = wrapped.renderPartialTicks

    override fun equals(other: Any?): Boolean {
        return other is TimerImpl && other.wrapped == this.wrapped
    }
}

inline fun ITimer.unwrap(): Timer = (this as TimerImpl).wrapped
inline fun Timer.wrap(): ITimer = TimerImpl(this)