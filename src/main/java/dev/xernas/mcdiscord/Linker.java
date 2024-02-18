package dev.xernas.mcdiscord;

import dev.xernas.mcdiscord.utils.Queue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Linker {

    private static final Map<UUID, Long> linkMap = new HashMap<>();
    private static final Queue<String, UUID> codesQueue = new Queue<>(5);

    public static UUID link(Long userId, String code) {
        UUID uuid = codesQueue.get(code);
        if (uuid == null) {
            return null;
        }
        linkMap.put(uuid, userId);
        return uuid;
    }

    public static boolean isLinked(UUID uuid) {
        return getLinkedUser(uuid) != null;
    }

    public static Long getLinkedUser(UUID uuid) {
        return linkMap.get(uuid);
    }

    public static void addUserToQueue(UUID uuid, String code) {
        codesQueue.add(code, uuid);
    }

}
