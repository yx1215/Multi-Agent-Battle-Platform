package com.kob.MatchingSystem.Servicel.Impl;


import com.kob.MatchingSystem.Servicel.Impl.utils.MatchingPool;
import com.kob.MatchingSystem.Servicel.MatchingService;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public static final MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add user: " + userId + " " + rating);
        matchingPool.addPlayer(userId, rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove user: " + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
