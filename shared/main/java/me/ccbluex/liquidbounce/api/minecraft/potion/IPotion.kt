/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.potion

interface IPotion {
    val liquidColor: Int
    val id: Int
    val name: String
    val hasStatusIcon: Boolean
    val statusIconIndex: Int
}