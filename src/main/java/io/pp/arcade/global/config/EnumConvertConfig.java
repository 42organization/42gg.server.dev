package io.pp.arcade.global.config;

import io.pp.arcade.global.util.GameType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Configuration
public class EnumConvertConfig {
    @Component
    public class StringToGameTypeEnumConverter implements Converter<String, GameType> {
        @Override
        public GameType convert(String source) {
            try {
                return GameType.valueOf(source.toUpperCase(Locale.ROOT));
            } catch (Exception e) {
                return null;
            }
        }
    }
}
