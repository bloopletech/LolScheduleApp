package net.bloople.lolschedule

class MatchesMetadata {
    private val matchesMetadata: HashMap<String, MatchMetadata> = HashMap();

    operator fun get(match: Match): MatchMetadata {
        return matchesMetadata.getOrPut(match.key) { MatchMetadata(match) }
    }

    operator fun set(match: Match, value: MatchMetadata) {
        matchesMetadata[match.key] = value
    }
}

data class MatchMetadata(var vodsRevealed: Int, var showSpoiler: Boolean) {
    constructor(match: Match) : this(if(match.vods.isNotEmpty()) 1 else 0, !match.spoiler)
}