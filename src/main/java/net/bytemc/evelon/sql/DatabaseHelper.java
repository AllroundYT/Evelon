/*
 * Copyright 2019-2023 ByteMC team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bytemc.evelon.sql;

import net.bytemc.evelon.misc.Reflections;
import net.bytemc.evelon.repository.AbstractIdFilter;
import net.bytemc.evelon.repository.Filter;
import net.bytemc.evelon.repository.annotations.Row;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseHelper {

    public static boolean isTableExists(String tableName) {
        return DatabaseConnection.executeQuery(("SHOW TABLES LIKE " + Schema.encloseSchema("%s") + ";").formatted(tableName), ResultSet::next, false);
    }

    public static String getRowName(Field field) {
        return (field.isAnnotationPresent(Row.class) ? field.getAnnotation(Row.class).name() : field.getName()).toLowerCase();
    }

    public static List<DatabaseRowData> getRowData(String table, String... rowId) {
        return DatabaseConnection.executeQuery("SHOW COLUMNS FROM %s;".formatted(table), resultSet -> {
            var list = new ArrayList<DatabaseRowData>();
            while (resultSet.next()) {
                list.add(new DatabaseRowData(resultSet.getString("field"), null, null));
            }
            return list;
        }, new ArrayList<>());
    }

    public static String getDatabaseFilterQuery(List<Filter> filters) {
        // if no filter is present lol -> ignore
        if (filters.isEmpty()) {
            return Reflections.EMPTY_STRING;
        }

        return new StringBuilder(" WHERE ").append(String.join(" AND ",
                filters.stream()
                        .filter(filter -> filter instanceof AbstractIdFilter)
                        .map(it -> ((AbstractIdFilter) it))
                        .map(it -> it.sqlFilter(it.getId())).toList())).toString();
    }

    public static String insertDefault(String... values) {
        return "INSERT INTO %s(%s) VALUES(%s);".formatted(values);
    }

    public static String create(String... value) {
        return "CREATE TABLE IF NOT EXISTS %s(%s);".formatted(value);
    }

    public static String update(String... value) {
        return "UPDATE %s SET %s;".formatted(value);
    }

}
