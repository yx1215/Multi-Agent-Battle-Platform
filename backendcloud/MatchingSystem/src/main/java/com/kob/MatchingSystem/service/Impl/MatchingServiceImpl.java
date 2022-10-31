package com.kob.MatchingSystem.service.Impl;


import com.kob.MatchingSystem.service.Impl.utils.MatchingPool;
import com.kob.MatchingSystem.service.MatchingService;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public static final MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add user: " + userId + " " + rating);
        matchingPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        matchingPool.removePlayer(userId);
        System.out.println("remove user: " + userId);
        return "remove player success";
    }
}
