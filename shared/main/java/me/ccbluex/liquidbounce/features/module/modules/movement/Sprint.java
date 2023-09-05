/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.module.modules.movement;

import me.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import me.ccbluex.liquidbounce.event.EventTarget;
import me.ccbluex.liquidbounce.event.UpdateEvent;
import me.ccbluex.liquidbounce.features.module.Module;
import me.ccbluex.liquidbounce.features.module.ModuleCategory;
import me.ccbluex.liquidbounce.features.module.ModuleInfo;
import me.ccbluex.liquidbounce.utils.MovementUtils;
import me.ccbluex.liquidbounce.utils.Rotation;
import me.ccbluex.liquidbounce.utils.RotationUtils;
import me.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name = "Sprint", description = "Automatically sprints all the time.", category = ModuleCategory.MOVEMENT)
public class Sprint extends Module {

    public final BoolValue allDirectionsValue = new BoolValue("AllDirections", true);
    public final BoolValue blindnessValue = new BoolValue("Blindness", true);
    public final BoolValue foodValue = new BoolValue("Food", true);

    public final BoolValue checkServerSide = new BoolValue("CheckServerSide", false);
    public final BoolValue checkServerSideGround = new BoolValue("CheckServerSideOnlyGround", false);

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!MovementUtils.isMoving() || mc.getThePlayer().isSneaking() ||
                (blindnessValue.get() && mc.getThePlayer().isPotionActive(classProvider.getPotionEnum(PotionType.BLINDNESS))) ||
                (foodValue.get() && !(mc.getThePlayer().getFoodStats().getFoodLevel() > 6.0F || mc.getThePlayer().getCapabilities().getAllowFlying()))
                || (checkServerSide.get() && (mc.getThePlayer().getOnGround() || !checkServerSideGround.get())
                && !allDirectionsValue.get() && RotationUtils.targetRotation != null &&
                RotationUtils.getRotationDifference(new Rotation(mc.getThePlayer().getRotationYaw(), mc.getThePlayer().getRotationPitch())) > 30)) {
            mc.getThePlayer().setSprinting(false);
            return;
        }

        if (allDirectionsValue.get() || mc.getThePlayer().getMovementInput().getMoveForward() >= 0.8F)
            mc.getThePlayer().setSprinting(true);
    }
}
