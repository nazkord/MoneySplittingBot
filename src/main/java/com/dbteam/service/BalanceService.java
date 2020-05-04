package com.dbteam.service;

import java.util.Map;

public interface BalanceService {

    Map<String, Double> getBalanceMap(String username, Long groupChatId);

}
