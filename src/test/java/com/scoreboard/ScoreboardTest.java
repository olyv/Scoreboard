package com.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.*;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScoreboardTest {

    private static final String HOME_TEAM_1 = "Mexico";
    private static final String HOME_TEAM_2 = "Spain";
    private static final String HOME_TEAM_3 = "Germany";
    private static final String AWAY_TEAM_1 = "Canada";
    private static final String AWAY_TEAM_2 = "Brazil";
    private static final String AWAY_TEAM_3 = "France";

    private Scoreboard scoreboard;
    private Clock clock;

    @BeforeEach
    public void setUp() {
        clock = Clock.systemDefaultZone();
        scoreboard = new Scoreboard(clock);
    }

    @Test
    public void shouldStartNewMatch() {
        //Given When
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        LocalDateTime expectedUpdateTime = LocalDateTime.now(fixedClock);
        scoreboard.setClock(fixedClock);
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //Then
        List<Match> matchesInProgress = scoreboard.getSummary();
        assertThat(matchesInProgress, hasSize(1));
        Match match = matchesInProgress.get(0);
        assertThat(match.getHomeTeam(), equalTo(HOME_TEAM_1));
        assertThat(match.getAwayTeam(), equalTo(AWAY_TEAM_1));
        assertThat(match.getScore().getHomeTeamScore(), equalTo(0));
        assertThat(match.getScore().getAwayTeamScore(), equalTo(0));
        assertThat(match.getLatestUpdate(), equalTo(expectedUpdateTime));
    }

    @Test
    public void shouldUpdateMatchScore() {
        //Given
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        LocalDateTime expectedUpdateTime = LocalDateTime.now(fixedClock);
        scoreboard.setClock(fixedClock);
        final int homeTeamScore = 1;
        final int awayTeamScore = 3;
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //When
        scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, homeTeamScore, awayTeamScore);

        //Then
        Match matchInProgress = scoreboard.getSummary().get(0);
        assertThat(matchInProgress.getScore().getHomeTeamScore(), equalTo(homeTeamScore));
        assertThat(matchInProgress.getScore().getAwayTeamScore(), equalTo(awayTeamScore));
        assertThat(matchInProgress.getLatestUpdate(), equalTo(expectedUpdateTime));
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
        assertThat(matchesInProgress.get(0).getHomeTeam(), equalTo(HOME_TEAM_2));
        assertThat(matchesInProgress.get(1).getHomeTeam(), equalTo(HOME_TEAM_1));
        assertThat(matchesInProgress.get(2).getHomeTeam(), equalTo(HOME_TEAM_3));
    }

    @Test
    public void shouldGetSummaryOfMatchesInProgressOrderedByMostRecentStart_givenTotalScoresAreEqual() {
        //Given
        Duration toFiveMinutesInThePast = Duration.ofMinutes(-5);
        scoreboard.setClock(Clock.offset(clock, toFiveMinutesInThePast));
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);
        scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, 0, 0);

        Duration noOffset = Duration.ofMinutes(0);
        scoreboard.setClock(Clock.offset(clock, noOffset));
        scoreboard.startNewMatch(HOME_TEAM_2, AWAY_TEAM_2);
        scoreboard.updateScore(HOME_TEAM_2, AWAY_TEAM_2, 2, 2);

        Duration toFiveMinutesInTheFuture = Duration.ofMinutes(5);
        scoreboard.setClock(Clock.offset(clock, toFiveMinutesInTheFuture));
        scoreboard.startNewMatch(HOME_TEAM_3, AWAY_TEAM_3);
        scoreboard.updateScore(HOME_TEAM_3, AWAY_TEAM_3, 2, 2);

        //When
        List<Match> matchesInProgress = scoreboard.getSummary();

        //Then
        assertThat(matchesInProgress, hasSize(3));
        assertThat(matchesInProgress.get(0).getHomeTeam(), equalTo(HOME_TEAM_3));
        assertThat(matchesInProgress.get(1).getHomeTeam(), equalTo(HOME_TEAM_2));
        assertThat(matchesInProgress.get(2).getHomeTeam(), equalTo(HOME_TEAM_1));
    }

    @Test
    public void shouldNotStartMatch_givenMatchInProgress() {
        //Given
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //When Then
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1)
        );
        assertEquals(exception.getMessage(), "Match is already in progress");
    }

    @ParameterizedTest
    @MethodSource("startMatchWithTeamInProgress")
    public void shouldNotStartMatch_givenTeamInMatchInProgress(String team, String opponentTeam, String otherOpponentTeam) {
        //Given
        scoreboard.startNewMatch(team, opponentTeam);

        //When Then
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(team, otherOpponentTeam)
        );
        assertEquals(exception.getMessage(), "Team is already in match in progress");
    }

    public static Stream<Arguments> startMatchWithTeamInProgress() {
        return Stream.of(
                Arguments.of(HOME_TEAM_1, HOME_TEAM_2, HOME_TEAM_2),
                Arguments.of(HOME_TEAM_1, HOME_TEAM_2, HOME_TEAM_1)
        );
    }

    @ParameterizedTest
    @MethodSource("startMatchWithInvalidInput")
    public void shouldNotStartMatch_givenInvalidInput(String homeTeam, String awayTeam) {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.startNewMatch(homeTeam, awayTeam)
        );
        assertEquals(exception.getMessage(), "It is not allowed to start a new match when home and away teams are the same team");
    }

    public static Stream<Arguments> startMatchWithInvalidInput() {
        return Stream.of(
                Arguments.of(HOME_TEAM_1, HOME_TEAM_1),
                Arguments.of(HOME_TEAM_1.toLowerCase(), HOME_TEAM_1),
                Arguments.of(HOME_TEAM_1, HOME_TEAM_1.toUpperCase())
        );
    }

    @Test
    public void shouldNotUpdateScore_givenMatchNotInProgress() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, 0, 0)
        );
        assertEquals(exception.getMessage(), "Not able to update score as match not in progress");
    }

    @ParameterizedTest
    @MethodSource("updateMatchWithInvalidInput")
    public void shouldNotUpdateScore_givenInvalidScoreInput(int homeTeamScore, int awayTeamScore) {
        //Given
        scoreboard.startNewMatch(HOME_TEAM_1, AWAY_TEAM_1);

        //When Then
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.updateScore(HOME_TEAM_1, AWAY_TEAM_1, homeTeamScore, awayTeamScore)
        );
        assertEquals(exception.getMessage(), "Attempt to set invalid values as a new match score");
    }

    public static Stream<Arguments> updateMatchWithInvalidInput() {
        return Stream.of(
                Arguments.of(-1, 1),
                Arguments.of(1, -2)
        );
    }

    @Test
    public void shouldNotFinishMatch_givenMatchNotInProgress() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> scoreboard.finishMatch(HOME_TEAM_1, AWAY_TEAM_1)
        );
        assertEquals(exception.getMessage(), "Not able to finish match as it is not in progress");
    }
}