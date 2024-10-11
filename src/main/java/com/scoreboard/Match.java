package com.scoreboard;

import java.time.LocalDateTime;

public class Match {

    private final String homeTeam;
    private final String awayTeam;
    private final Score score;
    private LocalDateTime started;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = new Score();
    }

    public String getHomeTeam() {
        return this.homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime updated) {
        this.started = updated;
    }

    public Score getScore() {
        return score;
    }
}
