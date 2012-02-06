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

    // method parameters

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER, paramIndex = 0)
    })
    public String testParam1() {
        return "void test(@TA Map.@TB Entry a) { }";
    }

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0}, paramIndex = 0)
    })
    public String testParam1b() {
        return "void test(List<@TA Map.@TB Entry> a) { }";
    }

    // The raw type arguments of Entry still count!
    @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
            genericLocation = {2}, paramIndex = 0)
    public String testParam1c() {
        return "void test(@TA java.util.Map.Entry a) { }";
    }

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER, paramIndex = 0)
    })
    public String testParam1d() {
        return "void test(@TA java.util.Map.@TB Entry a) { }";
    }

    // The raw type arguments of Entry still count!
    @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
            genericLocation = {0, 2}, paramIndex = 0)
    public String testParam1e() {
        return "void test(List<@TA java.util.Map.Entry> a) { }";
    }

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0}, paramIndex = 0)
    })
    public String testParam1f() {
        return "void test(List<@TA java.util.Map. @TB Entry> a) { }";
    }

    @TADescription(annotation = "TB", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
           genericLocation = {0}, paramIndex = 0)
    public String testParam1g() {
        return "void test(List<java.util.Map. @TB Entry> a) { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER, paramIndex = 0)
    })
    public String testParam2() {
        return "void test(@TA Map<String,String>.@TB Entry<String,String> a) { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0, 2}, paramIndex = 0),
        @TADescription(annotation = "TB", type = METHOD_PARAMETER_GENERIC_OR_ARRAY,
                genericLocation = {0}, paramIndex = 0)
    })
    public String testParam2b() {
        return "void test(List<@TA Map<String,String>.@TB Entry<String,String>> a) { }";
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
    public String testParam3() {
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
    public String testParam4() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " void test(List<@TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[]> a) { }\n" +
                "}";
    }


    // Local variables

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {2},
                        lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TB", type = LOCAL_VARIABLE,
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1})
    })
    public String testLocal1a() {
        return "void test() { @TA Map.@TB Entry a = null; }";
    }

    // The raw type arguments of Entry still count!
    @TADescription(annotation = "TA", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
            genericLocation = {2},
                    lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1})
    public String testLocal1b() {
        return "void test() { @TA Map.Entry a = null; }";
    }

    @TADescription(annotation = "TB", type = LOCAL_VARIABLE,
            lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1})
    public String testLocal1c() {
        return "void test() { Map.@TB Entry a = null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {2},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TB", type = LOCAL_VARIABLE,
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1})
    })
    public String testLocal2() {
        return "void test() { @TA Map<String,String>.@TB Entry<String,String> a = null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
        		genericLocation = {1, 3},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TB", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 2},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TC", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TD", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 1},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TE", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TF", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 0},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TG", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TH", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 0},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TI", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {1, 1},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TJ", type = LOCAL_VARIABLE,
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TK", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0},
                lvarOffset = {5}, lvarLength = {1}, lvarIndex = {1})
    })
    public String testLocal3() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " void test() { @TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[] a = null; }\n" +
                "}";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
        		genericLocation = {0, 1, 3},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TB", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TC", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TD", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 1},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TE", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TF", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 0},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TG", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TH", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 0},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TI", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 1},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TJ", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1}),
        @TADescription(annotation = "TK", type = LOCAL_VARIABLE_GENERIC_OR_ARRAY,
                genericLocation = {0, 0},
                lvarOffset = {2}, lvarLength = {1}, lvarIndex = {1})
    })
    public String testLocal4() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " void test() { List<@TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[]> a = null; }\n" +
                "}";
    }


    // fields

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {2}),
        @TADescription(annotation = "TB", type = FIELD)
    })
    public String testField1a() {
        return "@TA Map.@TB Entry a;";
    }

    // The raw type arguments of Entry still count!
    @TADescription(annotation = "TA", type = FIELD_GENERIC_OR_ARRAY,
            genericLocation = {2})
    public String testField1b() {
        return "@TA Map.Entry a;";
    }

    @TADescription(annotation = "TB", type = FIELD)
    public String testField1c() {
        return "Map.@TB Entry a;";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {2}),
        @TADescription(annotation = "TB", type = FIELD)
    })
    public String testField2() {
        return "@TA Map<String,String>.@TB Entry<String,String> a;";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = FIELD_GENERIC_OR_ARRAY,
        		genericLocation = {1, 3}),
        @TADescription(annotation = "TB", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 2}),
        @TADescription(annotation = "TC", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0}),
        @TADescription(annotation = "TD", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 1}),
        @TADescription(annotation = "TE", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0}),
        @TADescription(annotation = "TF", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 0}),
        @TADescription(annotation = "TG", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1}),
        @TADescription(annotation = "TH", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 0}),
        @TADescription(annotation = "TI", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {1, 1}),
        @TADescription(annotation = "TJ", type = FIELD),
        @TADescription(annotation = "TK", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0})
    })
    public String testField3() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " @TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[] a;\n" +
                "}";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = FIELD_GENERIC_OR_ARRAY,
        		genericLocation = {0, 1, 3}),
        @TADescription(annotation = "TB", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2}),
        @TADescription(annotation = "TC", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0}),
        @TADescription(annotation = "TD", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 1}),
        @TADescription(annotation = "TE", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0}),
        @TADescription(annotation = "TF", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 0}),
        @TADescription(annotation = "TG", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1}),
        @TADescription(annotation = "TH", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 0}),
        @TADescription(annotation = "TI", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 1}),
        @TADescription(annotation = "TJ", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0}),
        @TADescription(annotation = "TK", type = FIELD_GENERIC_OR_ARRAY,
                genericLocation = {0, 0})
    })
    public String testField4() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " List<@TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[]> a;\n" +
                "}";
    }


    // return types

    @TADescriptions({
        // The raw type arguments of Entry still count!
        @TADescription(annotation = "TA", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {2}),
        @TADescription(annotation = "TB", type = METHOD_RETURN)
    })
    public String testReturn1() {
        return "@TA Map.@TB Entry test() { return null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {2}),
        @TADescription(annotation = "TB", type = METHOD_RETURN)
    })
    public String testReturn2() {
        return "@TA Map<String,String>.@TB Entry<String,String> test() { return null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_RETURN_GENERIC_OR_ARRAY,
        		genericLocation = {1, 3}),
        @TADescription(annotation = "TB", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 2}),
        @TADescription(annotation = "TC", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0}),
        @TADescription(annotation = "TD", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 1}),
        @TADescription(annotation = "TE", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0}),
        @TADescription(annotation = "TF", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 2, 0, 0, 0}),
        @TADescription(annotation = "TG", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1}),
        @TADescription(annotation = "TH", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 0}),
        @TADescription(annotation = "TI", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {1, 1}),
        @TADescription(annotation = "TJ", type = METHOD_RETURN),
        @TADescription(annotation = "TK", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0})
    })
    public String testReturn3() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " @TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[] test() { return null; }\n" +
                "}";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_RETURN_GENERIC_OR_ARRAY,
        		genericLocation = {0, 1, 3}),
        @TADescription(annotation = "TB", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2}),
        @TADescription(annotation = "TC", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0}),
        @TADescription(annotation = "TD", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 1}),
        @TADescription(annotation = "TE", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0}),
        @TADescription(annotation = "TF", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 2, 0, 0, 0}),
        @TADescription(annotation = "TG", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1}),
        @TADescription(annotation = "TH", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 0}),
        @TADescription(annotation = "TI", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 1, 1}),
        @TADescription(annotation = "TJ", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0}),
        @TADescription(annotation = "TK", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 0})
    })
    public String testReturn4() {
        return "class Outer {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " List<@TA Outer . @TB GInner<@TC List<@TD Object @TE[] @TF[]>>. @TG GInner2<@TH Integer, @TI Object> @TJ[] @TK[]> test() { return null; }\n" +
                "}";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 3}),
        @TADescription(annotation = "TB", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 3, 0}),
        @TADescription(annotation = "TC", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 3, 1}),
        @TADescription(annotation = "TD", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 3, 1, 0}),
        @TADescription(annotation = "TE", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 2}),
        @TADescription(annotation = "TF", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 2, 0}),
        @TADescription(annotation = "TG", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 2, 0, 0, 1}),
        @TADescription(annotation = "TH", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 2, 0, 0}),
        @TADescription(annotation = "TI", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0, 2, 0, 0, 0}),
        @TADescription(annotation = "TJ", type = METHOD_RETURN_GENERIC_OR_ARRAY,
                genericLocation = {0}),
    })
    public String testReturn5() {
        return "class GOuter<A, B> {\n" +
                " class GInner<X> {\n" +
                "  class GInner2<Y, Z> {}\n" +
                "}}\n\n" +
                "class Test {\n" +
                " List<@TA GOuter<@TB String, @TC List<@TD Object>> . @TE GInner<@TF List<@TG Object @TH[] @TI[]>>. @TJ GInner2<String, String>> test() { return null; }\n" +
                "}";
    }
}
