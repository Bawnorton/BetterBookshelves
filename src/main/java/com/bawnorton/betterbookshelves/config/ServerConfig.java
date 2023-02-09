package com.bawnorton.betterbookshelves.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerConfig {
    private static ServerConfig INSTANCE;

    public static ServerConfig getInstance() {
        if (INSTANCE == null) INSTANCE = new ServerConfig();
        return INSTANCE;
    }

    public static void update(ServerConfig config) {
        INSTANCE = config;
    }

    @Expose
    @SerializedName("enchanting_table_book_requirement")
    public Integer enchantingTableBookRequirement = 3;

    @Expose
    @SerializedName("book_type_comparator_output")
    public Boolean bookTypeComparatorOutput = false;
}
