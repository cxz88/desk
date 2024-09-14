package com.chenxinzhi.sqlservice

import com.chenxinzhi.repository.PlayerQueries
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import sqlDriver

/**
 * @description
 * @author chenxinzhi
 * @date 2024/9/14
 */


enum class FuncEnum(val musicFunc: String, val key: String) {
    PLAY_MODEL("playModel", "playModel"),
    PLAY_CURRENT_TIME("playCurrentTime", "playCurrentTime"),
}

val GET_BY_KEY_LOCK = Any()

@OptIn(InternalCoroutinesApi::class)
fun getByKey(func: FuncEnum, defaultValue: String) =

    with(PlayerQueries(sqlDriver)) {
        println(
            selectByFuncKey(func.musicFunc, func.key)
                .executeAsOne()
        )
        selectByFuncKey(func.musicFunc, func.key)
            .executeAsOneOrNull()?.musicValue?: synchronized(GET_BY_KEY_LOCK) {
            selectByFuncKey(func.musicFunc, func.key)
                .executeAsOneOrNull()?.musicValue ?: insertByFuncKey(func.musicFunc, func.key, defaultValue)
                .let {
                    defaultValue
                }
        }
    }


fun updateByKey(func: FuncEnum, value: String) =
    with(PlayerQueries(sqlDriver)) {
        updateByFuncKey(value, func.musicFunc, func.key)
    }

