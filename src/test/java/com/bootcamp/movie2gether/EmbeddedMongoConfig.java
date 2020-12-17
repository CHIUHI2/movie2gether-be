package com.bootcamp.movie2gether;

import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class EmbeddedMongoConfig {
    @Bean
    public IMongodConfig prepareMongodbConfig() throws IOException {
        return new MongodConfigBuilder()
                .version(Version.Main.V4_0)
                .build();
    }
}
