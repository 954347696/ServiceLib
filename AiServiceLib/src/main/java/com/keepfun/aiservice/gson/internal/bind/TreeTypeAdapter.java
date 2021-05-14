/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keepfun.aiservice.gson.internal.bind;


import com.keepfun.aiservice.gson.EGson;
import com.keepfun.aiservice.gson.JsonDeserializationContext;
import com.keepfun.aiservice.gson.JsonDeserializer;
import com.keepfun.aiservice.gson.JsonElement;
import com.keepfun.aiservice.gson.JsonParseException;
import com.keepfun.aiservice.gson.JsonSerializationContext;
import com.keepfun.aiservice.gson.JsonSerializer;
import com.keepfun.aiservice.gson.TypeAdapter;
import com.keepfun.aiservice.gson.TypeAdapterFactory;
import com.keepfun.aiservice.gson.internal.$Gson$Preconditions;
import com.keepfun.aiservice.gson.internal.Streams;
import com.keepfun.aiservice.gson.reflect.TypeToken;
import com.keepfun.aiservice.gson.stream.JsonReader;
import com.keepfun.aiservice.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Adapts a EGson 1.x tree-style adapter as a streaming TypeAdapter. Since the
 * tree adapter may be serialization-only or deserialization-only, this class
 * has a facility to lookup a delegate type adapter on demand.
 */
public final class TreeTypeAdapter<T> extends TypeAdapter<T> {
  private final JsonSerializer<T> serializer;
  private final JsonDeserializer<T> deserializer;
  final EGson EGson;
  private final TypeToken<T> typeToken;
  private final TypeAdapterFactory skipPast;
  private final GsonContextImpl context = new GsonContextImpl();

  /** The delegate is lazily created because it may not be needed, and creating it may fail. */
  private TypeAdapter<T> delegate;

  public TreeTypeAdapter(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer,
                         EGson EGson, TypeToken<T> typeToken, TypeAdapterFactory skipPast) {
    this.serializer = serializer;
    this.deserializer = deserializer;
    this.EGson = EGson;
    this.typeToken = typeToken;
    this.skipPast = skipPast;
  }

  @Override public T read(JsonReader in) throws IOException {
    if (deserializer == null) {
      return delegate().read(in);
    }
    JsonElement value = Streams.parse(in);
    if (value.isJsonNull()) {
      return null;
    }
    return deserializer.deserialize(value, typeToken.getType(), context);
  }

  @Override public void write(JsonWriter out, T value) throws IOException {
    if (serializer == null) {
      delegate().write(out, value);
      return;
    }
    if (value == null) {
      out.nullValue();
      return;
    }
    JsonElement tree = serializer.serialize(value, typeToken.getType(), context);
    Streams.write(tree, out);
  }

  private TypeAdapter<T> delegate() {
    TypeAdapter<T> d = delegate;
    return d != null
            ? d
            : (delegate = EGson.getDelegateAdapter(skipPast, typeToken));
  }

  /**
   * Returns a new factory that will match each type against {@code exactType}.
   */
  public static TypeAdapterFactory newFactory(TypeToken<?> exactType, Object typeAdapter) {
    return new SingleTypeFactory(typeAdapter, exactType, false, null);
  }

  /**
   * Returns a new factory that will match each type and its raw type against
   * {@code exactType}.
   */
  public static TypeAdapterFactory newFactoryWithMatchRawType(
          TypeToken<?> exactType, Object typeAdapter) {
    // only bother matching raw types if exact type is a raw type
    boolean matchRawType = exactType.getType() == exactType.getRawType();
    return new SingleTypeFactory(typeAdapter, exactType, matchRawType, null);
  }

  /**
   * Returns a new factory that will match each type's raw type for assignability
   * to {@code hierarchyType}.
   */
  public static TypeAdapterFactory newTypeHierarchyFactory(
          Class<?> hierarchyType, Object typeAdapter) {
    return new SingleTypeFactory(typeAdapter, null, false, hierarchyType);
  }

  private static final class SingleTypeFactory implements TypeAdapterFactory {
    private final TypeToken<?> exactType;
    private final boolean matchRawType;
    private final Class<?> hierarchyType;
    private final JsonSerializer<?> serializer;
    private final JsonDeserializer<?> deserializer;

    SingleTypeFactory(Object typeAdapter, TypeToken<?> exactType, boolean matchRawType,
                      Class<?> hierarchyType) {
      serializer = typeAdapter instanceof JsonSerializer
              ? (JsonSerializer<?>) typeAdapter
              : null;
      deserializer = typeAdapter instanceof JsonDeserializer
              ? (JsonDeserializer<?>) typeAdapter
              : null;
      $Gson$Preconditions.checkArgument(serializer != null || deserializer != null);
      this.exactType = exactType;
      this.matchRawType = matchRawType;
      this.hierarchyType = hierarchyType;
    }

    @SuppressWarnings("unchecked") // guarded by typeToken.equals() call
    @Override
    public <T> TypeAdapter<T> create(EGson EGson, TypeToken<T> type) {
      boolean matches = exactType != null
              ? exactType.equals(type) || matchRawType && exactType.getType() == type.getRawType()
              : hierarchyType.isAssignableFrom(type.getRawType());
      return matches
              ? new TreeTypeAdapter<T>((JsonSerializer<T>) serializer,
              (JsonDeserializer<T>) deserializer, EGson, type, this)
              : null;
    }
  }

  private final class GsonContextImpl implements JsonSerializationContext, JsonDeserializationContext {
    @Override public JsonElement serialize(Object src) {
      return EGson.toJsonTree(src);
    }
    @Override public JsonElement serialize(Object src, Type typeOfSrc) {
      return EGson.toJsonTree(src, typeOfSrc);
    }
    @SuppressWarnings("unchecked")
    @Override public <R> R deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
      return (R) EGson.fromJson(json, typeOfT);
    }
  };
}
