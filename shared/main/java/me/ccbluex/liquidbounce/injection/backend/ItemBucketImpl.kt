/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.client.block.IBlock
import me.ccbluex.liquidbounce.api.minecraft.item.IItemBucket
import net.minecraft.item.ItemBucket

class ItemBucketImpl(wrapped: ItemBucket) : ItemImpl<ItemBucket>(wrapped), IItemBucket {
    override val isFull: IBlock
        get() = BlockImpl(wrapped.containedBlock)
}