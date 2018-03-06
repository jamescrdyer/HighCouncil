package highcouncil.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(highcouncil.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(highcouncil.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Game.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Game.class.getName() + ".players", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Game.class.getName() + ".turnResults", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Game.class.getName() + ".decks", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Game.class.getName() + ".cards", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Player.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Player.class.getName() + ".hands", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Deck.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Deck.class.getName() + ".cards", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Deck.class.getName() + ".discards", jcacheConfiguration);
            cm.createCache(highcouncil.domain.Kingdom.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Card.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.OrderResolution.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.ActionResolution.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.Orders.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.ExpectedOrderNumber.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.TurnResult.class.getName(), jcacheConfiguration);
            cm.createCache(highcouncil.domain.TurnResult.class.getName() + ".playerTurnResults", jcacheConfiguration);
            cm.createCache(highcouncil.domain.PlayerTurnResult.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
