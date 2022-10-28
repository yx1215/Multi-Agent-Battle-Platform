package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.RemoveService;
import com.kob.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();

        User user = UserUtil.getLoginUser();
        int botId = Integer.parseInt(data.getOrDefault("bot_id", "-1"));

        Bot bot = botMapper.selectById(botId);
        if (bot == null){
            map.put("error_message", "bot does not exist.");
            return map;
        }

        if (!bot.getUserid().equals(user.getId())){
            map.put("error_message", "Unable to remove other's bots.");
            return map;
        }

        botMapper.deleteById(botId);
        map.put("error_message", "success");

        return map;
    }
}
