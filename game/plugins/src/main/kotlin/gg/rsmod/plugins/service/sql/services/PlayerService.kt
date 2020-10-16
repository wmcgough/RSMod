package gg.rsmod.plugins.service.sql.services

import gg.rsmod.plugins.service.sql.models.Player
import gg.rsmod.plugins.service.sql.models.PlayerModel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerService {
    fun getAllUsers(): List<Player> = transaction {
        PlayerModel.selectAll().map { toPlayer(it) }
    }

    private fun toPlayer(row: ResultRow): Player =
            Player(
                    id = row[PlayerModel.id],
                    username = row[PlayerModel.username],
                    displayName = row[PlayerModel.displayName],
                    x = row[PlayerModel.x],
                    height = row[PlayerModel.height],
                    z = row[PlayerModel.z],
                    privilege = row[PlayerModel.privilege],
                    runEnergy = row[PlayerModel.runEnergy],
                    displayMode = row[PlayerModel.displayMode],
                    hash = row[PlayerModel.hash],
                    xteaKeyOne = row[PlayerModel.xteaKeyOne],
                    xteaKeytwo = row[PlayerModel.xteaKeyTwo],
                    xteaKeyThree = row[PlayerModel.xteaKeyThree],
                    xteaKeyFour = row[PlayerModel.xteaKeyFour],
                    displayNameLastChanged = row[PlayerModel.displayNameLastChanged]
            )
}