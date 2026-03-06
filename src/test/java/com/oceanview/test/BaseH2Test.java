package com.oceanview.test;

import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseH2Test {

    @BeforeAll
    void bootH2() {
        System.setProperty("db.url", "jdbc:h2:mem:oceanview;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        System.setProperty("db.user", "sa");
        System.setProperty("db.pass", "");
        System.setProperty("db.driver", "org.h2.Driver");

        TestDb.init();
    }
}