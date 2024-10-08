package com.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ScoreboardTest {

    private static final String HOME_TEAM = "Mexico";
    private static final String AWAY_TEAM = "Canada";

    private Scoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    public void shouldStartNewMatch() {
        //Given
        //When
        scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //Then
        List<Match> matchesInProgress = scoreboard.getSummary();
        assertThat(matchesInProgress, hasSize(1));
        Match match = matchesInProgress.get(0);
        assertThat(match.getHomeTeam(), equalTo(HOME_TEAM));
        assertThat(match.getAwayTeam(), equalTo(AWAY_TEAM));
        assertThat(match.getHomeTeamScore(), equalTo(0));
        assertThat(match.getAwayTeamScore(), equalTo(0));
    }

    @Test
    public void shouldUpdateMatchScore() {
        //Given
        final int homeTeamScore = 1;
        final int awayTeamScore = 3;
        scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //When
        scoreboard.updateScore(HOME_TEAM, AWAY_TEAM, homeTeamScore, awayTeamScore);

        //Then
        Match matchInProgress = scoreboard.getSummary().get(0);
        assertThat(matchInProgress.getHomeTeamScore(), equalTo(homeTeamScore));
        assertThat(matchInProgress.getAwayTeamScore(), equalTo(awayTeamScore));

    }

    @Test
    public void shouldFinishMatchInProgress() {
        //Given
        scoreboard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //When
        scoreboard.finishMatch(HOME_TEAM, AWAY_TEAM);

        //Then
        List<Match> matchesInProgress = scoreboard.getSummary();
        assertThat(matchesInProgress, empty());

    }

    @Test
    public void shouldGetSummaryOfMatchesInProgress() {

    }
}