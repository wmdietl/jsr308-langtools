/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.TypeAnnotationDesc;
import com.sun.tools.javac.code.TargetType;
import com.sun.tools.javac.code.TypeAnnotationPosition;

/**
 * Utility class for handling type annotations.
 *
 * @author Mahmood Ali
 * @since 1.7
 */
public class TypeAnnotation {

    public static TypeAnnotationDesc[] byType(ProgramElementDoc doc, TargetType type) {
        List<TypeAnnotationDesc> res = new ArrayList<TypeAnnotationDesc>();
        for (TypeAnnotationDesc anno : doc.typeAnnotations()) {
            if (anno.position().type == type)
                res.add(anno);
        }

        return toArray(res);
    }

    public static TypeAnnotationDesc[] byTypeAndIndex(ProgramElementDoc doc, TargetType type, int index) {
        List<TypeAnnotationDesc> res = new ArrayList<TypeAnnotationDesc>();
        for (TypeAnnotationDesc anno : doc.typeAnnotations()) {
            if (anno.position().type == type
                && (anno.position().parameter_index == index
                    || anno.position().type_index == index))
                res.add(anno);
        }

        return toArray(res);
    }

    private static TypeAnnotationDesc[] toArray(List<TypeAnnotationDesc> annos) {
        TypeAnnotationDesc[] res = new TypeAnnotationDesc[annos.size()];
        int i = 0;
        for (TypeAnnotationDesc anno : annos) {
            res[i++] = anno;
        }
        return res;
    }
}
