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
 * @summary Test population of reference info for class literals
 * @compile -g Driver.java ReferenceInfoUtil.java ClassLiterals.java
 * @run main Driver ClassLiterals
 */
public class ClassLiterals {

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String returnObject() {
        return "Object returnObject() { return @TA String.class; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = CLASS_LITERAL_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String returnObjectArray() {
        return "Object returnObjectArray() { return @TB String @TA [].class; }";
    }

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String returnPrim() {
        return "Object returnPrim() { return @TA int.class; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = CLASS_LITERAL_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String returnPrimArray() {
        return "Object returnPrimArray() { return @TB int @TA [].class; }";
    }

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String returnVoid() {
        return "Object returnVoid() { return @TA void.class; }";
    }

    // no returnVoidArray

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String initObject() {
        return "void initObject() { Object a =  @TA String.class; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = CLASS_LITERAL_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String initObjectArray() {
        return "void initObjectArray() { Object a = @TB String @TA [].class; }";
    }

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String initPrim() {
        return "void initPrim() { Object a =  @TA int.class; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = CLASS_LITERAL_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String initPrimArray() {
        return "void initPrimArray() { Object a = @TB int @TA [].class; }";
    }

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String initVoid() {
        return "void initVoid() { Object a =  @TA void.class; }";
    }

    // no void array

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String eqtestObject() {
        return "void eqtestObject() { if (null == @TA String.class); }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = CLASS_LITERAL_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String eqtestObjectArray() {
        return "void eqtestObjectArray() { if (null == @TB String @TA [].class); }";
    }

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String eqtestPrim() {
        return "void eqtestPrim() { if (null == @TA int.class); }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = CLASS_LITERAL_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String eqtestPrimArray() {
        return "void eqtestPrimArray() { if (null == @TB int @TA [].class); }";
    }

    @TADescription(annotation = "TA", type = CLASS_LITERAL, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String eqtestVoid() {
        return "void eqtestVoid() { if (null == @TA void.class); }";
    }

    // no void array

}
