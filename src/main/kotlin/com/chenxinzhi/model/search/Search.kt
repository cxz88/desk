package com.chenxinzhi.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("artist")
    val artist: Artist,
    @SerialName("copyrightId")
    val copyrightId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("mark")
    val mark: Int,
    @SerialName("name")
    val name: String,
    @SerialName("picId")
    val picId: Long,
    @SerialName("publishTime")
    val publishTime: Long,
    @SerialName("size")
    val size: Int,
    @SerialName("status")
    val status: Int
)

@Serializable
data class Artist(
    @SerialName("albumSize")
    val albumSize: Int,
    @SerialName("alia")
    val alia: List<String>? = null,
    @SerialName("alias")
    val alias: List<String>,
    @SerialName("fansGroup")
    val fansGroup: String?,
    @SerialName("id")
    val id: Long,
    @SerialName("img1v1")
    val img1v1: Long,
    @SerialName("img1v1Url")
    val img1v1Url: String,
    @SerialName("name")
    val name: String,
    @SerialName("picId")
    val picId: Long,
    @SerialName("picUrl")
    val picUrl: String?=null,
    @SerialName("trans")
    val trans: String?,
    @SerialName("transNames")
    val transNames: List<String>?=null
)

@Serializable
data class Search(
    @SerialName("albums")
    val albums: List<Album>,
    @SerialName("artists")
    val artists: List<Artist>,
    @SerialName("order")
    val order: List<String>,
    @SerialName("songs")
    val songs: List<Song>
)


@Serializable
data class Song(
    @SerialName("album")
    val album: Album,
    @SerialName("alias")
    val alias: List<String?>,
    @SerialName("artists")
    val artists: List<Artist>,
    @SerialName("copyrightId")
    val copyrightId: Int,
    @SerialName("duration")
    val duration: Int,
    @SerialName("fee")
    val fee: Int,
    @SerialName("ftype")
    val ftype: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("mark")
    val mark: Long,
    @SerialName("mvid")
    val mvid: Int,
    @SerialName("name")
    val name: String,
    @SerialName("rUrl")
    val rUrl: String?,
    @SerialName("rtype")
    val rtype: Int,
    @SerialName("status")
    val status: Int
)








