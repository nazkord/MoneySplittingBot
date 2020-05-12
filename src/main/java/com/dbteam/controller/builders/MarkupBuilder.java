package com.dbteam.controller.builders;

import com.dbteam.model.CallbackData;

public interface MarkupBuilder {

    void reset();

    MarkupBuilder setButton(int row, int col, String text, CallbackData data);

}
