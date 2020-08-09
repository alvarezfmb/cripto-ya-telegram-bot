package com.criptoyatelegrambot

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyValuesDTO(val ask: String,
                             val totalAsk: String,
                             val bid: String,
                             val totalBid : String,
                             val time: Int)