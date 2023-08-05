package ru.protei.telegram.recordMessage.telegram.otherCommands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.protei.telegram.recordMessage.commands.otherCommands.NonCommand;


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
        Assertions.assertEquals("89817062001", nonCommand.correctPhone("89817062001"));
        Assertions.assertEquals("89817062002", nonCommand.correctPhone("+79817062002"));
        Assertions.assertEquals("89817062003", nonCommand.correctPhone("79817062003"));
        Assertions.assertEquals("89817062004", nonCommand.correctPhone("9817062004"));

        logger.info("Успех");
    }

}