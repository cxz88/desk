package com.chenxinzhi.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchList(
    @SerialName("ARTISTPIC")
    val aRTISTPIC: String,
    @SerialName("abslist")
    val abslist: List<Abslist>,
    @SerialName("HIT")
    val hIT: String,
    @SerialName("HIT_BUT_OFFLINE")
    val hITBUTOFFLINE: String,
    @SerialName("HITMODE")
    val hITMODE: String,
    @SerialName("MSHOW")
    val mSHOW: String,
    @SerialName("NEW")
    val nEW: String,
    @SerialName("PN")
    val pN: String,
    @SerialName("RN")
    val rN: String,
    @SerialName("SHOW")
    val sHOW: String,
    @SerialName("searchgroup")
    val searchgroup: String,
    @SerialName("TOTAL")
    val tOTAL: String,
    @SerialName("UK")
    val uK: String
)
@Serializable
data class Paytagindex(
    @SerialName("AR501")
    val aR501: Int,
    @SerialName("DB")
    val dB: Int,
    @SerialName("F")
    val f: Int,
    @SerialName("H")
    val h: Int,
    @SerialName("HR")
    val hR: Int,
    @SerialName("L")
    val l: Int,
    @SerialName("S")
    val s: Int,
    @SerialName("ZP")
    val zP: Int,
    @SerialName("ZPGA201")
    val zPGA201: Int,
    @SerialName("ZPGA501")
    val zPGA501: Int,
    @SerialName("ZPLY")
    val zPLY: Int
)
@Serializable
data class FeeType(
    @SerialName("album")
    val album: String,
    @SerialName("bookvip")
    val bookvip: String,
    @SerialName("song")
    val song: String,
    @SerialName("vip")
    val vip: String
)
@Serializable
data class PayInfo(
    @SerialName("cannotDownload")
    val cannotDownload: String,
    @SerialName("cannotOnlinePlay")
    val cannotOnlinePlay: String,
    @SerialName("download")
    val download: String,
    @SerialName("feeType")
    val feeType: FeeType,
    @SerialName("limitfree")
    val limitfree: String,
    @SerialName("listen_fragment")
    val listenFragment: String,
    @SerialName("local_encrypt")
    val localEncrypt: String,
    @SerialName("ndown")
    val ndown: String,
    @SerialName("nplay")
    val nplay: String,
    @SerialName("overseas_ndown")
    val overseasNdown: String,
    @SerialName("overseas_nplay")
    val overseasNplay: String,
    @SerialName("paytagindex")
    val paytagindex: Paytagindex,
    @SerialName("play")
    val play: String,
    @SerialName("refrain_end")
    val refrainEnd: String,
    @SerialName("refrain_start")
    val refrainStart: String,
    @SerialName("tips_intercept")
    val tipsIntercept: String
)
@Serializable
data class Mvpayinfo(
    @SerialName("download")
    val download: String,
    @SerialName("play")
    val play: String,
    @SerialName("vid")
    val vid: String
)
@Serializable
data class Audiobookpayinfo(
    @SerialName("download")
    val download: String,
    @SerialName("play")
    val play: String
)
@Serializable
data class Abslist(
    @SerialName("AARTIST")
    val aARTIST: String,
    @SerialName("ALBUM")
    val aLBUM: String,
    @SerialName("ALBUMID")
    val aLBUMID: String,
    @SerialName("ALIAS")
    val aLIAS: String,
    @SerialName("ARTIST")
    val aRTIST: String,
    @SerialName("ARTISTID")
    val aRTISTID: String,
    @SerialName("ad_subtype")
    val adSubtype: String,
    @SerialName("ad_type")
    val adType: String,
    @SerialName("allartistid")
    val allartistid: String,
    @SerialName("audiobookpayinfo")
    val audiobookpayinfo: Audiobookpayinfo,
    @SerialName("barrage")
    val barrage: String,
    @SerialName("cache_status")
    val cacheStatus: String,
    @SerialName("CanSetRing")
    val canSetRing: String,
    @SerialName("CanSetRingback")
    val canSetRingback: String,
    @SerialName("content_type")
    val contentType: String,
    @SerialName("DC_TARGETID")
    val dCTARGETID: String,
    @SerialName("DC_TARGETTYPE")
    val dCTARGETTYPE: String,
    @SerialName("DURATION")
    val dURATION: String,
    @SerialName("FARTIST")
    val fARTIST: String,
    @SerialName("FORMAT")
    val fORMAT: String,
    @SerialName("FSONGNAME")
    val fSONGNAME: String,
    @SerialName("fpay")
    val fpay: String,
    @SerialName("info")
    val info: String,
    @SerialName("iot_info")
    val iotInfo: String,
    @SerialName("isdownload")
    val isdownload: String,
    @SerialName("isshowtype")
    val isshowtype: String,
    @SerialName("isstar")
    val isstar: String,
    @SerialName("KMARK")
    val kMARK: String,
    @SerialName("MINFO")
    val mINFO: String,
    @SerialName("MUSICRID")
    val mUSICRID: String,
    @SerialName("MVFLAG")
    val mVFLAG: String,
    @SerialName("MVPIC")
    val mVPIC: String,
    @SerialName("MVQUALITY")
    val mVQUALITY: String,
    @SerialName("mvpayinfo")
    val mvpayinfo: Mvpayinfo,
    @SerialName("NAME")
    val nAME: String,
    @SerialName("NEW")
    val nEW: String,
    @SerialName("N_MINFO")
    val nMINFO: String,
    @SerialName("nationid")
    val nationid: String,
    @SerialName("ONLINE")
    val oNLINE: String,
    @SerialName("opay")
    val opay: String,
    @SerialName("originalsongtype")
    val originalsongtype: String,
    @SerialName("overseas_copyright")
    val overseasCopyright: String,
    @SerialName("overseas_pay")
    val overseasPay: String,
    @SerialName("PAY")
    val pAY: String,
    @SerialName("PROVIDER")
    val pROVIDER: String,
    @SerialName("payInfo")
    val payInfo: PayInfo,
    @SerialName("react_type")
    val reactType: String,
    @SerialName("SONGNAME")
    val sONGNAME: String,
    @SerialName("SUBLIST")
    val sUBLIST: List<String>,
    @SerialName("SUBTITLE")
    val sUBTITLE: String,
    @SerialName("spPrivilege")
    val spPrivilege: String,
    @SerialName("subsStrategy")
    val subsStrategy: String,
    @SerialName("subsText")
    val subsText: String,
    @SerialName("TAG")
    val tAG: String,
    @SerialName("terminal")
    val terminal: String,
    @SerialName("tme_musician_adtype")
    val tmeMusicianAdtype: String,
    @SerialName("tpay")
    val tpay: String,
    @SerialName("web_albumpic_short")
    val webAlbumpicShort: String,
    @SerialName("web_artistpic_short")
    val webArtistpicShort: String,
    @SerialName("web_timingonline")
    val webTimingonline: String
)