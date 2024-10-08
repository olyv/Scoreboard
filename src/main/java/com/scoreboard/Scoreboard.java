package com.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Scoreboard {

    private final List<Match> matchesInProgress = new ArrayList<>();

    private static Predicate<Match> getMatchInProgressPredicate(String homeTeam, String awayTeam) {
        return it -> it.getHomeTeam().equals(homeTeam) && it.getAwayTeam().equals(awayTeam);
    }

    public void startNewMatch(String homeTeam, String awayTeam) {
        Match match = new Match(homeTeam, awayTeam);
        matchesInProgress.add(match);
    }

    public List<Match> getSummary() {
        return matchesInProgress;
    }

    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        Match matchInProgress = matchesInProgress.stream()
                .filter(getMatchInProgressPredicate(homeTeam, awayTeam))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        matchInProgress.setHomeTeamScore(homeTeamScore);
        matchInProgress.setAwayTeamScore(awayTeamScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        matchesInProgress.removeIf(getMatchInProgressPredicate(homeTeam, awayTeam));
    }
}
