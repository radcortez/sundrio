/*
 * Copyright 2016 The original authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.sundr.dsl.internal.visitors;

import java.util.ArrayList;
import java.util.List;

import io.sundr.builder.Visitor;
import io.sundr.model.TypeDefBuilder;
import io.sundr.model.TypeParamDef;

public class TypeParamDefReplace implements Visitor<TypeDefBuilder> {

  private final TypeParamDef target;
  private final TypeParamDef replacement;

  public TypeParamDefReplace(TypeParamDef target, TypeParamDef replacement) {
    this.target = target;
    this.replacement = replacement;
  }

  public void visit(TypeDefBuilder builder) {
    List<TypeParamDef> updated = new ArrayList<TypeParamDef>();
    for (TypeParamDef parameter : builder.getParameters()) {
      if (parameter.equals(target)) {
        updated.add(replacement);
      } else {
        updated.add(parameter);
      }
    }
  }
}
