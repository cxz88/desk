package com.chenxinzhi.model.url


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Music(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val `data`: Data,
    @SerialName("locationid")
    val locationid: String,
    @SerialName("msg")
    val msg: String
)

@Serializable
data class Data(
    @SerialName("bitrate")
    val bitrate: Int,
    @SerialName("format")
    val format: String,
    @SerialName("p2p_audiosourceid")
    val p2pAudiosourceid: String,
    @SerialName("rid")
    val rid: Int,
    @SerialName("sig")
    val sig: String,
    @SerialName("source")
    val source: String,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String,
    @SerialName("user")
    val user: String
)