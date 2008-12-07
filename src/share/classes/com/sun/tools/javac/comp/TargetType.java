package com.sun.tools.javac.comp;

import static com.sun.tools.javac.comp.TargetType.TargetAttribute.*;

/**
 * Describes the type of program element an extended annotation (or extended
 * compound attribute) targets.
 *
 * By comparison, a Tree.Kind has enum values for all elements in the AST, and
 * it does not provide enough resoultion for type arguments (i.e., whether an
 * annotation targets a type argument in a local variable, method return type,
 * or a typecast).
 *
 * Target types come in pairs. The first of each pair is for annotations on the
 * top level of a type use; the second is for annotations on inner types. Thus,
 * a TargetType is for inner types if and only if its ordinal is odd.
 */
public enum TargetType {

    /** For annotations on typecasts. */
    TYPECAST(0x00, NO_ATTRIBUTE),

    /** For annotations on a type argument or nested array of a typecast. */
    TYPECAST_GENERIC_OR_ARRAY(0x01, HAS_LOCATION),

    /** For annotations on type tests. */
    INSTANCEOF(0x02, NO_ATTRIBUTE),

    /** For annotations on a type argument or nested array of a type test. */
    INSTANCEOF_GENERIC_OR_ARRAY(0x03, HAS_LOCATION),

    /** For annotations on object creation expressions. */
    NEW(0x04, NO_ATTRIBUTE),

    /**
     * For annotations on a type argument or nested array of an object creation
     * expression.
     */
    NEW_GENERIC_OR_ARRAY(0x05, HAS_LOCATION),


    /** For annotations on the method receiver. */
    METHOD_RECEIVER(0x06, NO_ATTRIBUTE),

    /**
     * For annotations on a type argument or nested array of the method
     * receiver.
     *
     * Deprecated because such annotations are not allowed (yet), but included
     * so that the numbering works out.
     */
    @Deprecated METHOD_RECEIVER_GENERIC_OR_ARRAY(0x07, HAS_LOCATION),

    /** For annotations on local variables. */
    LOCAL_VARIABLE(0x08, NO_ATTRIBUTE),

    /** For annotations on a type argument or nested array of a local. */
    LOCAL_VARIABLE_GENERIC_OR_ARRAY(0x09, HAS_LOCATION),

    /**
     * For annotations on a method return type.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    @Deprecated METHOD_RETURN(0x0A, NO_ATTRIBUTE),

    /**
     * For annotations on a type argument or nested array of a method return
     * type.
     */
    METHOD_RETURN_GENERIC_OR_ARRAY(0x0B, HAS_LOCATION),

    /**
     * For annotations on a method parameter.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    @Deprecated METHOD_PARAMETER(0x0C, NO_ATTRIBUTE),

    /** For annotations on a type argument or nested array of a method parameter. */
    METHOD_PARAMETER_GENERIC_OR_ARRAY(0x0D, HAS_LOCATION),

    /**
     * For annotations on a field.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    @Deprecated FIELD(0x0E, NO_ATTRIBUTE),

    /** For annotations on a type argument or nested array of a field. */
    FIELD_GENERIC_OR_ARRAY(0x0F, HAS_LOCATION),

    /** For annotations on a bound of a type parameter of a class. */
    CLASS_TYPE_PARAMETER_BOUND(0x10, HAS_BOUND | HAS_PARAMETER),

    /**
     * For annotations on a type argument or nested array of a bound of a type
     * parameter of a class.
     */
    CLASS_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY(0x11, HAS_BOUND | HAS_LOCATION | HAS_PARAMETER),

    /** For annotations on a bound of a type parameter of a method. */
    METHOD_TYPE_PARAMETER_BOUND(0x12, HAS_BOUND | HAS_PARAMETER),

    /**
     * For annotations on a type argument or nested array of a bound of a type
     * parameter of a method.
     */
    METHOD_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY(0x13, HAS_BOUND | HAS_LOCATION | HAS_PARAMETER),

    /** For annotations on the type of an "extends" or "implements" clause. */
    CLASS_EXTENDS(0x14, NO_ATTRIBUTE),

    /** For annotations on the inner type of an "extends" or "implements" clause. */
    CLASS_EXTENDS_GENERIC_OR_ARRAY(0x15, HAS_LOCATION),

    /** For annotations on a throws clause in a method declaration. */
    THROWS(0x16, NO_ATTRIBUTE),
    @Deprecated THROWS_GENERIC_OR_ARRAY(0x17, HAS_LOCATION),

    /** For annotations in type arguments of object creation expressions. */
    NEW_TYPE_ARGUMENT(0x18, NO_ATTRIBUTE),
    NEW_TYPE_ARGUMENT_GENERIC_OR_ARRAY(0x19, HAS_LOCATION),

    METHOD_TYPE_ARGUMENT(0x1A, NO_ATTRIBUTE),
    METHOD_TYPE_ARGUMENT_GENERIC_OR_ARRAY(0x1B, HAS_LOCATION),

    WILDCARD_BOUND(0x1C, HAS_BOUND),
    WILDCARD_BOUND_GENERIC_OR_ARRAY(0x1D, HAS_BOUND | HAS_LOCATION),

    CLASS_LITERAL(0x1E, NO_ATTRIBUTE),
    @Deprecated CLASS_LITERAL_GENERIC_OR_ARRAY(0x1F, HAS_LOCATION),

    METHOD_TYPE_PARAMETER(0x20, HAS_PARAMETER),
    @Deprecated METHOD_TYPE_PARAMETER_GENERIC_OR_ARRAY(0x21, HAS_LOCATION | HAS_PARAMETER),

    CLASS_TYPE_PARAMETER(0x22, HAS_PARAMETER),
    @Deprecated CLASS_TYPE_PARAMETER_GENERIC_OR_ARRAY(0x23, HAS_LOCATION | HAS_PARAMETER),

    /** For annotations with an unknown target. */
    UNKNOWN(-1, NO_ATTRIBUTE);

    private final int targetTypeValue;
    private final int flags;

    TargetType(int targetTypeValue, int flags) {
        assert targetTypeValue >= Byte.MIN_VALUE;
        assert targetTypeValue <= Byte.MAX_VALUE;
        this.targetTypeValue = (byte)targetTypeValue;
        this.flags = flags;
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is an inner type of a generic or array type.
     *
     * @return true if this TargetType represents an annotation on an inner
     *         type, false otherwise
     */
    public boolean hasLocation() {
        return (flags & HAS_LOCATION) != 0;
    }

    public TargetType getGenericComplement() {
        if (hasLocation())
            return this;
        else
            return fromTargetTypeValue(targetTypeValue() + 1);
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target has a parameter index.
     *
     * @return true if this TargetType has a parameter index,
     *         false otherwise
     */
    public boolean hasParameter() {
        return (flags & HAS_PARAMETER) != 0;
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is a type parameter bound.
     *
     * @return true if this TargetType represents an type parameter bound
     *         annotation, false otherwise
     */
    public boolean hasBound() {
        return (flags & HAS_BOUND) != 0;
    }

    public int targetTypeValue() {
        return this.targetTypeValue;
    }

    public static TargetType fromTargetTypeValue(int tag) {
        if (tag == UNKNOWN.targetTypeValue)
            return UNKNOWN;
        if (tag < 0 || tag >= values().length)
            throw new IllegalArgumentException("Unknown TargetType: " + tag);
        return values()[tag];
    }

    static class TargetAttribute {
        static final int NO_ATTRIBUTE = 0;
        static final int HAS_LOCATION = 1 << 0;
        static final int HAS_PARAMETER = 1 << 1;
        static final int HAS_BOUND = 1 << 2;
    }
}
