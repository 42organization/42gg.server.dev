package io.pp.arcade.global.config;

import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.StatusType;
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
    @Component
    public class StringToStatusTypeEnumConverter implements Converter<String, StatusType> {
        @Override
        public StatusType convert(String source) {
            try {
                return StatusType.valueOf(source.toUpperCase());
            } catch (Exception e) {
                return null;
            }
        }
    }
}
