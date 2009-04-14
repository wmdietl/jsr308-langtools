package com.sun.tools.javac.code;

import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.util.*;


public class TypeAnnotations {

    public static class Position {

        public TargetType type = TargetType.UNKNOWN;

        // For generic/array types.
        public List<Integer> location = List.nil();

        // Tree position.
        public int pos = -1;

        // For typecasts, type tests, new (and locals, as start_pc).
        public int offset = -1;

        // For locals. arrays same length
        public int[] lvarOffset = new int[] { -1 };
        public int[] lvarLength = new int[] { -1 };
        public int[] lvarIndex = new int[] { -1 };

        // For type parameter bound
        public int bound_index = -1;

        // For type parameter and method parameter
        public int parameter_index = -1;

        // For class extends, implements, and throws classes
        public int type_index = -2;

        // For wildcards
        public Position wildcard_position = null;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            sb.append(type);

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
                sb.append(", offset = ");
                sb.append(offset);
                break;
             // local variable
            case LOCAL_VARIABLE:
            case LOCAL_VARIABLE_GENERIC_OR_ARRAY:
                sb.append(", {");
                for (int i = 0; i < lvarOffset.length; ++i) {
                    if (i != 0) sb.append("; ");
                    sb.append(", start_pc = ");
                    sb.append(lvarOffset[i]);
                    sb.append(", length = ");
                    sb.append(lvarLength[i]);
                    sb.append(", index = ");
                    sb.append(lvarIndex[i]);
                }
                sb.append("}");
                break;
             // method receiver
            case METHOD_RECEIVER:
                // Do nothing
                break;
            // type parameters
            case CLASS_TYPE_PARAMETER:
            case METHOD_TYPE_PARAMETER:
                sb.append(", param_index = ");
                sb.append(parameter_index);
                break;
            // type parameters bound
            case CLASS_TYPE_PARAMETER_BOUND:
            case CLASS_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY:
            case METHOD_TYPE_PARAMETER_BOUND:
            case METHOD_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY:
                sb.append(", param_index = ");
                sb.append(parameter_index);
                sb.append(", bound_index = ");
                sb.append(bound_index);
                break;
             // wildcard
            case WILDCARD_BOUND:
            case WILDCARD_BOUND_GENERIC_OR_ARRAY:
                sb.append(", wild_card = ");
                sb.append(wildcard_position);
                break;
             // Class extends and implements clauses
            case CLASS_EXTENDS:
            case CLASS_EXTENDS_GENERIC_OR_ARRAY:
                sb.append(", type_index = ");
                sb.append(type_index);
                break;
            // throws
            case THROWS:
                sb.append(", type_index = ");
                sb.append(type_index);
                break;
            case CLASS_LITERAL:
                sb.append(", offset = ");
                sb.append(offset);
                break;
            // method parameter: not specified
            case METHOD_PARAMETER_GENERIC_OR_ARRAY:
                sb.append(", param_index = ");
                sb.append(parameter_index);
                break;
            // method type argument: wasn't specified
            case METHOD_TYPE_ARGUMENT:
            case METHOD_TYPE_ARGUMENT_GENERIC_OR_ARRAY:
                sb.append(", offset = ");
                sb.append(offset);
                sb.append(", type_index = ");
                sb.append(type_index);
                break;
            // We don't need to worry abut these
            case METHOD_RETURN_GENERIC_OR_ARRAY:
            case FIELD_GENERIC_OR_ARRAY:
                break;
            case UNKNOWN:
                break;
            default:
//                throw new AssertionError("unknown type: " + type);
            }

            // Append location data for generics/arrays.
            if (type.hasLocation()) {
                sb.append(", location = (");
                sb.append(location);
                sb.append(")");
            }

            sb.append(", pos = ");
            sb.append(pos);

            sb.append(']');
            return sb.toString();
        }
    }

    public Position position;
    public List<Attribute.Compound> annotations;
    public List<TypeAnnotations> erased;

    public TypeAnnotations() {
        this.position = new Position();
        this.annotations = List.nil();
        this.erased = List.nil();
    }

    public String toString() {
        return "<" + this.position +
            (this.annotations.isEmpty() ? "" : " " + this.annotations)
            + ">";
    }
}
