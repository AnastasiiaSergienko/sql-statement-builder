package com.exasol.sql.dml.rendering;

import com.exasol.sql.Field;
import com.exasol.sql.Table;
import com.exasol.sql.dml.*;
import com.exasol.sql.expression.ValueExpression;
import com.exasol.sql.rendering.AbstractFragmentRenderer;
import com.exasol.sql.rendering.StringRendererConfig;

/**
 * The {@link InsertRenderer} turns SQL statement structures in to SQL strings.
 */
// [impl->dsn~rendering.sql.insert~1]
public class InsertRenderer extends AbstractFragmentRenderer implements InsertVisitor {
    /**
     * Create a new {@link InsertRenderer} with custom render settings.
     *
     * @param config render configuration settings
     */
    public InsertRenderer(final StringRendererConfig config) {
        super(config);
    }

    @Override
    public void visit(final Insert insert) {
        appendKeyWord("INSERT INTO ");
        setLastVisited(insert);
    }

    @Override
    public void visit(final Table table) {
        appendAutoQuoted(table.getName());
        setLastVisited(table);
    }

    @Override
    public void visit(final Field field) {
        appendCommaWhenNeeded(field);
        appendAutoQuoted(field.getName());
        setLastVisited(field);
    }

    @Override
    public void visit(final InsertFields insertFields) {
        append(" (");
        setLastVisited(insertFields);
    }

    @Override
    public void leave(final InsertFields insertFields) {
        append(")");
    }

    @Override
    public void visit(final InsertValues insertValues) {
        appendKeyWord(" VALUES (");
        for (final ValueExpression expression : insertValues.getValues()) {
            appendCommaWhenNeeded(insertValues);
            appendRenderedValueExpression(expression);
            setLastVisited(insertValues);
        }
    }

    @Override
    public void leave(final InsertValues insertValues) {
        append(")");
    }

    /**
     * Create an {@link InsertRenderer} using the default renderer configuration
     *
     * @return insert renderer
     */
    public static InsertRenderer create() {
        return create(StringRendererConfig.createDefault());
    }

    /**
     * Create an {@link InsertRenderer}
     *
     * @param config renderer configuration
     * @return insert renderer
     */
    public static InsertRenderer create(final StringRendererConfig config) {
        return new InsertRenderer(config);
    }
}