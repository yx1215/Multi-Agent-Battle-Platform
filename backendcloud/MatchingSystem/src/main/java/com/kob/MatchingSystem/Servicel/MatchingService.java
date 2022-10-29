package com.kob.MatchingSystem.Servicel;

public interface MatchingService {

    String addPlayer(Integer userId, Integer rating);

    String removePlayer(Integer userId);
}
