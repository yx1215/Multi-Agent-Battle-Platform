package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.GetListService;
import com.kob.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GetListServiceImpl implements GetListService {

    @Autowired
    BotMapper botMapper;

    @Override
    public List<Bot> getList() {
        User user = UserUtil.getLoginUser();
        int userId = user.getId();
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        return botMapper.selectList(queryWrapper);
    }
}
