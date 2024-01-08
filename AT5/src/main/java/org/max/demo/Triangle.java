package org.max.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Triangle {

    private static final Logger logger
            = LoggerFactory.getLogger(Triangle.class);

    public double square(int a, int b, int c) throws NotTriangleException {
        logger.info("Вызвана функция вычисления площади треугольника со сторонами {}, {}, {}", a, b, c);
        testTriangle(a, b, c);
        return getSquare(a, b, c);
    }

    public void testTriangle(int a, int b, int c) throws NotTriangleException {
        logger.info("Вызвана функция проверки существования треугольника со сторонами {}, {}, {}", a, b, c);
        if (a + b >= c) {
            if (b + c >= a)
                if (a + c >= b)
                    logger.info("Треугольник сущесвует");
        }
        else {
            logger.error("Треугольник не сущесвует");
            throw new NotTriangleException();
        }
    }

    public double getSquare(int a, int b, int c) {
        double s=(((a+b+c)/2)*((a+b+c)/2-a)*((a+b+c)/2-b)*((a+b+c)/2-c));
        return Math.sqrt(s);
    }

}
