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

package com.sun.tools.classfile;

import java.io.IOException;

import com.sun.tools.javac.code.TypeAnnotations;
import com.sun.tools.javac.comp.TargetType;
import com.sun.tools.javac.util.ListBuffer;

/**
 * See JSR 308 specification, section 4.1
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class ExtendedAnnotation {
    ExtendedAnnotation(ClassReader cr) throws IOException, Annotation.InvalidAnnotation {
        annotation = new Annotation(cr);
        position = read_position(cr);
    }

    public ExtendedAnnotation(ConstantPool constant_pool,
            Annotation annotation,
            TypeAnnotations.Position position) {
        this.annotation = annotation;
        this.position = position;
    }

    public int length() {
        int n = annotation.length();
        n += position_length(position);
        return n;
    }

    public final Annotation annotation;
    public final TypeAnnotations.Position position;

    private static TypeAnnotations.Position read_position(ClassReader cr) throws IOException {
        // Copied from ClassReader
        TypeAnnotations.Position position = new TypeAnnotations.Position();
        int tag = cr.readUnsignedByte();
        TargetType type = TargetType.fromTargetTypeValue(tag);

        position.type = type;

        switch (type) {
        // type case
        case TYPECAST:
        case TYPECAST_GENERIC_OR_ARRAY:
        // object creation
        case INSTANCEOF:
        case INSTANCEOF_GENERIC_OR_ARRAY:
        // new expression
        case NEW:
        case NEW_GENERIC_OR_ARRAY:
        case NEW_TYPE_ARGUMENT:
        case NEW_TYPE_ARGUMENT_GENERIC_OR_ARRAY:
            position.offset = cr.readUnsignedShort();
            break;
         // local variable
        case LOCAL_VARIABLE:
        case LOCAL_VARIABLE_GENERIC_OR_ARRAY:
            // FIXME: check for table length
            int table_length = cr.readUnsignedShort();
            assert table_length == 1;
            position.offset = cr.readUnsignedShort();
            position.length = cr.readUnsignedShort();
            position.index = cr.readUnsignedShort();
            break;
         // method receiver
        case METHOD_RECEIVER:
            // Do nothing
            break;
        // type parameters
        case CLASS_TYPE_PARAMETER:
        case METHOD_TYPE_PARAMETER:
            position.parameter_index = cr.readUnsignedByte();
            break;
        // type parameter bounds
        case CLASS_TYPE_PARAMETER_BOUND:
        case CLASS_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY:
        case METHOD_TYPE_PARAMETER_BOUND:
        case METHOD_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY:
            position.parameter_index = cr.readUnsignedByte();
            position.bound_index = cr.readUnsignedByte();
            break;
         // wildcards
        case WILDCARD_BOUND:
        case WILDCARD_BOUND_GENERIC_OR_ARRAY:
            position.wildcard_position = read_position(cr);
            break;
         // Class extends and implements clauses
        case CLASS_EXTENDS:
        case CLASS_EXTENDS_GENERIC_OR_ARRAY:
            position.type_index = cr.readUnsignedByte();
            break;
        // throws
        case THROWS:
            position.type_index = cr.readUnsignedByte();
            break;
        case CLASS_LITERAL:
            position.offset = cr.readUnsignedShort();
            break;
        // method parameter: not specified
        case METHOD_PARAMETER_GENERIC_OR_ARRAY:
            position.parameter_index = cr.readUnsignedByte();
            break;
        // method type argument: wasn't specified
        case METHOD_TYPE_ARGUMENT:
        case METHOD_TYPE_ARGUMENT_GENERIC_OR_ARRAY:
            position.offset = cr.readUnsignedShort();
            position.type_index = cr.readUnsignedByte();
            break;
        // We don't need to worry abut these
        case METHOD_RETURN_GENERIC_OR_ARRAY:
        case FIELD_GENERIC_OR_ARRAY:
            break;
        case UNKNOWN:
            break;
//        case METHOD_PARAMETER:
//        case METHOD_RETURN:
//        case METHOD_RECEIVER_GENERIC_OR_ARRAY:
//        case CLASS_LITERAL_GENERIC_OR_ARRAY:
//        case METHOD_TYPE_PARAMETER_GENERIC_OR_ARRAY:
//        case FIELD:
//        case THROWS_GENERIC_OR_ARRAY:
//            // method_return sometimes shows up
//            // throw new AssertionError("target unusable: " + position);
//            break;
        default:
//            throw new AssertionError("unknown type: " + position);
        }

        if (type.hasLocation()) {
            int len = cr.readUnsignedShort();
            ListBuffer<Integer> loc = ListBuffer.lb();
            for (int i = 0; i < len; i++)
                loc = loc.append(cr.readUnsignedByte());
            position.location = loc.toList();
        }
        return position;
    }

    private static int position_length(TypeAnnotations.Position pos) {
        int n = 0;
        n += 1; // target_type
        switch (pos.type) {
        // type case
        case TYPECAST:
        case TYPECAST_GENERIC_OR_ARRAY:
        // object creation
        case INSTANCEOF:
        case INSTANCEOF_GENERIC_OR_ARRAY:
        // new expression
        case NEW:
        case NEW_GENERIC_OR_ARRAY:
        case NEW_TYPE_ARGUMENT:
        case NEW_TYPE_ARGUMENT_GENERIC_OR_ARRAY:
            n += 2;
            break;
         // local variable
        case LOCAL_VARIABLE:
        case LOCAL_VARIABLE_GENERIC_OR_ARRAY:
            // FIXME: check for table length
            n += 2; // table_length;
            n += 2; // offset
            n += 2; // length;
            n += 2; // index
            break;
         // method receiver
        case METHOD_RECEIVER:
            // Do nothing
            break;
        // type parameters
        case CLASS_TYPE_PARAMETER:
        case METHOD_TYPE_PARAMETER:
            n += 1; // parameter_index;
            break;
        // type parameter bounds
        case CLASS_TYPE_PARAMETER_BOUND:
        case CLASS_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY:
        case METHOD_TYPE_PARAMETER_BOUND:
        case METHOD_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY:
            n += 1; // parameter_index
            n += 1; // bound_index
            break;
        case WILDCARD_BOUND:
        case WILDCARD_BOUND_GENERIC_OR_ARRAY:
            n += position_length(pos.wildcard_position);
            break;
         // Class extends and implements clauses
        case CLASS_EXTENDS:
        case CLASS_EXTENDS_GENERIC_OR_ARRAY:
            n += 1; // type_index
            break;
        // throws
        case THROWS:
            n += 1; // type_index
            break;
        case CLASS_LITERAL:
            n += 1; // offset
            break;
        // method parameter: not specified
        case METHOD_PARAMETER_GENERIC_OR_ARRAY:
            n += 1; // parameter_index
            break;
        // method type argument: wasn't specified
        case METHOD_TYPE_ARGUMENT:
        case METHOD_TYPE_ARGUMENT_GENERIC_OR_ARRAY:
            n += 2; // offset
            n += 1; // type index
            break;
        // We don't need to worry abut these
        case METHOD_RETURN_GENERIC_OR_ARRAY:
        case FIELD_GENERIC_OR_ARRAY:
            break;
        case UNKNOWN:
            break;
//        case METHOD_PARAMETER:
//        case METHOD_RETURN:
//        case METHOD_RECEIVER_GENERIC_OR_ARRAY:
//        case CLASS_LITERAL_GENERIC_OR_ARRAY:
//        case METHOD_TYPE_PARAMETER_GENERIC_OR_ARRAY:
//        case FIELD:
//        case THROWS_GENERIC_OR_ARRAY:
//            // method_return sometimes shows up
//            // throw new AssertionError("target unusable: " + position);
//            break;
        default:
//            throw new AssertionError("unknown type: " + pos);
        }

        if (pos.type.hasLocation()) {
            n += 2; // length
            n += 1 * pos.location.size(); // actual array size
        }

        return n;
    }
}
