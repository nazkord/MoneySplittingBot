package com.dbteam.service;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.repository.PersonRepository;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * This class works with states of user.
 * General bot chat state structure looks as follows:
 *
 * {command}/{groupChatId}[/optional[/...]]
 *
 * {command} and {groupChatId} are required because
 * we must know what command is being executed and
 * on context of what group.
 *
 * Next we have optional args.
 *
 * todo - group chat state structure description
 */
public interface StateService {

    boolean isUserBotChatStateStartsWith(String username, String expectedState) throws PersonNotFoundException;

    String buildBotChatState(String command, String... optional);

    String getUserBotChatState(String username) throws PersonNotFoundException;

    String getUserGroupChatState(String username, Long groupChatId) throws PersonNotFoundException;

}
