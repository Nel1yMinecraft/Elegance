/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.api.minecraft.client.entity.player

import com.mojang.authlib.GameProfile
import me.ccbluex.liquidbounce.api.MinecraftVersion
import me.ccbluex.liquidbounce.api.SupportsMinecraftVersions
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import me.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer
import me.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities
import me.ccbluex.liquidbounce.api.minecraft.inventory.IContainer
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import me.ccbluex.liquidbounce.api.minecraft.stats.IStatBase
import me.ccbluex.liquidbounce.api.minecraft.util.IFoodStats

@Suppress("INAPPLICABLE_JVM_NAME")
interface IEntityPlayer : IEntityLivingBase {
    val gameProfile: GameProfile
    val fishEntity: IEntity?
    val foodStats: IFoodStats
    val prevChasingPosY: Double
    var sleepTimer: Int
    var sleeping: Boolean
    val isPlayerSleeping: Boolean
    var speedInAir: Float
    var cameraYaw: Float

    @get:JvmName("isBlocking")
    val isBlocking: Boolean
    var itemInUseCount: Int
    val itemInUse: IItemStack?
    val capabilities: IPlayerCapabilities
    val heldItem: IItemStack?
    val isUsingItem: Boolean
    val inventoryContainer: IContainer
    val inventory: IInventoryPlayer
    val openContainer: IContainer?
    val itemInUseDuration: Int
    val displayNameString: String

    @get:JvmName("isSpectator")
    val spectator: Boolean


    fun stopUsingItem()
    fun onCriticalHit(entity: IEntity)
    fun onEnchantmentCritical(entity: IEntityLivingBase)
    fun attackTargetEntityWithCurrentItem(entity: IEntity)
    fun fall(distance: Float, damageMultiplier: Float)
    fun triggerAchievement(stat: IStatBase)
    fun jump()

    @SupportsMinecraftVersions(value = [MinecraftVersion.MC_1_12])
    fun getCooledAttackStrength(fl: Float): Float
    fun resetCooldown()
}