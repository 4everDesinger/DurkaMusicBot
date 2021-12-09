package me.desinger.durka.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import me.desinger.durka.audio.ServerMusicManager;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;

public class CommandUtil {
    public static void playTrack(String query, TextChannel channel, ServerMusicManager m, AudioPlayerManager manager){
        manager.loadItemOrdered(m, isUrl(query) ? query : "ytsearch: " + query, new FunctionalResultHandler(audioTrack -> {
            System.out.println("n1");
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Новая песня!")
                            .setDescription("Сейчас играет: " + audioTrack.getInfo().title)).send(channel);
            m.scheduler.queue(audioTrack);

        }, audioPlaylist -> {
            if (audioPlaylist.isSearchResult()) {
                m.scheduler.queue(audioPlaylist.getTracks().get(0));
                System.out.println("n2");
                new MessageBuilder()
                        .setEmbed(new EmbedBuilder()
                                .setColor(Color.GREEN)
                                .setTitle("Новая песня!")
                                .setDescription("Новый трек добавлен в очередь: " + audioPlaylist.getTracks().get(0).getInfo().title)).send(channel);
            } else {
                audioPlaylist.getTracks().forEach(audioTrack -> {
                    m.scheduler.queue(audioTrack);
                    new MessageBuilder()
                            .setEmbed(new EmbedBuilder()
                                    .setColor(Color.GREEN)
                                    .setTitle("Новая песня!")
                                    .setDescription("Новый трек добавлен в очередь: " + audioTrack.getInfo().title)).send(channel);
                });
            }
        }, () -> {
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Ошибочка!")
                            .setDescription("Я не могу могу найти эту песню:(")).send(channel);
        }, e -> {
            new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Ошибочка!")
                            .setDescription("Я не могу могу воспроизвести эту песню:(")).send(channel);
        }));
    }

    public static boolean isUrl(String argument){
        return argument.startsWith("https://") || argument.startsWith("http://");
    }
}
