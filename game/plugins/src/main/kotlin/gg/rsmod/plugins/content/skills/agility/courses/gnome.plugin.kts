package gg.rsmod.plugins.content.skills.agility.courses

import gg.rsmod.game.model.ForcedMovement
import gg.rsmod.game.model.Tile
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Anims
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.player

val courseProgress = AttributeKey<Int>("gnomeCourse")

on_obj_option(23145, "Walk-across") {
    player.queue {
        forcedWalkWithAnimation(anim = Anims.WALKING_ON_BALANCE, wait = 8, tile = Tile(2474, 3429))
    }
    if (player.attr.getOrDefault(courseProgress, 0) == 0) {
       player.attr[courseProgress] = 1
    }
    player.addXp(Skills.AGILITY,7.5)
}


on_obj_option(23134, "Climb-over") {
    player.queue {
        player.animate(Anims.CLIMB_OVER_NET)
        wait(2)
        player.moveTo(Tile(player.tile.x, player.tile.z - 2, 1))
    }
    if (player.attr[courseProgress] == 1) {
        player.attr[courseProgress] = 2
    }
    player.addXp(Skills.AGILITY,7.5)
}

on_obj_option(23559, "Climb") {
    player.queue {
        player.animate(Anims.CLIMB_BRANCH)
        wait(2)
        player.moveTo(Tile(2473, 3420, 2))
    }
    player.addXp(Skills.AGILITY,5.0)
}

on_obj_option(23557, "Walk-on") {
    player.queue {
        forcedWalkWithAnimation(anim = Anims.WALKING_ON_BALANCE, wait = 8, tile = Tile(2483, 3420, 2))
    }
    player.addXp(Skills.AGILITY,7.5)
}

val branches = intArrayOf(
        Objs.TREE_BRANCH_23560,
        Objs.TREE_BRANCH_23561
)

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
    player.queue {
        player.animate(Anims.CLIMB_OVER_NET)
        wait(2)
        player.moveTo(Tile(player.tile.x, player.tile.z + 2, 0))
    }
    if (player.attr[courseProgress] == 2) {
        player.attr[courseProgress] = 3
    }
    player.addXp(Skills.AGILITY,7.5)
}

val pipes = intArrayOf(
        Objs.OBSTACLE_PIPE_23138,
        Objs.OBSTACLE_PIPE_23139
)

suspend fun QueueTask.forcedWalkWithAnimation(anim: Int, tile: Tile, wait: Int) {
    player.lock = LockState.DELAY_ACTIONS
    player.forceAnimations(Anims.WALKING_ON_BALANCE)
    player.walkTo(tile = tile, stepType = MovementQueue.StepType.FORCED_WALK, detectCollision = false);
    wait(wait)
    player.forceAnimations(-1)
    player.lock = LockState.NONE
}

pipes.forEach {
    on_obj_option(it, "Squeeze-through") {
        val pipe = player.getInteractingGameObj()
        if (pipe.tile == Tile(2484, 3435) || pipe.tile == Tile(2487, 3435)) {
            return@on_obj_option
        }
        player.queue {
            player.animate(Anims.PIPE_CRAWL)
            player.forceMove(this, ForcedMovement.of(player.tile, Tile(pipe.tile.x, 3437), 33, 126, Direction.NORTH.angle))
            player.animate(Anims.PIPE_CRAWL)
            player.forceMove(this, ForcedMovement.of(player.tile, Tile(pipe.tile.x, 3437), 33, 126, Direction.NORTH.angle))
        }
        if (player.attr[courseProgress] == 3) {
            player.addXp(Skills.AGILITY,39.0)
            player.attr[courseProgress] = 0
        }
        else {
            player.addXp(Skills.AGILITY,7.5)
        }
    }
}