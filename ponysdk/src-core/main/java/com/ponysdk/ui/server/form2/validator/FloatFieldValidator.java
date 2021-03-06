/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *  
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.ui.server.form2.validator;

public class FloatFieldValidator implements FieldValidator {

    private static final String ERROR_MESSAGE = "Not a float";

    private static ValidationResult isAFloat(final String value) {
        try {
            Float.parseFloat(value);
            return ValidationResult.newOKValidationResult();
        } catch (final Exception e) {
            return ValidationResult.newFailedValidationResult(ERROR_MESSAGE);
        }
    }

    @Override
    public ValidationResult isValid(final String value) {
        if (value == null || value.isEmpty()) return ValidationResult.newOKValidationResult();
        return isAFloat(value);
    }
}
