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

package net.bytemc.evelon.repository.filters;

import net.bytemc.evelon.local.LocalStorageHelper;
import net.bytemc.evelon.repository.AbstractIdFilter;
import net.bytemc.evelon.sql.Schema;

import java.util.stream.Stream;

public final class MatchFilter extends AbstractIdFilter {

    private final Object value;

    public MatchFilter(String id, Object value) {
        super(id);
        this.value = value;
    }

    @Override
    public String sqlFilter(String id) {
        return ("%s = " + Schema.encloseSchema("%s")).formatted(getId(), value.toString());
    }

    @Override
    public <T> Stream<T> localFilter(Stream<T> stream) {
        return stream.filter(it -> LocalStorageHelper.getObjectFilter(getId(), it).equals(value));
    }
}
