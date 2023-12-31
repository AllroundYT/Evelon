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

import lombok.Getter;
import net.bytemc.evelon.sql.stages.*;
import net.bytemc.evelon.sql.substages.CollectionObjectStage;
import net.bytemc.evelon.sql.substages.MapObjectStage;
import net.bytemc.evelon.sql.substages.VirtualObjectStage;
import net.bytemc.evelon.sql.transformers.RecordTransformer;

import java.util.ArrayList;
import java.util.List;

public final class StageHandler {

    @Getter
    private final static StageHandler instance;
    private final List<Stage<?>> stages = new ArrayList<>();

    static {
        instance = new StageHandler();
    }

    public StageHandler() {
        // for primitives, default java arguments
        this.stages.add(new DefaultParameterStage());

        // only for enumeration types -> mariadb extra type
        this.stages.add(new EnumerationStage());

        this.stages.add(new PathStage());
        this.stages.add(new UuidStage());
        this.stages.add(new DateStage());

        // this for: hashmaps, treemap, concurentHashMap...
        this.stages.add(new MapObjectStage());

        // this is for: lists, collections, sets...
        this.stages.add(new CollectionObjectStage());

        // transform record to object -> deny duplicated code
        this.stages.add(new RecordTransformer());

        // for custom user objects
        this.stages.add(new VirtualObjectStage());
    }


    /**
     * @param type clazz of the object
     * @return the current stage of the class, if not found return null
     */
    public Stage<?> getElementStage(Class<?> type) {
        return this.stages.stream().filter(it -> it.isElement(type)).findFirst().orElse(null);
    }

    /**
     * @param clazz clazz of the stage
     * @param <T>  type of the stage
     * @return the stage of the class, if not found return null
     */
    public <T> Stage<T> getStage(Class<T> clazz) {
        return (Stage<T>) this.stages.stream().filter(it -> it.getClass().equals(clazz)).findFirst().orElse(null);
    }

}
