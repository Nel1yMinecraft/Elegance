/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.item

import me.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial

interface IItemArmor : IItem {
    val armorMaterial: IArmorMaterial
    val armorType: Int
    fun getColor(stack: IItemStack): Int

}