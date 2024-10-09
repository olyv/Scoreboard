package com.scoreboard;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Scoreboard {

    private Clock clock;
    private final List<Match> matchesInProgress = new ArrayList<>();

    public Scoreboard(Clock clock) {
        this.clock = clock;
    }

    private static Predicate<Match> getMatchInProgressPredicate(String homeTeam, String awayTeam) {
        return it -> it.getHomeTeam().equals(homeTeam) && it.getAwayTeam().equals(awayTeam);
    }

    private static Comparator<Match> getMatchComparator() {
        Comparator<Match> comparingByTotalScore = Comparator.comparing((Match it) -> it.getHomeTeamScore() + it.getAwayTeamScore(), Comparator.reverseOrder());
        Comparator<Match> comparingByLatestUpdate = Comparator.comparing(Match::getLatestUpdate, Comparator.reverseOrder());
        return comparingByTotalScore
                .thenComparing(comparingByLatestUpdate);
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void startNewMatch(String homeTeam, String awayTeam) {
        validateStartNewMatchInput(homeTeam, awayTeam);
        Match match = new Match(homeTeam, awayTeam);
        matchesInProgress.add(match);
    }

    private void validateStartNewMatchInput(String homeTeam, String awayTeam) {
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException("It is not allowed to start match for the same home and away teams");
        }
        boolean isMatchInProgress = matchesInProgress.stream()
                .anyMatch(getMatchInProgressPredicate(homeTeam, awayTeam));
        if (isMatchInProgress) {
            throw new IllegalArgumentException("Match is already in progress");
        }
    }

    public List<Match> getSummary() {
        return matchesInProgress.stream()
                .sorted(getMatchComparator())
                .collect(Collectors.toList());
    }

    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        validateUpdateScoreInput(homeTeamScore, awayTeamScore);
        Match matchInProgress = matchesInProgress.stream()
                .filter(getMatchInProgressPredicate(homeTeam, awayTeam))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not able to update score as match not in progress"));
        matchInProgress.setHomeTeamScore(homeTeamScore);
        matchInProgress.setAwayTeamScore(awayTeamScore);
        matchInProgress.setLatestUpdate(LocalDateTime.now(this.clock));
    }

    private void validateUpdateScoreInput(int homeTeamScore, int awayTeamScore) {
        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new IllegalArgumentException("Attempt to set invalid values as new match score");
        }
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        matchesInProgress.removeIf(getMatchInProgressPredicate(homeTeam, awayTeam));
    }
}
