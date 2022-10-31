package com.kob.BotRunningSystem.service.impl.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot {

    Integer userId;
    String botCode;
    String input;

}
