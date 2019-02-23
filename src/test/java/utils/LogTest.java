package utils;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest
{
    private final static Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void logTest()
    {
        BasicConfigurator.configure();
        logger.debug("Prova log livello DEBUG");
        logger.info("Prova log livello INFO");
        logger.warn("Prova log livello WARNING");
        logger.error("Prova log livello ERROR");
    }

    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        logger.debug("Prova log livello DEBUG");
        logger.info("Prova log livello INFO");
        logger.warn("Prova log livello WARNING");
        logger.error("Prova log livello ERROR");
    }
}
