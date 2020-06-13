package com.microsoft.sqlserver.jdbc.datatypes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.microsoft.sqlserver.jdbc.RandomUtil;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.TestUtils;
import com.microsoft.sqlserver.testframework.AbstractSQLGenerator;
import com.microsoft.sqlserver.testframework.AbstractTest;
import com.microsoft.sqlserver.testframework.PrepUtil;

@RunWith(JUnitPlatform.class)
public class CustomCharsetTest extends AbstractTest{

    @Test
    public void testDisableStringEncoding() throws Exception {
        try (SQLServerConnection conn = (SQLServerConnection) PrepUtil
                    .getConnection(connectionString+";charset==ISO-8859-1");Statement st = conn.createStatement()) {
                String table = AbstractSQLGenerator.escapeIdentifier(RandomUtil.getIdentifier("stringEncoding"));
                TestUtils.dropTableIfExists(table, st);
                st.execute("CREATE TABLE " + table + " (c1 char(1) COLLATE SQL_Latin1_General_CP850_BIN2)");
                st.execute("INSERT INTO " + table + "(c1) VALUES(CAST(200 as binary(1)))");

                try (ResultSet rs = st.executeQuery("SELECT c1 FROM " + table)) {
                    rs.next();
                    String string= rs.getString(1);
                    assertEquals((char)200,string.charAt(0));
                } finally {
                    TestUtils.dropTableIfExists(table, st);
                }
            }
        }

}
