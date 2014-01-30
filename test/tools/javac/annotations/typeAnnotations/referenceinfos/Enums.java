/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
 * bug 8030751
 * @ignore
 * @summary Test population of reference info for enums
 * @author wmdietl
 * @compile -g Driver.java ReferenceInfoUtil.java Enums.java
 * @run main Driver Enums
 */
public class Enums {

    @TADescription(annotation = "TA", type = FIELD)
    public String enums1() {
        return "enum Test { @TA E }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = FIELD),
        @TADescription(annotation = "TB", type = FIELD)
    })
    public String enums2() {
        return "enum Test { @TA E1, @TB E2 }";
    }

    @TADescription(annotation = "TA", type = NEW, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String enums3() {
        return "enum Test { E(new @TA Object()); Test(Object o) {} }";
    }
}
