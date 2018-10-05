package com.exasol.dql.rendering;

import static com.exasol.hamcrest.SqlFragmentRenderResultMatcher.rendersTo;
import static com.exasol.hamcrest.SqlFragmentRenderResultMatcher.rendersWithConfigTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.sql.StatementFactory;
import com.exasol.sql.dql.Select;
import com.exasol.sql.rendering.StringRendererConfig;

class TestSelectRendering {
    private Select select;

    @BeforeEach
    void beforeEach() {
        this.select = StatementFactory.getInstance().select();
    }

    @Test
    void testEmptySelect() {
        assertThat(this.select, rendersTo("SELECT"));
    }

    @Test
    void testEmptySelectLowerCase() {
        final StringRendererConfig config = new StringRendererConfig.Builder().lowerCase(true).build();
        assertThat(this.select, rendersWithConfigTo(config, "select"));
    }

    @Test
    void testSelectAll() {
        this.select.all();
        assertThat(this.select, rendersTo("SELECT *"));
    }

    @Test
    void testSelectFieldNames() {
        this.select.field("a", "b");
        assertThat(this.select, rendersTo("SELECT a, b"));
    }

    @Test
    void testSelectChainOfFieldNames() {
        this.select.field("a", "b").field("c");
        assertThat(this.select, rendersTo("SELECT a, b, c"));
    }

    @Test
    void testSelectFromTable() {
        this.select.all().from().table("persons");
        assertThat(this.select, rendersTo("SELECT * FROM persons"));
    }

    @Test
    void testSelectFromMultipleTable() {
        this.select.all().from().table("table1").table("table2");
        assertThat(this.select, rendersTo("SELECT * FROM table1, table2"));
    }

    @Test
    void testSelectFromTableAs() {
        this.select.all().from().tableAs("table", "t");
        assertThat(this.select, rendersTo("SELECT * FROM table AS t"));
    }

    @Test
    void testSelectFromMultipleTableAs() {
        this.select.all().from().tableAs("table1", "t1").tableAs("table2", "t2");
        assertThat(this.select, rendersTo("SELECT * FROM table1 AS t1, table2 AS t2"));
    }
}