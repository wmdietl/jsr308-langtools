/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

import static com.sun.tools.classfile.ExtendedAnnotation.TargetType.*;

/*
 * @test
 * @summary Test population of reference info for class extends clauses
 * @compile -g Driver.java ReferenceInfoUtil.java ClassExtends
 * @run main Driver ClassExtends
 */
public class ClassExtends {

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_EXTENDS, typeIndex = -1),
        @TADescription(annotation = "TB", type = CLASS_EXTENDS, typeIndex = 1)
    })
    public String regularClass() {
        return "class Test extends @TA Object implements Cloneable, @TB Runnable {"
               + "  public void run() { } }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_EXTENDS_GENERIC_OR_ARRAY, typeIndex = -1,
                genericLocation = { 0 }),
        @TADescription(annotation = "TB", type = CLASS_EXTENDS_GENERIC_OR_ARRAY, typeIndex = 1,
                genericLocation  = { 1 })
    })
    public String regularClassExtendsParametrized() {
        return "class Test extends HashMap<@TA String, String> implements Cloneable, Map<String, @TB String>{ } ";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_EXTENDS, typeIndex = -1),
        @TADescription(annotation = "TB", type = CLASS_EXTENDS, typeIndex = 1)
    })
    public String abstractClass() {
        return "abstract class Test extends @TA Date implements Cloneable, @TB Runnable {"
               + "  public void run() { } }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_EXTENDS_GENERIC_OR_ARRAY, typeIndex = -1,
                genericLocation = { 0 }),
        @TADescription(annotation = "TB", type = CLASS_EXTENDS_GENERIC_OR_ARRAY, typeIndex = 1,
                genericLocation  = { 1 })
    })
    public String abstractClassExtendsParametrized() {
        return "abstract class Test extends HashMap<@TA String, String> implements Cloneable, Map<String, @TB String>{ } ";
    }

    @TADescription(annotation = "TB", type = CLASS_EXTENDS, typeIndex = 1)
    public String regularInterface() {
        return "interface Test extends Cloneable, @TB Runnable { }";
    }

    @TADescription(annotation = "TB", type = CLASS_EXTENDS_GENERIC_OR_ARRAY, typeIndex = 1,
            genericLocation  = { 1 })
    public String regularInterfaceExtendsParametrized() {
        return "interface Test extends Cloneable, Map<String, @TB String>{ } ";
    }

    @TADescription(annotation = "TB", type = CLASS_EXTENDS, typeIndex = 1)
    public String regularEnum() {
        return "enum Test implements Cloneable, @TB Runnable { TEST; public void run() { } }";
    }

    @TADescription(annotation = "TB", type = CLASS_EXTENDS_GENERIC_OR_ARRAY, typeIndex = 1,
            genericLocation  = { 0 })
    public String regularEnumExtendsParametrized() {
        return
            "enum Test implements Cloneable, Comparator<@TB String> { TEST;  "
            + "public int compare(String a, String b) { return 0; }}";
    }

}
