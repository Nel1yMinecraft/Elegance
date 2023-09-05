/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.world

import me.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP
import me.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos

interface IChunk {
    val x: Int
    val z: Int

    fun getEntitiesWithinAABBForEntity(thePlayer: IEntityPlayerSP, arrowBox: IAxisAlignedBB, collidedEntities: MutableList<IEntity>, nothing: Nothing?)
    fun getHeightValue(x: Int, z: Int): Int
    fun getBlockState(blockPos: WBlockPos): IIBlockState
}