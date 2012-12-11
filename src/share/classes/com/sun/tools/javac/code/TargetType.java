/*
 * Copyright (c) 2008, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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

package com.sun.tools.javac.code;

import com.sun.tools.javac.util.Assert;

/**
 * Describes the type of program element an extended annotation (or extended
 * compound attribute) targets.
 *
 * By comparison, a Tree.Kind has enum values for all elements in the AST, and
 * it does not provide enough resolution for type arguments (i.e., whether an
 * annotation targets a type argument in a local variable, method return type,
 * or a typecast).
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
// Code duplicated in com.sun.tools.classfile.TypeAnnotation.TargetType
public enum TargetType {
    /** For annotations on a class type parameter declaration. */
    CLASS_TYPE_PARAMETER(0x00),

    /** For annotations on a method type parameter declaration. */
    METHOD_TYPE_PARAMETER(0x02),

    /** For annotations on the type of an "extends" or "implements" clause. */
    CLASS_EXTENDS(0x10),

    /** For annotations on a bound of a type parameter of a class. */
    CLASS_TYPE_PARAMETER_BOUND(0x12),

    /** For annotations on a bound of a type parameter of a method. */
    METHOD_TYPE_PARAMETER_BOUND(0x14),

    /** For annotations on a field. */
    FIELD(0x16),

    /** For annotations on a method return type. */
    METHOD_RETURN(0x18),

    /** For annotations on the method receiver. */
    METHOD_RECEIVER(0x1A),

    /** For annotations on a method parameter. */
    METHOD_PARAMETER(0x1C),

    /** For annotations on a throws clause in a method declaration. */
    THROWS(0x1E),

    /** For annotations on a local variable. */
    LOCAL_VARIABLE(0x80, true),

    /** For annotations on a resource variable. */
    RESOURCE_VARIABLE(0x82, true),

    /** For annotations on an exception parameter. */
    EXCEPTION_PARAMETER(0x84, true),

    /** For annotations on a typecast. */
    CAST(0x86, true),

    /** For annotations on a type test. */
    INSTANCEOF(0x88, true),

    /** For annotations on an object creation expression. */
    NEW(0x8A, true),

    /** For annotations on a type argument of an object creation expression. */
    CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(0x8C, true),

    /** For annotations on a type argument of a method call. */
    METHOD_INVOCATION_TYPE_ARGUMENT(0x8E, true),

    /** For annotations on a lambda parameter type. */
    LAMBDA_FORMAL_PARAMETER(0x90, true),

    /** For annotations on a type argument of a method reference. */
    METHOD_REFERENCE_TYPE_ARGUMENT(0x92, true),

    /** For annotations with an unknown target. */
    UNKNOWN(0xFFFF);

    private static final int MAXIMUM_TARGET_TYPE_VALUE = 0x92;

    private final int targetTypeValue;
    private final boolean isLocal;

    private TargetType(int targetTypeValue) {
        this(targetTypeValue, false);
    }

    private TargetType(int targetTypeValue, boolean isLocal) {
        if (targetTypeValue < Character.MIN_VALUE
                || targetTypeValue > Character.MAX_VALUE)
                // TODO: Is a u2 a Short or a Character? jvm.ClassReader has a nextChar method,
                // but classfile.ClassReader has a readUnsignedShort method.
                // Short is signed, char is unsigned -> use char.
                Assert.error("Attribute type value needs to be a char: " + targetTypeValue);
        this.targetTypeValue = (char)targetTypeValue;
        this.isLocal = isLocal;
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is exclusively a tree in a method body
     *
     * Note: wildcard bound targets could target a local tree and a class
     * member declaration signature tree
     */
    public boolean isLocal() {
        return isLocal;
    }

    public int targetTypeValue() {
        return this.targetTypeValue;
    }

    private static TargetType[] targets = null;

    private static TargetType[] buildTargets() {
        TargetType[] targets = new TargetType[MAXIMUM_TARGET_TYPE_VALUE + 1];
        TargetType[] alltargets = values();
        for (TargetType target : alltargets) {
            if (target.targetTypeValue != UNKNOWN.targetTypeValue)
                targets[target.targetTypeValue] = target;
        }
        for (int i = 0; i <= MAXIMUM_TARGET_TYPE_VALUE; ++i) {
            if (targets[i] == null)
                targets[i] = UNKNOWN;
        }
        return targets;
    }

    public static boolean isValidTargetTypeValue(int tag) {
        if (targets == null)
            targets = buildTargets();
        if (((char)tag) == ((char)UNKNOWN.targetTypeValue))
            return true;
        return (tag >= 0 && tag < targets.length);
    }

    public static TargetType fromTargetTypeValue(int tag) {
        if (targets == null)
            targets = buildTargets();

        if (((char)tag) == ((char)UNKNOWN.targetTypeValue))
            return UNKNOWN;

        if (tag < 0 || tag >= targets.length)
            Assert.error("Unknown TargetType: " + tag);
        return targets[tag];
    }
}
