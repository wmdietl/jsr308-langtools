/*
 * Copyright (c) 2009 Oracle and/or its affiliates. All rights reserved.
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
 * @summary Test population of reference info for class type parameters
 * @compile -g Driver.java ReferenceInfoUtil.java ClassTypeParam.java
 * @run main Driver ClassTypeParam
 */
public class ClassTypeParam {

    @TADescriptions({
        @TADescription(annotation = "A", type = CLASS_TYPE_PARAMETER, paramIndex = 0),
        @TADescription(annotation = "TB", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 0, boundIndex = 0),
        @TADescription(annotation = "C", type = CLASS_TYPE_PARAMETER, paramIndex = 1),
        @TADescription(annotation = "TD", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 0),
        @TADescription(annotation = "TE", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 1)
    })
    public String regularClass() {
        return "class Test<@A K extends @TB Date, @C V extends @TD Object & @TE Cloneable> { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 0, boundIndex = 0),
        @TADescription(annotation = "TB", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 0, boundIndex = 0, genericLocation = {1}),
        @TADescription(annotation = "TC", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 1),
        @TADescription(annotation = "TD", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 1, boundIndex = 1, genericLocation = {0}),
        @TADescription(annotation = "TE", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 1, boundIndex = 1, genericLocation = {0, 0}),
        @TADescription(annotation = "TF", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 0)
    })
    public String regularClassParameterized() {
        return "class Test<K extends @TA Map<String, @TB String>, V extends @TF Object & @TC List<@TD List<@TE Object>>> { }";
    }

    @TADescriptions({
        @TADescription(annotation = "A", type = CLASS_TYPE_PARAMETER, paramIndex = 0),
        @TADescription(annotation = "TB", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 0, boundIndex = 0),
        @TADescription(annotation = "C", type = CLASS_TYPE_PARAMETER, paramIndex = 1),
        @TADescription(annotation = "TD", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 0),
        @TADescription(annotation = "TE", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 1)
    })
    public String abstractClass() {
        return "abstract class Test<@A K extends @TB Date, @C V extends @TD Object & @TE Cloneable> { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 0, boundIndex = 0),
        @TADescription(annotation = "TB", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 0, boundIndex = 0, genericLocation = {1}),
        @TADescription(annotation = "TC", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 1),
        @TADescription(annotation = "TD", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 1, boundIndex = 1, genericLocation = {0}),
        @TADescription(annotation = "TE", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 1, boundIndex = 1, genericLocation = {0, 0}),
        @TADescription(annotation = "TF", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 0)
    })
    public String abstractClassParameterized() {
        return "abstract class Test<K extends @TA Map<String, @TB String>, V extends @TF Object & @TC List<@TD List<@TE Object>>> { }";
    }

    @TADescriptions({
        @TADescription(annotation = "A", type = CLASS_TYPE_PARAMETER, paramIndex = 0),
        @TADescription(annotation = "TB", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 0, boundIndex = 0),
        @TADescription(annotation = "C", type = CLASS_TYPE_PARAMETER, paramIndex = 1),
        @TADescription(annotation = "TD", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 0),
        @TADescription(annotation = "TE", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 1)
    })
    public String regularInterface() {
        return "interface Test<@A K extends @TB Date, @C V extends @TD Object & @TE Cloneable> { }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 0, boundIndex = 0),
        @TADescription(annotation = "TB", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 0, boundIndex = 0, genericLocation = {1}),
        @TADescription(annotation = "TC", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 1),
        @TADescription(annotation = "TD", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 1, boundIndex = 1, genericLocation = {0}),
        @TADescription(annotation = "TE", type = CLASS_TYPE_PARAMETER_BOUND_COMPONENT, paramIndex = 1, boundIndex = 1, genericLocation = {0, 0}),
        @TADescription(annotation = "TF", type = CLASS_TYPE_PARAMETER_BOUND, paramIndex = 1, boundIndex = 0)
    })
    public String regularInterfaceParameterized() {
        return "interface Test<K extends @TA Map<String, @TB String>, V extends @TF Object & @TC List<@TD List<@TE Object>>> { }";
    }

    @TADescription(annotation = "TA", type = METHOD_RETURN)
    public String useInReturn1() {
        return "class Test<T> { @TA T m() { throw new RuntimeException(); } }";
    }

    @TADescription(annotation = "TA", type = METHOD_RETURN_COMPONENT, genericLocation = {0})
    public String useInReturn2() {
        return "class Test<T> { Class<@TA T> m() { throw new RuntimeException(); } }";
    }

    @TADescription(annotation = "TA", type = METHOD_PARAMETER_COMPONENT,
            paramIndex = 0, genericLocation = {0})
    public String useInParam1() {
        return "class Test<T> { void m(Class<@TA T> p) { throw new RuntimeException(); } }";
    }

    @TADescription(annotation = "TA", type = METHOD_PARAMETER_COMPONENT,
            paramIndex = 0, genericLocation = {0})
    public String useInParam2() {
        return "class Test { void m(Class<@TA Object> p) { throw new RuntimeException(); } }";
    }
}
