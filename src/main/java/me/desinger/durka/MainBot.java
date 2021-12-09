package me.desinger.durka;

import me.desinger.durka.audio.AudioManager;
import me.desinger.durka.audio.PlayerManager;
import me.desinger.durka.audio.ServerMusicManager;
import me.desinger.durka.utils.Commands;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

public class MainBot {
    public static void main(String[] args) {
        FallbackLoggerConfiguration.setTrace(false);
        PlayerManager.init();
        new DiscordApiBuilder()
                .setToken(args[0])
                .setRecommendedTotalShards()
                .join()
                .loginAllShards()
                .forEach(shardFuture -> shardFuture
                        .thenAccept(MainBot::onShardLogin)
                        .exceptionally(ExceptionLogger.get())
                );
    }

    private static void onShardLogin(DiscordApi api){
        api.setAutomaticMessageCacheCleanupEnabled(true);
        api.setMessageCacheSize(10, 60 * 5);
        api.setReconnectDelay(attempt -> attempt * 2);

        api.addMessageCreateListener(event -> {
                    String[] args = event.getMessageContent().split("\\s+");
                    if (args[0].equals("?play")) {
                        ServerMusicManager m = AudioManager.get(event.getServer().get().getId());
                        String query = event.getMessageContent().replace(args[0] + " ", "");
                        Commands.playCommand(query, m, event, event.getServer().get());
                    } else if(args[0].equals("?stop")) {
                        Commands.stopCommand(event, event.getServer().get());
                    } else if(args[0].equals("?skip")) {
                        Commands.skipCommand(event, event.getServer().get());
                    } else if(args[0].equals("?queue")) {
                        Commands.queueCommand(event, event.getServer().get());
                    }
                });
    }

}
