package com.fgfbrands.myapplication.data.local.converters

import androidx.room.TypeConverter

/**
 * Handles the conversion of a list of user IDs to a string and vice versa.
 *
 * Purpose:
 * - Room does not support lists directly, so this converter serializes/deserializes the data.
 *
 * Key Highlights:
 * - Optimizes data storage by converting complex types.
 */
class UserIdListConverter {

    @TypeConverter
    fun fromUserIdList(userIds: List<Int>): String {
        return userIds.joinToString(separator = ",")
    }

    @TypeConverter
    fun toUserIdList(data: String): List<Int> {
        return if (data.isEmpty()) emptyList() else data.split(",").map { it.toInt() }
    }
}