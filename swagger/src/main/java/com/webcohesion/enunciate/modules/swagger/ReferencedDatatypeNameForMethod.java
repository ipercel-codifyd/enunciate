/**
 * Copyright © 2006-2016 Web Cohesion (info@webcohesion.com)
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
package com.webcohesion.enunciate.modules.swagger;

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Template method used to determine the objective-c "simple name" of an accessor.
 *
 * @author Ryan Heaton
 */
public class ReferencedDatatypeNameForMethod implements TemplateMethodModelEx {

  public Object exec(List list) throws TemplateModelException {
    if (list.size() < 1) {
      throw new TemplateModelException("The datatypeNameFor method must have a parameter.");
    }

    TemplateModel from = (TemplateModel) list.get(0);
    BeansWrapper wrpper = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
    Object unwrapped = wrpper.unwrap(from);
    BaseType baseType = null;
    if (unwrapped instanceof DataTypeReference) {
      DataTypeReference reference = (DataTypeReference) unwrapped;
      baseType = reference.getBaseType();
    }

    if (baseType == null) {
      throw new TemplateModelException("No referenced data type name for: " + unwrapped);
    }

    String defaultType = "file";
    if (list.size() > 1) {
      defaultType = wrpper.unwrap((TemplateModel) list.get(1)).toString();
    }

    switch (baseType) {
      case bool:
        return "boolean";
      case number:
        return "number";
      case string:
        return "string";
      default:
        return defaultType;
    }
  }
}