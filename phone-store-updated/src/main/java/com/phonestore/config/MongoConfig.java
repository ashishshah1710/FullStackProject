package com.phonestore.config;

import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class MongoConfig {

  @Bean
  public MongoCustomConversions mongoCustomConversions() {
    return new MongoCustomConversions(Arrays.asList(
        new OffsetDateTimeWriteConverter(),
        new OffsetDateTimeReadConverter(),
        new UUIDToStringConverter()
    ));
  }

  public class OffsetDateTimeReadConverter implements Converter<Date, OffsetDateTime> {
    @Override
    public OffsetDateTime convert(Date date) {
      return date.toInstant().atOffset(ZoneOffset.UTC);
    }
  }

  public class OffsetDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {
    @Override
    public Date convert(OffsetDateTime offsetDateTime) {
      return Date.from(offsetDateTime.toInstant());
    }
  }

  static class UUIDToStringConverter implements Converter<UUID, String> {
    @Override
    public String convert(UUID source) {
      return source.toString();
    }
  }

}