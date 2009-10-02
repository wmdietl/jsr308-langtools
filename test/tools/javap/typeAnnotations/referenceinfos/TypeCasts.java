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
 * @summary Test population of reference info for type casts
 * @compile -g Driver.java ReferenceInfoUtil.java TypeCasts.java
 * @run main Driver TypeCasts
 */
public class TypeCasts {

    @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String returnObject() {
        return "Object returnObject() { return (@TA String)null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String returnObjectArray() {
        return "Object returnObjectArray() { return (@TB String @TA [])null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String returnObjectGeneric() {
        return "Object returnObjectGeneric() { return (@TA List<@TB String>)null; }";
    }

    @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String returnPrim() {
        return "Object returnPrim() { return (@TA int)0; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String returnPrimArray() {
        return "Object returnPrimArray() { return (@TB int @TA [])null; }";
    }

    @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String initObject() {
        return "void initObject() { Object a =  (@TA String)null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String initObjectArray() {
        return "void initObjectArray() { Object a = (@TB String @TA [])null; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String initObjectGeneric() {
        return "void initObjectGeneric() { Object a = (@TA List<@TB String>)null; }";
    }

    @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String initPrim() {
        return "void initPrim() { Object a =  (@TA int)0; }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String initPrimArray() {
        return "void initPrimArray() { Object a = (@TB int @TA [])null; }";
    }

    @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String eqtestObject() {
        return "void eqtestObject() { if (null == (@TA String)null); }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String eqtestObjectArray() {
        return "void eqtestObjectArray() { if (null == (@TB String @TA [])null); }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String eqtestObjectGeneric() {
        return "void eqtestObjectGeneric() { if (null == (@TA List<@TB String >)null); }";
    }

    @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE)
    public String eqtestPrim() {
        return "void eqtestPrim() { if (0 == (@TA int)0); }";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = TYPECAST, offset = ReferenceInfoUtil.IGNORE_VALUE),
        @TADescription(annotation = "TB", type = TYPECAST_GENERIC_OR_ARRAY,
                genericLocation = { 0 }, offset = ReferenceInfoUtil.IGNORE_VALUE),
    })
    public String eqtestPrimArray() {
        return "void eqtestPrimArray() { if (null == (@TB int @TA [])null); }";
    }

}
