package me.nelly.fdp.clientspoof

import io.netty.buffer.Unpooled
import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.utils.render.ColorUtils
import me.ccbluex.liquidbounce.value.ListValue
import me.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.CPacketCustomPayload

// Made By Zywl // Fix By nelly
@ModuleInfo(name = "ClientSpoof", description = "ClientSpoof", category = ModuleCategory.MISC)
class ClientSpoof : Module() {
    val modeValue = ListValue(
        "Mode", arrayOf(
            "Vanilla",
            "Forge",
            "Lunar",
            "LabyMod",
            "Germ",
            "Rise",
            "CheatBreaker",
            "PvPLounge",
            "Custom"
        ), "Vanilla"
    )
    val customClientName = TextValue("CustomClientName", "ColorByte").displayable { modeValue.get().equals("Custom") }

    override val tag: String
        get() = modeValue.get()

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        val clientSpoof = LiquidBounce.moduleManager.getModule(ClientSpoof::class.java) as ClientSpoof

        if (ClientSpoofer.enabled && !mc2.isIntegratedServerRunning) {
            try {
                if (packet is CPacketCustomPayload) {
                    val payload = PacketBuffer(Unpooled.buffer())
                    val payloadData = when (clientSpoof.modeValue.get()) {
                        "Vanilla" -> "vanilla"
                        "LabyMod" -> "LMC"
                        "Germ" -> "Forge-germmod"
                        "Rise" -> "Rise"
                        "CheatBreaker" -> "CB"
                        "Lunar" -> "Lunar-Client"
                        "PvPLounge" -> "PLC18"
                        "Custom" -> ColorUtils.translateAlternateColorCodes(clientSpoof.customClientName.get())
                        else -> null
                    }
                    payloadData?.let { payload.writeString(it); packet.data = payload }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}