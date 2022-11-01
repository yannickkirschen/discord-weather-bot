package sh.yannick.bot.weather.message;

/**
 * Sends a message to a Discord channel.
 *
 * @author Yannick Kirschen
 * @since 1.0.0
 */
@FunctionalInterface
public interface MessageSender {
    /**
     * Sends a message to a Discord channel.
     *
     * @param message message to send
     */
    void send(String message);
}
