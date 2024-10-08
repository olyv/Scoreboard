package com.scoreboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ScoreboardTest {

    @Test
    public void shouldStartNewMatch() {
        //Given
        final String homeTeam = "Mexico";
        final String awayTeam = "Canada";

        Scoreboard scoreboard = new Scoreboard();

        //When
        scoreboard.startNewMatch(homeTeam, awayTeam);

        //Then
        List<Match> matchesInProgress = scoreboard.getSummary();
        assertThat(matchesInProgress, hasSize(1));
        Match match = matchesInProgress.get(0);
        assertThat(match.getHomeTeam(), equalTo(homeTeam));
        assertThat(match.getAwayTeam(), equalTo(awayTeam));
        assertThat(match.getHomeTeamScore(), equalTo(0));
        assertThat(match.getAwayTeamScore(), equalTo(0));
    }

    @Test
    public void shouldUpdateMatchScore() {

    }

    @Test
    public void shouldFinishMatchInProgress() {

    }

    @Test
    public void shouldGetSummaryOfMatchesInProgress() {

    }

}