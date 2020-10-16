package gg.rsmod.plugins.service.sql.controllers

import gg.rsmod.game.model.entity.Client
import gg.rsmod.plugins.service.sql.models.PlayerModel
import gg.rsmod.plugins.service.sql.serializers.SQLSerializer
import gg.rsmod.plugins.service.sql.services.PlayerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.Locale
import javax.xml.datatype.DatatypeConstants.DAYS


class DisplayNameController: Controller() {
    suspend fun updatePlayerDisplayName(name: String, client: Client): Boolean {
        val serialize: SQLSerializer = LoadController().loadPlayer(client) ?: return false

        withContext(Dispatchers.IO) {
            transaction {
                PlayerModel.update({
                    PlayerModel.id eq serialize.player[PlayerModel.id]
                }) {
                    client.username = name
                    it[displayNameLastChanged] = DateTime.now()
                }
            }
        }
        log("${client.loginUsername} has just changed their display name.")
        return true
    }

    fun displayNameIsAvailable(name: String): Boolean {
        var nameIsAvailable: Boolean = true
        val players = PlayerService().getAllUsers()

        transaction {
            for (player in players) {
                if (player.displayName.toLowerCase() == name.toLowerCase()) {
                    nameIsAvailable = false
                }
            }
        }
        return nameIsAvailable
    }

    fun timeUntilNextNameChange(client: Client): String {
        val serialize: SQLSerializer = LoadController().loadPlayer(client) ?: return "";

        val playersLastNameChange = serialize.player[PlayerModel.displayNameLastChanged].toLocalDate()
        val thirtyDaysAfterLastNameChange = playersLastNameChange.plusDays(30)

        var daysBetweenLastNameChange = Days.daysBetween(DateTime.now().toLocalDate(), thirtyDaysAfterLastNameChange).days

        println(daysBetweenLastNameChange)

        return if (daysBetweenLastNameChange == 0) {
            "Now!"
        } else {
            "$daysBetweenLastNameChange+ Days!"
        }
    }

    fun setPlayerCanChangeNameVarbit(client: Client): Int {
        var varbit = 1
        var daysUntilNextChange = timeUntilNextNameChange(client)
        if (daysUntilNextChange == "Now!") {
            varbit = 4
        }
        return varbit
    }
}