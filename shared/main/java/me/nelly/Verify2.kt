package me.nelly

import net.ccbluex.liquidbounce.utils.misc.HttpUtils
import net.minecraft.client.Minecraft
import java.net.InetAddress
//import java.net.InetAddress
import java.net.InterfaceAddress
import javax.swing.JOptionPane

class Verify2 {
    companion object {
        fun getip(): String {
            val ip = HttpUtils.get("https://api.ipify.org/?format=txt")
            return ip
        }

        fun veirfy() {
            val myIP: InetAddress = InetAddress.getLocalHost()
            try {
                if (HttpUtils.get("https://gitcode.net/m0_62964839/vulgarsense/-/raw/master/test").contains(myIP.hostAddress,true)) {
                    print("ok")
                }
            } catch (e: Exception) {
                print(myIP.hostAddress)
            }
        }
    }
}