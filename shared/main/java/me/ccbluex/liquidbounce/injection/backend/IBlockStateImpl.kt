/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState
import me.ccbluex.liquidbounce.api.minecraft.client.block.IBlock
import net.minecraft.block.state.IBlockState

class IBlockStateImpl(val wrapped: IBlockState) : IIBlockState {
    override val block: IBlock
        get() = BlockImpl(wrapped.block)


    override fun equals(other: Any?): Boolean {
        return other is IBlockStateImpl && other.wrapped == this.wrapped
    }
}

inline fun IIBlockState.unwrap(): IBlockState = (this as IBlockStateImpl).wrapped
inline fun IBlockState.wrap(): IIBlockState = IBlockStateImpl(this)