package com.criptoyatelegrambot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.Update
import kotlinx.coroutines.*

private val exchanges = mapOf(
    "buenbit" to "BuenBit", "qubit" to "Qubit", "ripio" to "Ripio", "ripioexchange" to "Ripio Exchange",
    "satoshitango" to "Satoshi Tango", "sesocio" to "SeSocio"
)

fun main(args: Array<String>) {
    val bot = bot {
        token = "..."
        dispatch {
            command("start") { bot, update ->
                val chatId = update.getId() ?: return@command
                bot.sendMessage(chatId, "Usa /dolarDai para ver el precio de Dai en pesos")
            }
            command("dolarDai") { bot, update ->
                val chatId = update.getId() ?: return@command
                GlobalScope.launch {
                    val list = exchanges
                        .map { async { transformResponse(it) } }.awaitAll()
                    bot.sendMessage(chatId, list.joinToString(separator = "\n"))
                }
            }
        }
    }.startPolling()
}

suspend fun transformResponse(exchange: Map.Entry<String, String>): String? {
    val currencyValues = CriptoYaApi.criptoYaApiService.getDaiPrices(exchange.key)
    return if (currencyValues != null) {
        return "${exchange.value} -> $${currencyValues.totalAsk}"
    } else null
}

private fun Update.getId() = message?.chat?.id
