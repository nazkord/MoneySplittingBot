package com.dbteam.service;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;

import java.util.Map;

public interface BalanceService {

    Map<String, Double> getBalanceMap(String username, Long groupChatId) throws PersonNotFoundException, GroupNotFoundException;

}
