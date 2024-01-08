package org.max.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Демонстрация работы библиотеки логирования
 */
public class LogbackExampleTest {

    //Создание объекта Logger
    private static final Logger logger
            = LoggerFactory.getLogger(LogbackExampleTest.class);

    //генерация лог сообщения
    @Test
    void simpleExampleTest() {
        logger.info("Example log from " + LogbackExampleTest.class.getSimpleName());
        logger.info("log String value to console");
    }

    //генерация лог сообщения с параметрами
    @Test
    void parametrizedMessageTest() {
        String message = "This is a String";
        Integer zero = 0;

        try {
            logger.debug("Logging message: {}", message);
            logger.debug("Going to divide {} by {}", 42, zero);
            int result = 42 / zero;
        } catch (Exception e) {
            logger.error("Error dividing {} by {} ", 42, zero, e);
        }
    }
}
