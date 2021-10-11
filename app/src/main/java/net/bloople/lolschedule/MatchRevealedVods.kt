package net.bloople.lolschedule

class MatchRevealedVods {
    private val matchRevealedVods: HashMap<String, Int> = HashMap();

    operator fun get(match: Match): Int {
        return matchRevealedVods[match.key] ?: (if(match.vods.isNotEmpty()) 1 else 0)
    }

    operator fun set(match: Match, value: Int) {
        matchRevealedVods[match.key] = value
    }
}