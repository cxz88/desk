package com.chenxinzhi.sqlservice

import com.chenxinzhi.repository.PlayerQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.withContext
import sqlDriver

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/14
 */


enum class FuncEnum(val musicFunc: String, val key: String) {
    PLAY_MODEL("playModel", "playModel"),
    SHOW_DESK_LYC("showDeskLyc", "showDeskLyc"),
    PLAY_CURRENT_TIME("playCurrentTime", "playCurrentTime"),
    PLAY_OR_PAUSE_STATE("playOrPauseState", "playOrPauseState"),
    MUSIC_ID("musicId", "musicId"),
    LycPost("LycPost", "LycPost"),
    mainPost("mainPost", "mainPost"),
}


val GET_BY_KEY_LOCK = Any()

@OptIn(InternalCoroutinesApi::class)
suspend fun getByKey(func: FuncEnum, defaultValue: String) =
    withContext(Dispatchers.IO) {
        with(PlayerQueries(sqlDriver)) {
            selectByFuncKey(func.musicFunc, func.key)
                .executeAsOneOrNull()?.musicValue ?: synchronized(GET_BY_KEY_LOCK) {
                selectByFuncKey(func.musicFunc, func.key)
                    .executeAsOneOrNull()?.musicValue ?: insertByFuncKey(
                    func.musicFunc,
                    func.key,
                    defaultValue
                )
                    .let {
                        defaultValue
                    }
            }
        }
    }


suspend fun updateByKey(func: FuncEnum, value: String) =
    withContext(Dispatchers.IO) {
        with(PlayerQueries(sqlDriver)) {
            updateByFuncKey(value, func.musicFunc, func.key)
        }
    }

