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

package net.bytemc.evelon.storages;

import net.bytemc.evelon.repository.RepositoryQuery;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Storage {

    /**
     * @param query a query to the specific repository
     * @param value an object from the specific repository
     * @param <T> repository type
     */
    <T> void create(RepositoryQuery<T> query, T value);

    /**
     * @param query
     * @param <T>
     */
    <T> void delete(RepositoryQuery<T> query);

    /**
     * @param query
     * @param <T>
     * @return
     */
    <T> List<T> findAll(RepositoryQuery<T> query);

    /**
     * @param query a query to the specific repository
     * @param <T>
     * @return
     */
    <T> @Nullable T findFirst(RepositoryQuery<T> query);

    /**
     * @param query
     * @param value
     * @param <T>
     */
    <T> void update(RepositoryQuery<T> query, T value);

    <T> boolean exists(RepositoryQuery<T> query);

    <T> int count(RepositoryQuery<T> query);

    default <T> void createIfNotExists(RepositoryQuery<T> query, T value) {
        if(!exists(query)) {
            create(query, value);
        }
    }
}
