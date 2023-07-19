package com.bankproject.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        return modelMapper;
    }
}

/*
ModelMapper - библиотека
Основная роль ModelMapper заключается в сопоставлении объектов путем определения того,
как одна объектная модель сопоставляется с другой, называемой объектом преобразования данных (DTO)

МodelMapper по названию полей сам догадывается, что на что нужно маппить.
Весь процесс можно разбить на две части: распознание и связь полей, а также перенос значений.

По умолчанию ModelMapper ищет поля, помеченные как public,и использует JavaBeans Naming Convention,
чтобы определить какие проперти соответствуют друг другу.
Каждый шаг распознавания поля и связи с полем модели назначения можно настроить;
--AccessLevel — имеет следующие значения PUBLIC, PROTECTED, PACKAGE_PRIVATE, PRIVATE;
--NamingConvention — имеет следующие значения JAVABEANS_ACCESSOR, JAVABEANS_MUTATOR, NONE JAVABEANS_ACCESSOR
ищет гетторы а JAVABEANS_MUTATOR ищет сеттеры, так-же можно создать свой NamingConvention dspdfd NamingConvention.builder();
--NameTokenizers — имеет следующие значения CAMEL_CASE, UNDERSCORE эта опция используется для глубокого маппинга,
пример маппинга имени автора выше;
--MatchingStrategies — может быть STANDARD, LOOSE, STRICT по умолчанию стоит STANDARD.
Если описать работу маппера простыми словами, то: он сканирует поля в соответствии с AccessLevel,
парсит их и бьёт на токены, сравнивая эти токены он пытается понять подходит ли поле для маппинга.
Стратегии настраивают степень точности:

--STRICT — все токены должны быть в одном порядке, а также все токены модели источника должны совпадать с токенами модели
получателя;
--STANDARD — порядок токенов может не совпадать, все токены цели должны совпадать и только один токен источника должен
совпадать.;
--LOOSE — порядок токенов может не совпадать, только один токен модели источника и получателя должен совпадать.
 */
