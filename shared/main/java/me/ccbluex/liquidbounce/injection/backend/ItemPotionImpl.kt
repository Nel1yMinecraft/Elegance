/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.item.IItemPotion
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect
import me.ccbluex.liquidbounce.api.util.WrappedCollection
import net.minecraft.item.ItemPotion
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils

class ItemPotionImpl(wrapped: ItemPotion) : ItemImpl<ItemPotion>(wrapped), IItemPotion {
    override fun getEffects(stack: IItemStack): Collection<IPotionEffect> {
        return WrappedCollection(
                PotionUtils.getEffectsFromStack(stack.unwrap()),
                IPotionEffect::unwrap,
                PotionEffect::wrap
        )
    }
}


