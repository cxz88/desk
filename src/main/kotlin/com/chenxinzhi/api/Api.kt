package com.chenxinzhi.api

import com.chenxinzhi.model.base.KuWoResponse
import com.chenxinzhi.model.lyc.Lyc
import com.chenxinzhi.model.search.SearchList
import com.chenxinzhi.model.url.Music
import com.chenxinzhi.utils.KtorHttpClient
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/18
 */
object Api {

    suspend fun search(key: String): KuWoResponse<List<String>> = withContext(Dispatchers.IO) {
        KtorHttpClient.getAndFallBack(KuWoResponse()) {
            url("https://www.kuwo.cn/openapi/v1/www/search/searchKey")
            parameter("key", key)

        }!!
    }

    suspend fun page(key: String, pn: Int = 0, rn: Int = 40): SearchList? = withContext(Dispatchers.IO) {
        KtorHttpClient.getAndFallBack<SearchList?>(null) {
            url("https://www.kuwo.cn/search/searchMusicBykeyWord")
            parameter("all", key)
            parameter("ft", "music")
            parameter("encoding", "utf8")
            parameter("pn", pn)
            parameter("rn", rn)
            parameter("rformat", "json")
            parameter("mobi", 1)
            parameter("vipver", 1)
            parameter("client", "kt")
            parameter("strategy", "2012")
            parameter("issubtitle", 1)
            parameter("show_copyright_off", 1)

        }
    }

    suspend fun getMusicUrl(musicId: String) = withContext(Dispatchers.IO) {
        KtorHttpClient.getAndFallBack<Music>(null) {
            url("https://nmobi.kuwo.cn/mobi.s?f=web&type=convert_url_with_sign&source=kwplayer_ar_5.1.0.0_B_jiakong_vh.apk&br=320kmp3&rid=$musicId&format=mp3")
        }
    }

    suspend fun getlyc(musicId: String) = withContext(Dispatchers.IO) {
        KtorHttpClient.getAndFallBack<Lyc>(null) {
            url("https://www.kuwo.cn/openapi/v1/www/lyric/getlyric?musicId=$musicId")
        }
    }


}