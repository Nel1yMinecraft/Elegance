/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api

import com.mojang.authlib.GameProfile
import me.ccbluex.liquidbounce.api.minecraft.client.block.IBlock
import me.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment
import me.ccbluex.liquidbounce.api.minecraft.entity.IEnumCreatureAttribute
import me.ccbluex.liquidbounce.api.minecraft.item.IItem
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.api.minecraft.potion.IPotion
import me.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam
import me.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity
import me.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent
import me.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation

interface IExtractedFunctions {
    fun getBlockById(id: Int): IBlock?
    fun getIdFromBlock(block: IBlock): Int
    fun getModifierForCreature(heldItem: IItemStack?, creatureAttribute: IEnumCreatureAttribute): Float
    fun getObjectFromItemRegistry(res: IResourceLocation): IItem?
    fun renderTileEntity(tileEntity: ITileEntity, partialTicks: Float, destroyStage: Int)
    fun getBlockFromName(name: String): IBlock?
    fun getItemByName(name: String): IItem?
    fun getEnchantmentByLocation(location: String): IEnchantment?
    fun getEnchantmentById(enchantID: Int): IEnchantment?
    fun getEnchantments(): Collection<IResourceLocation>
    fun getItemRegistryKeys(): Collection<IResourceLocation>
    fun getBlockRegistryKeys(): Collection<IResourceLocation>
    fun disableStandardItemLighting()
    fun formatI18n(key: String, vararg values: String): String
    fun sessionServiceJoinServer(profile: GameProfile, token: String, sessionHash: String)
    fun getPotionById(potionID: Int): IPotion
    fun enableStandardItemLighting()
    fun scoreboardFormatPlayerName(scorePlayerTeam: ITeam?, playerName: String): String
    fun disableFastRender()
    fun jsonToComponent(toString: String): IIChatComponent
    fun setActiveTextureLightMapTexUnit()
    fun setActiveTextureDefaultTexUnit()

}
