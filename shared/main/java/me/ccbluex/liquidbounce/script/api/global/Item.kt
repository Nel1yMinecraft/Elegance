/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.script.api.global

import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.utils.item.ItemUtils

/**
 * Object used by the script API to provide an easier way of creating items.
 */
object Item {

    /**
     * Creates an item.
     * @param itemArguments Arguments describing the item.
     * @return An instance of [ItemStack] with the given data.
     */
    @JvmStatic
    fun create(itemArguments: String): IItemStack = ItemUtils.createItem(itemArguments)
}