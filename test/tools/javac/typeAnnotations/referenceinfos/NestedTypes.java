/*
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import static com.sun.tools.classfile.TypeAnnotation.TargetType.*;

/*
 * @test
 * @summary Test population of reference info for nested types
 * @compile -g Driver.java ReferenceInfoUtil.java NestedTypes.java
 * @run main Driver NestedTypes
 */
public class NestedTypes {

    @TADescriptions({
        // TODO: should the raw type arguments of Entry be counted???
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER, paramIndex = 0)
    })
    public String test1() {
        return "void test(@TA Map.@TB Entry a) { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER, paramIndex = 0)
    })
    public String test2() {
        return "void test(@TA Map<String,String>.@TB Entry<String,String> a) { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
        		genericLocation = {1, 3}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 2}, paramIndex = 0),
        @TADescription(annotation = "TC", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0}, paramIndex = 0),
        @TADescription(annotation = "TD", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 1}, paramIndex = 0),
        @TADescription(annotation = "TE", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0}, paramIndex = 0),
        @TADescription(annotation = "TF", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 0}, paramIndex = 0),
        @TADescription(annotation = "TG", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1}, paramIndex = 0),
        @TADescription(annotation = "TH", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 0}, paramIndex = 0),
        @TADescription(annotation = "TI", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {1, 1}, paramIndex = 0),
        @TADescription(annotation = "TJ", type = METHOD_PARAMETER, paramIndex = 0),
        @TADescription(annotation = "TK", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0}, paramIndex = 0)
    })
    public String test3() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " void test(@TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[] a) { }\n" +
                "}";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
        		genericLocation = {0, 1, 3}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2}, paramIndex = 0),
        @TADescription(annotation = "TC", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0}, paramIndex = 0),
        @TADescription(annotation = "TD", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 1}, paramIndex = 0),
        @TADescription(annotation = "TE", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0}, paramIndex = 0),
        @TADescription(annotation = "TF", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 0}, paramIndex = 0),
        @TADescription(annotation = "TG", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1}, paramIndex = 0),
        @TADescription(annotation = "TH", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 0}, paramIndex = 0),
        @TADescription(annotation = "TI", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 1}, paramIndex = 0),
        @TADescription(annotation = "TJ", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0}, paramIndex = 0),
        @TADescription(annotation = "TK", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 0}, paramIndex = 0)
    })
    public String test4() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " void test(List<@TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[]> a) { }\n" +
                "}";
    }
}
