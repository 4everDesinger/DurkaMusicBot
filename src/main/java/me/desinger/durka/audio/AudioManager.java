package me.desinger.durka.audio;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static final Map<Long, ServerMusicManager> managers = new HashMap<>();

    public static ServerMusicManager get(long server) {
        if (!managers.containsKey(server)) {
            managers.put(server, new ServerMusicManager(PlayerManager.getManager()));
        }

        return managers.get(server);
    }

}
