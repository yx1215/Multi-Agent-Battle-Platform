package com.kob.backend.service.impl.user.bot;


import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.AddService;
import com.kob.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        User user = UserUtil.getLoginUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

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

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, 1500, now, now);
        botMapper.insert(bot);
        map.put("error_message", "success");
        return map;
    }
}
