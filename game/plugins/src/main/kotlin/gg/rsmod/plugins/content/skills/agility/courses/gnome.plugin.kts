package gg.rsmod.plugins.content.skills.agility.courses

import gg.rsmod.game.model.Tile
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Anims
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.player
import kotlin.random.Random
import gg.rsmod.game.model.attr.GNOME_COURSE_PROGRESS

suspend fun QueueTask.forcedWalkWithAnimation(anim: Int, tile: Tile, wait: Int) {
    player.lock = LockState.DELAY_ACTIONS
    player.forceAnimations(anim)
    player.walkTo(tile = tile, stepType = MovementQueue.StepType.FORCED_WALK, detectCollision = false);
    wait(wait)
    player.forceAnimations(-1)
    player.lock = LockState.NONE
}

val branches = intArrayOf(
        Objs.TREE_BRANCH_23560,
        Objs.TREE_BRANCH_23561
)

val pipes = intArrayOf(
        Objs.OBSTACLE_PIPE_23138,
        Objs.OBSTACLE_PIPE_23139
)

on_obj_option(23145, "Walk-across") {
    player.queue {
        forcedWalkWithAnimation(anim = Anims.WALKING_ON_LOG_BALANCE, wait = 8, tile = Tile(2474, 3429))
    }
    player.attr.put(GNOME_COURSE_PROGRESS, 1)
    player.addXp(Skills.AGILITY, 7.5)
}


on_obj_option(23134, "Climb-over") {
    try {
        if (player.attr[GNOME_COURSE_PROGRESS] == 1) {
            player.attr.put(GNOME_COURSE_PROGRESS, 2)
        }
        player.queue {
            player.moveTo(Tile(player.tile.x, player.tile.z - 2, 1))
            player.animate(Anims.CLIMB_OVER_NET)
        }
        player.addXp(Skills.AGILITY, 7.5)
    } catch (e: Exception) {
        player.filterableMessage("You must start the course at the log balance!")
    }

}

on_obj_option(23559, "Climb") {
    player.queue {
        player.animate(Anims.CLIMB_BRANCH)
        wait(2)
        player.moveTo(Tile(2473, 3420, 2))
    }
    player.addXp(Skills.AGILITY, 5.0)
}

on_obj_option(23557, "Walk-on") {
    player.queue {
        forcedWalkWithAnimation(anim = Anims.WALKING_ON_LOG_BALANCE, wait = 8, tile = Tile(2483, 3420, 2))
    }
    player.addXp(Skills.AGILITY, 7.5)
}

branches.forEach {
    on_obj_option(it, "Climb-down") {
        player.queue {
            player.animate(Anims.CLIMB_BRANCH)
            wait(2)
            player.moveTo(Tile(player.tile.x, player.tile.z, 0))
        }
        player.addXp(Skills.AGILITY, 5.0)
    }
}

on_obj_option(23135, "Climb-over") {
    try {
        if (player.attr[GNOME_COURSE_PROGRESS] == 2) {
            player.attr.put(GNOME_COURSE_PROGRESS, 3)
        }
        player.queue {
            player.moveTo(Tile(player.tile.x, player.tile.z + 2, 0))
            player.animate(Anims.CLIMB_OVER_NET)
        }
        player.addXp(Skills.AGILITY, 7.5)
    } catch (e: Exception) {
        player.filterableMessage("You must start the course at the log balance!")
    }
}

pipes.forEach {
    on_obj_option(it, "Squeeze-through") {
        try {
            val petChanceNumber = Random.nextInt(0, 35609)
            val pipe = player.getInteractingGameObj()
            if (pipe.tile == Tile(2484, 3435) || pipe.tile == Tile(2487, 3435)) {
                return@on_obj_option
            }
            if (player.attr[GNOME_COURSE_PROGRESS] == 3) {
                player.addXp(Skills.AGILITY, 39.0)
                player.attr.put(GNOME_COURSE_PROGRESS, 0)
            } else {
                player.addXp(Skills.AGILITY, 7.5)
            }
            player.queue {
                forcedWalkWithAnimation(anim = Anims.PIPE_CRAWL, wait = 8, tile = Tile(pipe.tile.x, 3437))
            }
            if (petChanceNumber == 35609) {
                player.inventory.add(20659, 1)
                player.filterableMessage("You feel something weird sneaking into your backpack.")
            }
        } catch (e: Exception) {
            player.filterableMessage("You must start the course at the log balance!")
        }

    }
}