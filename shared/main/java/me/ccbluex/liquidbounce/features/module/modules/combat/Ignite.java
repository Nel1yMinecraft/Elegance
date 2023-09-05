/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.combat;

import me.ccbluex.liquidbounce.api.enums.EnumFacingType;
import me.ccbluex.liquidbounce.api.enums.ItemType;
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import me.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import me.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import me.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import me.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import me.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import me.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import me.ccbluex.liquidbounce.event.EventTarget;
import me.ccbluex.liquidbounce.event.UpdateEvent;
import me.ccbluex.liquidbounce.features.module.Module;
import me.ccbluex.liquidbounce.features.module.ModuleCategory;
import me.ccbluex.liquidbounce.features.module.ModuleInfo;
import me.ccbluex.liquidbounce.utils.EntityUtils;
import me.ccbluex.liquidbounce.utils.InventoryUtils;
import me.ccbluex.liquidbounce.utils.RotationUtils;
import me.ccbluex.liquidbounce.utils.block.BlockUtils;
import me.ccbluex.liquidbounce.utils.timer.MSTimer;
import me.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name = "Ignite", description = "Automatically sets targets around you on fire.", category = ModuleCategory.COMBAT)
public class Ignite extends Module {
   private final BoolValue lighterValue = new BoolValue("Lighter", true);
   private final BoolValue lavaBucketValue = new BoolValue("Lava", true);

   private final MSTimer msTimer = new MSTimer();

   @EventTarget
   public void onUpdate(final UpdateEvent event) {
       if (!msTimer.hasTimePassed(500L))
           return;

       IEntityPlayerSP thePlayer = mc.getThePlayer();
       IWorldClient theWorld = mc.getTheWorld();

       if (thePlayer == null || theWorld == null)
           return;

       final int lighterInHotbar =
               lighterValue.get() ? InventoryUtils.findItem(36, 45, classProvider.getItemEnum(ItemType.FLINT_AND_STEEL)) : -1;
       final int lavaInHotbar =
               lavaBucketValue.get() ? InventoryUtils.findItem(26, 45, classProvider.getItemEnum(ItemType.LAVA_BUCKET)) : -1;

       if (lighterInHotbar == -1 && lavaInHotbar == -1)
           return;

       final int fireInHotbar = lighterInHotbar != -1 ? lighterInHotbar : lavaInHotbar;

       for (final IEntity entity : theWorld.getLoadedEntityList()) {
           if (EntityUtils.isSelected(entity, true) && !entity.isBurning()) {
               WBlockPos blockPos = entity.getPosition();

               if (mc.getThePlayer().getDistanceSq(blockPos) >= 22.3D ||
                       !BlockUtils.isReplaceable(blockPos) ||
                       !classProvider.isBlockAir(BlockUtils.getBlock(blockPos)))
                   continue;

               RotationUtils.keepCurrentRotation = true;

               mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(fireInHotbar - 36));

               final IItemStack itemStack = mc.getThePlayer().getInventory().getStackInSlot(fireInHotbar);

               if (classProvider.isItemBucket(itemStack.getItem())) {
                   final double diffX = blockPos.getX() + 0.5D - mc.getThePlayer().getPosX();
                   final double diffY = blockPos.getY() + 0.5D -
                           (thePlayer.getEntityBoundingBox().getMinY() +
                                   thePlayer.getEyeHeight());
                   final double diffZ = blockPos.getZ() + 0.5D - thePlayer.getPosZ();
                   final double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
                   final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90F;
                   final float pitch = (float) -(Math.atan2(diffY, sqrt) * 180.0D / Math.PI);

                   mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerLook(
                           thePlayer.getRotationYaw() +
                                   WMathHelper.wrapAngleTo180_float(yaw - thePlayer.getRotationYaw()),
                           thePlayer.getRotationPitch() +
                                   WMathHelper.wrapAngleTo180_float(pitch - thePlayer.getRotationPitch()),
                           thePlayer.getOnGround()));

                   mc.getPlayerController().sendUseItem(thePlayer, theWorld, itemStack);
               } else {
                   for (EnumFacingType enumFacingType : EnumFacingType.values()) {
                       IEnumFacing side = classProvider.getEnumFacing(enumFacingType);

                       final WBlockPos neighbor = blockPos.offset(side);

                       if (!BlockUtils.canBeClicked(neighbor)) continue;

                       final double diffX = neighbor.getX() + 0.5D - thePlayer.getPosX();
                       final double diffY = neighbor.getY() + 0.5D -
                               (thePlayer.getEntityBoundingBox().getMinY() +
                                       thePlayer.getEyeHeight());
                       final double diffZ = neighbor.getZ() + 0.5D - thePlayer.getPosZ();
                       final double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
                       final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90F;
                       final float pitch = (float) -(Math.atan2(diffY, sqrt) * 180.0D / Math.PI);

                       mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerLook(
                               thePlayer.getRotationYaw() +
                                       WMathHelper.wrapAngleTo180_float(yaw - thePlayer.getRotationYaw()),
                               thePlayer.getRotationPitch() +
                                       WMathHelper.wrapAngleTo180_float(pitch - thePlayer.getRotationPitch()),
                               thePlayer.getOnGround()));

                       if (mc.getPlayerController().onPlayerRightClick(thePlayer, theWorld, itemStack, neighbor,
                               side.getOpposite(), new WVec3(side.getDirectionVec()))) {
                           thePlayer.swingItem();
                           break;
                       }
                   }
               }

               mc.getNetHandler()
                       .addToSendQueue(classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
               RotationUtils.keepCurrentRotation = false;
               mc.getNetHandler().addToSendQueue(
                       classProvider.createCPacketPlayerLook(thePlayer.getRotationYaw(), thePlayer.getRotationPitch(), thePlayer.getOnGround())
               );

               msTimer.reset();
               break;
           }
      }
   }
}
