package ru.telegram.recordMessage.telegram.nonCommand;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.telegram.recordMessage.telegram.Bot;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class NonCommandTest {

    Logger logger = LoggerFactory.getLogger(NonCommandTest.class);

    NonCommand nonCommand = new NonCommand();
    /**
     * Проверка что строка это телефон
     */
    @Test
    void validatePhoneTest() {
        logger.info("Начинаем проверять телефон");
        Assertions.assertTrue(nonCommand.validatePhone("89817062001"));
        Assertions.assertTrue(nonCommand.validatePhone("+79817062001"));
        Assertions.assertTrue(nonCommand.validatePhone("79817062001"));
        Assertions.assertTrue(nonCommand.validatePhone("9817062001"));
        Assertions.assertFalse(nonCommand.validatePhone("879817062001"));
        Assertions.assertFalse(nonCommand.validatePhone("8a817062001"));
        logger.info("Успех");
    }

    @Test
    void savePhoneTest() {
        logger.info("Начинаем исправлять телефон");
        Bot.setStorage(new TreeMap<>());
        nonCommand.savePhone(1L,"89817062001");
        nonCommand.savePhone(2L,"+79817062002");
        nonCommand.savePhone(3L,"79817062003");
        nonCommand.savePhone(4L,"9817062004");
        Assertions.assertEquals("89817062001", Bot.getStorage().get(1L).remove().getMsisdn());
        Assertions.assertEquals("89817062002", Bot.getStorage().get(2L).remove().getMsisdn());
        Assertions.assertEquals("89817062003", Bot.getStorage().get(3L).remove().getMsisdn());
        Assertions.assertEquals("89817062004", Bot.getStorage().get(4L).remove().getMsisdn());
        logger.info("Успех");
    }

}