package de.tsearch.highlightcollector;

import de.tsearch.tclient.Config;
import de.tsearch.tclient.GameClient;
import de.tsearch.tclient.HighlightClient;
import de.tsearch.tclient.TClientInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TClientInstance tClientInstance(@Value("${twitch.clientid}") String clientId,
                                           @Value("${twitch.clientsecret}") String clientSecret) {
        return new TClientInstance(Config.ConfigBuilder.newInstance()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build());
    }

    @Bean
    public GameClient gameClient(TClientInstance clientInstance) {
        return new GameClient(clientInstance);
    }

    @Bean
    public HighlightClient highlightClient(TClientInstance clientInstance) {
        return new HighlightClient(clientInstance);
    }
}
