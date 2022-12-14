package sh.yannick.bot.weather.message;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Sends a message to a discord channel.
 * <p>
 * This bean requires a property <code>discord.token</code> containing the bots token and <code>discord.channel</code>
 * containing the destination channel's ID.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@Slf4j
@Repository
public class MessageSenderDiscord implements MessageSender {
    @Value("${discord.token}")
    private String token;

    @Value("${discord.channel}")
    private String channel;

    @Override
    public void send(String message) {
        log.info("Sending message to Discord channel ...");
        DiscordClient client = DiscordClient.create(token);
        client.getChannelById(Snowflake.of(channel)).createMessage(message).block();
        log.info("Message sent to Discord channel.");
    }
}
