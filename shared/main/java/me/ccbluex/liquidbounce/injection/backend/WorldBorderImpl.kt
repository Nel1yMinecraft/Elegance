/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.api.minecraft.world.border.IWorldBorder
import me.ccbluex.liquidbounce.injection.backend.utils.unwrap
import net.minecraft.world.border.WorldBorder

class WorldBorderImpl(val wrapped: WorldBorder) : IWorldBorder {
    override fun contains(blockPos: WBlockPos): Boolean = wrapped.contains(blockPos.unwrap())

    override fun equals(other: Any?): Boolean {
        return other is WorldBorderImpl && other.wrapped == this.wrapped
    }
}

inline fun IWorldBorder.unwrap(): WorldBorder = (this as WorldBorderImpl).wrapped
inline fun WorldBorder.wrap(): IWorldBorder = WorldBorderImpl(this)