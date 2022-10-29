package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.UpdateService;
import com.kob.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        User user = UserUtil.getLoginUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        int botId = Integer.parseInt(data.getOrDefault("bot_id", "-1"));

        Map<String, String> map = new HashMap<>();

        if (title == null || title.length() == 0){
            map.put("error_message", "title should not be empty");
            return map;
        }

        if (title.length() > 500){
            map.put("error_message", "title max length is 500.");
            return map;
        }

        if (description == null || description.length() == 0){
            description = "This user is lazy and did not describe his code.";
        }

        if (description.length() > 500){
            map.put("error_message", "description max length is 500.");
            return map;
        }

        if (content == null || content.length() == 0){
            map.put("error_message", "code should not be empty");
            return map;
        }

        Bot bot = botMapper.selectById(botId);

        if (bot == null){
            map.put("error_message", "bot does not exist.");
            return map;
        }

        if (!bot.getUserid().equals(user.getId())){
            map.put("error_message", "Unable to update other's bots.");
            return map;
        }

        Date now = new Date();
        Bot newBot = new Bot(bot.getId(), bot.getUserid(), title, description, content, bot.getCreatetime(), now);
        botMapper.updateById(newBot);
        map.put("error_message", "success");
        return map;
    }
}
