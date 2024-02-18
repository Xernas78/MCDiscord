package dev.xernas.mcdiscord.utils;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.UUID;

public class MinecraftUtils {

    public static String getUsernameByUUID(UUID uuid) throws IOException, InterruptedException {
        JsonObject response = HttpUtils.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).getAsJsonObject();
        return response.get("name").toString();
    }

}
