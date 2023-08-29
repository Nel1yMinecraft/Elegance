package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.misc.StringUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NellyAICommand : Command("chatai") {
    val apiUrl = "https://v2.api-m.com/api/turing?msg="

    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            try {
                val url = URL(apiUrl + StringUtils.toCompleteString(args, 1))

                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val jsonResult = response.toString()

                    if (jsonResult.contains("\"msg\":\"数据请求成功\"")) {
                        val data = jsonResult.substring(
                            jsonResult.lastIndexOf("\"data\":\"") + 8,
                            jsonResult.lastIndexOf("\"}")
                        )
                        ClientUtils.displayChatMessage("AI：$data")
                    } else {
                        ClientUtils.displayChatMessage("AI初始化失败")
                    }
                } else {
                    ClientUtils.displayChatMessage("请求失败，错误码：$responseCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        chatSyntax("chatai <message...>")
    }
}