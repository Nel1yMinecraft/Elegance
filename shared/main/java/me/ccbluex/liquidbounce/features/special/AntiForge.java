/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.ccbluex.liquidbounce.features.special;

import io.netty.buffer.Unpooled;
import me.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import me.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import me.ccbluex.liquidbounce.event.EventTarget;
import me.ccbluex.liquidbounce.event.Listenable;
import me.ccbluex.liquidbounce.event.PacketEvent;
import me.ccbluex.liquidbounce.utils.MinecraftInstance;

public class AntiForge extends MinecraftInstance implements Listenable {

    public static boolean enabled = false;
    public static boolean blockFML = true;
    public static boolean blockProxyPacket = true;
    public static boolean blockPayloadPackets = true;

    @EventTarget
    public void onPacket(PacketEvent event) {
        final IPacket packet = event.getPacket();

        if (enabled && !mc.isIntegratedServerRunning()) {
            try {
                if (blockProxyPacket && packet.getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket"))
                    event.cancelEvent();

                if (blockPayloadPackets && classProvider.isCPacketCustomPayload(packet)) {
                    ICPacketCustomPayload customPayload = packet.asCPacketCustomPayload();

                    if (!customPayload.getChannelName().startsWith("MC|"))
                        event.cancelEvent();
                    else if (customPayload.getChannelName().equalsIgnoreCase("MC|Brand"))
                        customPayload.setData(classProvider.createPacketBuffer(Unpooled.buffer()).writeString("vanilla"));
                }
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}