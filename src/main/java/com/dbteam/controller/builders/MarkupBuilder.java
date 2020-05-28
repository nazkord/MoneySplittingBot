package com.dbteam.controller.builders;

public interface MarkupBuilder {

    void reset();

    MarkupBuilder setButton(int row, int col, String text, String callbackData);

}
