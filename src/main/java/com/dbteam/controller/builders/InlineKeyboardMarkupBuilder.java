package com.dbteam.controller.builders;

import com.dbteam.model.CallbackData;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardMarkupBuilder implements MarkupBuilder {

    List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

    @Override
    public void reset() {
        buttons = new ArrayList<>();
    }

    @Override
    public MarkupBuilder setButton(int row, int col, String text, CallbackData data) {
        if (buttons.get(row) == null) {
            buttons.add(row, new ArrayList<>());
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button
                .setText(text)
                .setCallbackData(data.getValue());

        buttons.get(row).add(col, button);
        return this;
    }

    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }
}
