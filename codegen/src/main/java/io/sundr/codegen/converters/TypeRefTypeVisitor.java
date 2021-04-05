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

package io.sundr.codegen.converters;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.AbstractTypeVisitor6;

import io.sundr.codegen.DefinitionRepository;
import io.sundr.codegen.functions.element.ElementContext;
import io.sundr.codegen.model.ClassRefBuilder;
import io.sundr.codegen.model.PrimitiveRefBuilder;
import io.sundr.codegen.model.TypeParamRefBuilder;
import io.sundr.codegen.model.TypeRef;
import io.sundr.codegen.model.VoidRefBuilder;

public class TypeRefTypeVisitor extends AbstractTypeVisitor6<TypeRef, Integer> {

  private final ElementContext context;

  public TypeRefTypeVisitor(ElementContext context) {
    this.context = context;
  }

  public TypeRef visitPrimitive(PrimitiveType t, Integer dimension) {
    return new PrimitiveRefBuilder().withName(t.getKind().name().toLowerCase()).withDimensions(dimension).build();
  }

  public TypeRef visitNull(NullType t, Integer dimension) {
    return null;
  }

  public TypeRef visitArray(ArrayType t, Integer dimension) {
    return t.getComponentType().accept(this, dimension + 1);
  }

  public TypeRef visitDeclared(DeclaredType t, Integer dimension) {
    List<TypeRef> arguments = new ArrayList<TypeRef>();
    for (TypeMirror typeMirror : t.getTypeArguments()) {
      TypeRef arg = typeMirror.accept(this, dimension);
      if (arg != null) {
        arguments.add(arg);
      }
    }
    TypeElement element = (TypeElement) t.asElement();

    //TODO: need a cleaner way to get this registered.
    if (!DefinitionRepository.getRepository().hasDefinition(element.toString())) {
      context.getReferences().add(element);
    }

    String fqcn = element.toString();
    return new ClassRefBuilder().withFullyQualifiedName(fqcn).withDimensions(dimension)
        .withArguments(arguments)
        .build();
  }

  public TypeRef visitError(ErrorType t, Integer dimension) {
    TypeElement element = (TypeElement) t.asElement();
    String fqcn = element.toString();
    return new ClassRefBuilder().withFullyQualifiedName(fqcn)
        .build();
  }

  public TypeRef visitTypeVariable(TypeVariable t, Integer dimension) {
    return new TypeParamRefBuilder().withName(t.asElement().getSimpleName().toString()).build();
  }

  public TypeRef visitWildcard(WildcardType t, Integer dimension) {
    return null;
  }

  public TypeRef visitExecutable(ExecutableType t, Integer dimension) {
    return null;
  }

  public TypeRef visitNoType(NoType t, Integer dimension) {
    return new VoidRefBuilder().build();
  }

  public TypeRef visitUnknown(TypeMirror t, Integer dimension) {
    return null;
  }

}
