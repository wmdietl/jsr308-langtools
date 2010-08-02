/*
 * Copyright 2003-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.tools.javadoc;


import com.sun.javadoc.*;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Type.ClassType;


/**
 * Implementation of <code>AnnotatedType</code>, which
 * represents an annotated type.
 *
 * @author Mahmood Ali
 * @since 1.7
 */
public class AnnotatedTypeImpl
        extends AbstractTypeImpl implements AnnotatedType {

    AnnotatedTypeImpl(DocEnv env, Type type) {
        super(env, type);
    }

    /**
     * Get the annotations of this program element.
     * Return an empty array if there are none.
     */
    public AnnotationDesc[] annotations() {
        AnnotationDesc res[] = new AnnotationDesc[type.typeAnnotations.length()];
        int i = 0;
        for (Attribute.Compound a : type.typeAnnotations) {
            res[i++] = new AnnotationDescImpl(env, a);
        }
        return res;
    }

    @Override
    public com.sun.javadoc.Type underlyingType() {
        return TypeMaker.getType(env, type, true, false);
    }

    @Override
    public AnnotatedType asAnnotatedType() {
        return this;
    }

    @Override
    public String toString() {
        return typeName();
    }

    @Override
    public String dimension() {
        return this.underlyingType().dimension();
    }

    @Override
    public boolean isPrimitive() {
        return this.underlyingType().isPrimitive();
    }

    @Override
    public ClassDoc asClassDoc() {
        return this.underlyingType().asClassDoc();
    }

    @Override
    public TypeVariable asTypeVariable() {
        return this.underlyingType().asTypeVariable();
    }

    @Override
    public WildcardType asWildcardType() {
        return this.underlyingType().asWildcardType();
    }

    @Override
    public ParameterizedType asParameterizedType() {
        return this.underlyingType().asParameterizedType();
    }
}
