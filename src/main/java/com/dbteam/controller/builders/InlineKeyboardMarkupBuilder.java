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
    public MarkupBuilder setButton(int row, int col, String text, String callbackData) {
        if (buttons.get(row) == null) {
            buttons.add(row, new ArrayList<>());
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button
                .setText(text)
                .setCallbackData(callbackData);

        buttons.get(row).add(col, button);
        return this;
    }

    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(buttons);
        return keyboardMarkup;
    }

    public void setButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        if (buttons.isEmpty()) {
            buttons.add(new ArrayList<>());
            buttons.get(0).add(button);
        } else {
            boolean added = false;
            for (List<InlineKeyboardButton> row: buttons) {
                if (row.size() < 3) {
                    row.add(button);
                    added = true;
                    break;
                }
            }
            if (!added) {
                List<InlineKeyboardButton> newRow = new ArrayList<>();
                newRow.add(button);
                buttons.add(newRow);
            }
        }

    }
}
