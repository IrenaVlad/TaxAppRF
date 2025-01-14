package com.taxapprf.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


const val USER_TABLE = "user"

@Entity(tableName = USER_TABLE)
data class UserEntity(
    @ColumnInfo(name = KEY)
    @PrimaryKey
    val key: String,

    @ColumnInfo(name = IS_SIGN_IN)
    val isSignIn: Boolean,

    @ColumnInfo(name = NAME)
    val name: String,

    @ColumnInfo(name = EMAIL)
    val email: String,

    @ColumnInfo(name = PHONE)
    val phone: String,
) {
    companion object {
        const val KEY = "key"

        const val IS_SIGN_IN = "is_sign_in"

        const val NAME = "name"
        const val EMAIL = "email"
        const val PHONE = "phone"
    }
}