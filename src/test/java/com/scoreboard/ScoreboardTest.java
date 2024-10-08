package com.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ScoreboardTest {

    private static final String HOME_TEAM_1 = "Mexico";
    private static final String HOME_TEAM_2 = "Spain";
    private static final String HOME_TEAM_3 = "Germany";
    private static final String AWAY_TEAM_1 = "Canada";
    private static final String AWAY_TEAM_2 = "Brazil";
    private static final String AWAY_TEAM_3 = "France";

    private Scoreboard scoreboard;

    @BeforeEach
    public void setUp() {
        scoreboard = new Scoreboard();
    }

    @Test
    public void shouldStartNewMatch() {
        //Given When
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //Then
        List<Match> matchesInProgress = scoreboard.getSummary();
        assertThat(matchesInProgress, hasSize(1));
        Match match = matchesInProgress.get(0);
        assertThat(match.getHomeTeam(), equalTo(HOME_TEAM_1));
        assertThat(match.getAwayTeam(), equalTo(AWAY_TEAM_1));
        assertThat(match.getHomeTeamScore(), equalTo(0));
        assertThat(match.getAwayTeamScore(), equalTo(0));
    }

    @Test
    public void shouldUpdateMatchScore() {
        //Given
        final int homeTeamScore = 1;
        final int awayTeamScore = 3;
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //When
        scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, homeTeamScore, awayTeamScore);

        //Then
        Match matchInProgress = scoreboard.getSummary().get(0);
        assertThat(matchInProgress.getHomeTeamScore(), equalTo(homeTeamScore));
        assertThat(matchInProgress.getAwayTeamScore(), equalTo(awayTeamScore));

    }

    @Test
    public void shouldFinishMatchInProgress() {
        //Given
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //When
        scoreboard.finishMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //Then
        List<Match> matchesInProgress = scoreboard.getSummary();
        assertThat(matchesInProgress, empty());

    }

    @Test
    public void shouldGetSummaryOfMatchesInProgressOrderedByTheirTotalScore() {
        //Given
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);
        scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, 6, 0);
        scoreboard.startNewMatch(HOME_TEAM_2, AWAY_TEAM_2);
        scoreboard.updateScore(HOME_TEAM_2, AWAY_TEAM_2, 6, 2);
        scoreboard.startNewMatch(HOME_TEAM_3, AWAY_TEAM_3);
        scoreboard.updateScore(HOME_TEAM_3, AWAY_TEAM_3, 0, 3);

        //When
        List<Match> matchesInProgress = scoreboard.getSummary();

        //Then
        assertThat(matchesInProgress, hasSize(3));
        assertThat(matchesInProgress.get(0), is(samePropertyValuesAs(new Match(HOME_TEAM_2, AWAY_TEAM_2, 6, 2))));
        assertThat(matchesInProgress.get(1), is(samePropertyValuesAs(new Match(HOME_TEAM_1, AWAY_TEAM_1, 6, 0))));
        assertThat(matchesInProgress.get(2), is(samePropertyValuesAs(new Match(HOME_TEAM_3, AWAY_TEAM_3, 0, 3))));
    }

    @Test
    public void shouldGetSummaryOfMatchesInProgressOrderedByMostRecentStart_givenTotalScoresAreEqual() {
        //Given
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);
        scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, 2, 1);
        scoreboard.startNewMatch(HOME_TEAM_2, AWAY_TEAM_2);
        scoreboard.updateScore(HOME_TEAM_2, AWAY_TEAM_2, 1, 2);
        scoreboard.startNewMatch(HOME_TEAM_3, AWAY_TEAM_3);
        scoreboard.updateScore(HOME_TEAM_3, AWAY_TEAM_3, 0, 3);

        //When
        List<Match> matchesInProgress = scoreboard.getSummary();

        //Then
        assertThat(matchesInProgress, hasSize(3));
        assertThat(matchesInProgress.get(0), is(samePropertyValuesAs(new Match(HOME_TEAM_3, AWAY_TEAM_3, 0, 3))));
        assertThat(matchesInProgress.get(1), is(samePropertyValuesAs(new Match(HOME_TEAM_2, AWAY_TEAM_2, 1, 2))));
        assertThat(matchesInProgress.get(2), is(samePropertyValuesAs(new Match(HOME_TEAM_1, AWAY_TEAM_1, 2, 1))));
    }
}