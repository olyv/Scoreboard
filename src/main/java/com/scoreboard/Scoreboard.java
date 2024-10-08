package com.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {

    private final List<Match> matchesInProgress = new ArrayList<>();

    public void startNewMatch(String homeTeam, String awayTeam) {
        Match match = new Match(homeTeam, awayTeam);
        matchesInProgress.add(match);
    }

    public List<Match> getSummary() {
        return matchesInProgress;
    }
}
