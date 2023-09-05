/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer
import me.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient
import me.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import me.ccbluex.liquidbounce.api.util.WrappedCollection
import me.ccbluex.liquidbounce.injection.backend.utils.unwrap
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity

class WorldClientImpl(wrapped: WorldClient) : WorldImpl<WorldClient>(wrapped), IWorldClient {
    override val playerEntities: Collection<IEntityPlayer>
        get() = WrappedCollection(wrapped.playerEntities, IEntityPlayer::unwrap, EntityPlayer::wrap)
    override val loadedEntityList: Collection<IEntity>
        get() = WrappedCollection(wrapped.loadedEntityList, IEntity::unwrap, Entity::wrap)
    override val loadedTileEntityList: Collection<ITileEntity>
        get() = WrappedCollection(wrapped.loadedTileEntityList, ITileEntity::unwrap, TileEntity::wrap)

    override fun sendQuittingDisconnectingPacket() = wrapped.sendQuittingDisconnectingPacket()

    override fun sendBlockBreakProgress(entityId: Int, blockPos: WBlockPos, damage: Int) = wrapped.sendBlockBreakProgress(entityId, blockPos.unwrap(), damage)

    override fun addEntityToWorld(entityId: Int, fakePlayer: IEntity) = wrapped.addEntityToWorld(entityId, fakePlayer.unwrap())

    override fun removeEntityFromWorld(entityId: Int) {
        wrapped.removeEntityFromWorld(entityId)
    }

}

inline fun IWorldClient.unwrap(): WorldClient = (this as WorldClientImpl).wrapped
inline fun WorldClient.wrap(): IWorldClient = WorldClientImpl(this)