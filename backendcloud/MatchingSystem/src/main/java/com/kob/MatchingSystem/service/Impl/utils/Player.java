package com.kob.MatchingSystem.service.Impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player{

    private Integer userId;
    private Integer rating;
    private Integer waitingTime;
    private Integer botId;
}
