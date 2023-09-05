/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.utils

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import me.ccbluex.liquidbounce.features.module.modules.combat.NoFriends
import me.ccbluex.liquidbounce.features.module.modules.misc.AntiBot.isBot
import me.ccbluex.liquidbounce.features.module.modules.misc.Teams
import me.ccbluex.liquidbounce.utils.extensions.isAnimal
import me.ccbluex.liquidbounce.utils.extensions.isClientFriend
import me.ccbluex.liquidbounce.utils.extensions.isMob

object EntityUtils : MinecraftInstance() {

    @JvmField
    var targetInvisible = false

    @JvmField
    var targetPlayer = true

    @JvmField
    var targetMobs = true

    @JvmField
    var targetAnimals = false

    @JvmField
    var targetDead = false

    @JvmStatic
    fun isSelected(entity: IEntity?, canAttackCheck: Boolean): Boolean {
        if (classProvider.isEntityLivingBase(entity) && (targetDead || entity!!.entityAlive) && entity != null
                && entity != mc.thePlayer) {
            if (targetInvisible || !entity.invisible) {
                if (targetPlayer && classProvider.isEntityPlayer(entity)) {
                    val entityPlayer = entity.asEntityPlayer()

                    if (canAttackCheck) {
                        if (isBot(entityPlayer))
                            return false

                        if (entityPlayer.isClientFriend() && !LiquidBounce.moduleManager.getModule(NoFriends::class.java).state)
                            return false

                        if (entityPlayer.spectator) return false
                        val teams = LiquidBounce.moduleManager.getModule(Teams::class.java) as Teams
                        return !teams.state || !teams.isInYourTeam(entityPlayer)
                    }
                    return true
                }

                return targetMobs && entity.isMob() || targetAnimals && entity.isAnimal()
            }
        }
        return false
    }

}