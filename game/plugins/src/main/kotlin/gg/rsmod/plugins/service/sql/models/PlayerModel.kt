package gg.rsmod.plugins.service.sql.models

import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.LocalDateTime

object PlayerModel : Table("Players") {
    val id = integer("id").autoIncrement().primaryKey()
    val username = varchar("username", 60).uniqueIndex()
    val displayName = varchar("display_name", 60).uniqueIndex()
    val x = integer("position_x")
    val height = integer("position_y")
    val z = integer("position_z")
    val privilege = integer("privilege").default(1)
    val runEnergy = float("run_energy").default(100.toFloat())
    val displayMode = integer("display_mode").default(0)
    val hash = varchar("password_hash", 60)
    val xteaKeyOne = integer("xtea_one")
    val xteaKeyTwo = integer("xtea_two")
    val xteaKeyThree = integer("xtea_three")
    val xteaKeyFour = integer("xtea_four")
    val displayNameLastChanged = datetime("display_name_last_changed")
}

data class Player(
        val id: Int,
        val username: String,
        val displayName: String,
        val x: Int,
        val height: Int,
        val z: Int,
        val privilege: Int,
        val runEnergy: Float,
        val displayMode: Int,
        val hash: String,
        val xteaKeyOne: Int,
        val xteaKeytwo: Int,
        val xteaKeyThree: Int,
        val xteaKeyFour: Int,
        val displayNameLastChanged: DateTime
)
