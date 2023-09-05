package me.nelly

import me.ccbluex.liquidbounce.LiquidBounce
import me.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import me.ccbluex.liquidbounce.event.AttackEvent
import me.ccbluex.liquidbounce.event.EventTarget
import me.ccbluex.liquidbounce.event.PacketEvent
import me.ccbluex.liquidbounce.event.UpdateEvent
import me.ccbluex.liquidbounce.features.module.Module
import me.ccbluex.liquidbounce.features.module.ModuleCategory
import me.ccbluex.liquidbounce.features.module.ModuleInfo
import me.ccbluex.liquidbounce.injection.backend.unwrap
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import me.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import me.ccbluex.liquidbounce.value.BoolValue
import me.ccbluex.liquidbounce.value.ListValue
import me.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.SPacketChat
import java.util.regex.Pattern

@ModuleInfo(name = "AutoL", description = "NELLY", category = ModuleCategory.MISC)
class AutoL : Module() {
    private val killautoL = BoolValue("KillPlayerAutoL", true)
    private val killautoLmode = ListValue("KillPlayerAutoL-Mode", arrayOf("SendMessage", "AddNotification"), "AddNotification").displayable {killautoL.get()}
    private val killutolmessage = TextValue("KillPlayerAutoL-Message", "我可是${LiquidBounce.CLIENT_NAME}用户 |").displayable {killautoLmode.get().contains("SendMessage")}
    private val banAutoL = BoolValue("BanAutoL", true)
    private val banautoLmode = ListValue("BanAutoL-Mode", arrayOf("SendMessage", "AddNotification"), "AddNotification").displayable {banAutoL.get()}
    private val banautolmessage = TextValue("BanAutoL-Message", "我可是${LiquidBounce.CLIENT_NAME}用户 |").displayable {banautoLmode.get().contains("SendMessage")}
    private val hubAutoL = BoolValue("HubAutoL", true)
    private val hubautoLmode = ListValue("HubAutoL-Mode", arrayOf("SendMessage", "AddNotification"), "AddNotification").displayable {banAutoL.get()}
    private val hubautolmessage = TextValue("HubAutoL-Message", "我可是${LiquidBounce.CLIENT_NAME}用户 |").displayable {banautoLmode.get().contains("SendMessage")}
    var syncEntity: IEntityLivingBase? = null
    var killCounts = 0
    var ban = 0
    var hub = 0

    @EventTarget
    fun onAttack(event: AttackEvent) {
        syncEntity = event.targetEntity as IEntityLivingBase?
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if(syncEntity!!.isDead && syncEntity != mc2.player){
            ++killCounts
            killautoL()
            syncEntity = null
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (packet is SPacketChat) {
            val matcher2 = Pattern.compile("离开了游戏").matcher(packet.chatComponent.unformattedText)
            if (matcher2.find()) {
                hub++
                val hubname = matcher2.group(1)
                if (hubAutoL.get() && hubautoLmode.get().contains("AddNotification")) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            "HubChecker",
                            "${hubautolmessage.get()} $hubname has been banned. Total bans: $hub",
                            NotifyType.INFO
                        )
                    )
                }
                if(hubAutoL.get()  && hubautoLmode.get().contains("SendMessage")) {
                    mc.thePlayer!!.sendChatMessage("@${hubautolmessage.get()} $hubname 已被封禁")
                }
            }
            val matcher = Pattern.compile("玩家(.*?)在本局游戏中行为异常").matcher(packet.chatComponent.unformattedText)
            if (matcher.find()) {
                ban++
                val banname = matcher.group(1)
                if (banAutoL.get() && banautoLmode.get().contains("AddNotification")) {
                    LiquidBounce.hud.addNotification(
                        Notification(
                            "BanChecker",
                            "${banautolmessage.get()} $banname has been banned. Total bans: $ban",
                            NotifyType.INFO
                        )
                    )
                }
                if(banAutoL.get()  && banautoLmode.get().contains("SendMessage")) {
                    mc.thePlayer!!.sendChatMessage("@${killutolmessage.get()} $banname 已被封禁")
                }
            }
        }
    }

    fun killautoL() {
        if(killautoL.get() && killautoLmode.get().contains("SendMessage")) {
            mc.thePlayer!!.sendChatMessage("@${killutolmessage.get()} ${syncEntity!!.name} 已被封禁")
        }
        if(killautoL.get() && killautoLmode.get().contains("AddNotification")) {
            LiquidBounce.hud.addNotification(
                Notification(
                    "AutoL",
                    "${syncEntity!!.name} has been banned. Total bans: $ban",
                    NotifyType.INFO
                )
            )
        }
    }
}



