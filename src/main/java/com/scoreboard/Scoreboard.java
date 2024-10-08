package com.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Scoreboard {

    private final List<Match> matchesInProgress = new ArrayList<>();

    public void startNewMatch(String homeTeam, String awayTeam) {
        Match match = new Match(homeTeam, awayTeam);
        matchesInProgress.add(match);
    }

    public List<Match> getSummary() {
        return matchesInProgress;
    }

    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        Match matchInProgress = matchesInProgress.stream()
                .filter(it -> it.getHomeTeam().equals(homeTeam) && it.getAwayTeam().equals(awayTeam))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        matchInProgress.setHomeTeamScore(homeTeamScore);
        matchInProgress.setAwayTeamScore(awayTeamScore);
    }
}
