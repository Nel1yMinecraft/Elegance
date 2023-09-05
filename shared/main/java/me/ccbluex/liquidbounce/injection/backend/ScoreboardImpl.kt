/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package me.ccbluex.liquidbounce.injection.backend

import me.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore
import me.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective
import me.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard
import me.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam
import me.ccbluex.liquidbounce.api.util.WrappedCollection
import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.Scoreboard

class ScoreboardImpl(val wrapped: Scoreboard) : IScoreboard {
    override fun getPlayersTeam(name: String?): ITeam? = wrapped.getPlayersTeam(name)?.wrap()

    override fun getObjectiveInDisplaySlot(index: Int): IScoreObjective? = wrapped.getObjectiveInDisplaySlot(index)?.wrap()

    override fun getSortedScores(objective: IScoreObjective): Collection<IScore> = WrappedCollection(wrapped.getSortedScores(objective.unwrap()), IScore::unwrap, Score::wrap)

    override fun equals(other: Any?): Boolean {
        return other is ScoreboardImpl && other.wrapped == this.wrapped
    }
}

inline fun IScoreboard.unwrap(): Scoreboard = (this as ScoreboardImpl).wrapped
inline fun Scoreboard.wrap(): IScoreboard = ScoreboardImpl(this)