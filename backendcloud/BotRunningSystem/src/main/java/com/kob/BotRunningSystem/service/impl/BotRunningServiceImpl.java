package com.kob.BotRunningSystem.service.impl;

import com.kob.BotRunningSystem.service.BotRunningService;
import com.kob.BotRunningSystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;


@Service
public class BotRunningServiceImpl implements BotRunningService {

    public static final BotPool botPool = new BotPool();

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        botPool.addBot(userId, botCode, input);
        System.out.println("add bot: " + userId + " " + botCode + " " + input);
        return "add bot success";
    }
}
