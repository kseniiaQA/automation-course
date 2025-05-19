package config;

import org.aeonbits.owner.Config;

public interface StatusConfig extends Config {
    @Key("baseUrl")
    String baseUrl();
}