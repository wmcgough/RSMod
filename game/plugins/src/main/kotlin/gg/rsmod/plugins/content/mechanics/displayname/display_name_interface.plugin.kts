package gg.rsmod.plugins.content.mechanics.displayname

import java.nio.file.Files
import java.nio.file.Paths
import gg.rsmod.plugins.service.sql.services.PlayerService
import gg.rsmod.plugins.service.sql.controllers.DisplayNameController
import gg.rsmod.plugins.service.sql.controllers.SaveController


var name = "";

on_button(261, 75) {
    player.openInterface(interfaceId = 589, dest = InterfaceDestination.TAB_AREA)
    player.setComponentText(interfaceId = 589, component = 6, text = "Next free change:")
    player.setComponentText(interfaceId = 589, component = 7, text = DisplayNameController().timeUntilNextNameChange(player as Client))
    player.setInterfaceEvents(interfaceId = 589, component = 18, range = 0..9, setting = 0)
    player.setVarbit(5605, 1)
}

on_button(589, 18) {
    player.queue {
        name = inputString("What name would you like to check (maximum of 12 characters)?")
        player.setComponentText(interfaceId = 589, component = 15, text = name)
        player.setVarbit(5605, 2)
        if (DisplayNameController().displayNameIsAvailable(name)) {
            player.setInterfaceEvents(interfaceId = 589, component = 19, range = 0..9, setting = 0)
            player.setVarbit(5605, DisplayNameController().setPlayerCanChangeNameVarbit(player as Client))
            player.setVarbit(5606, 1)
            player.setVarbit(5607, 0)
            player.setComponentText(interfaceId = 589, component = 15, text = "<col=00ff00>Not taken")
            player.runClientScript(635, name)
        } else {
            player.setComponentText(interfaceId = 589, component = 15, text = "<col=ffffff>Not available")
            player.setVarbit(5605, 1)
        }
    }
}

on_button(589, 19) {
    player.queue {
        player.setVarbit(5605, 1)
        player.setComponentText(interfaceId = 589, component = 15, text = "")
        DisplayNameController().updatePlayerDisplayName(name, player as Client)
        player.queue {
            messageBox("Your display name request  was accepted. Please log out and in for your changes to take effect.")
        }
    }
    player.closeInterface(589)
}

on_button(589, 2) {
    player.closeInterface(589)
}
