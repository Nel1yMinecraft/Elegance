/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing
import me.ccbluex.liquidbounce.api.minecraft.util.WVec3i
import me.ccbluex.liquidbounce.injection.backend.utils.wrap
import net.minecraft.util.EnumFacing

class EnumFacingImpl(val wrapped: EnumFacing) : IEnumFacing {
    override fun isNorth(): Boolean = wrapped == EnumFacing.NORTH

    override fun isSouth(): Boolean = wrapped == EnumFacing.SOUTH

    override fun isEast(): Boolean = wrapped == EnumFacing.EAST

    override fun isWest(): Boolean = wrapped == EnumFacing.WEST

    override fun isUp(): Boolean = wrapped == EnumFacing.UP

    override val opposite: IEnumFacing
        get() = wrapped.opposite.wrap()
    override val directionVec: WVec3i
        get() = wrapped.directionVec.wrap()
    override val axisOrdinal: Int
        get() = wrapped.axis.ordinal

    override fun equals(other: Any?): Boolean {
        return other is EnumFacingImpl && other.wrapped == this.wrapped
    }
}

inline fun IEnumFacing.unwrap(): EnumFacing = (this as EnumFacingImpl).wrapped
inline fun EnumFacing.wrap(): IEnumFacing = EnumFacingImpl(this)