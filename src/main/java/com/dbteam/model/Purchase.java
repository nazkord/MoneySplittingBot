package com.dbteam.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Purchase {
    String buyerUsername;
    Date date;
    String title;
    String description;
    List<User> recipients;
}
