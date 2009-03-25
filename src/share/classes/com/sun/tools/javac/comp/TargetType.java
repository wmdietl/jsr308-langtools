package com.sun.tools.javac.comp;

import static com.sun.tools.javac.comp.TargetType.TargetAttribute.*;

import java.util.EnumSet;
import java.util.Set;

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
    TYPECAST(0x00, EnumSet.noneOf(TargetAttribute.class)),

    /** For annotations on a type argument or nested array of a typecast. */
    TYPECAST_GENERIC_OR_ARRAY(0x01, EnumSet.of(HasLocation)),

    /** For annotations on type tests. */
    INSTANCEOF(0x02, EnumSet.noneOf(TargetAttribute.class)),

    /** For annotations on a type argument or nested array of a type test. */
    INSTANCEOF_GENERIC_OR_ARRAY(0x03, EnumSet.of(HasLocation)),

    /** For annotations on object creation expressions. */
    NEW(0x04, EnumSet.noneOf(TargetAttribute.class)),

    /**
     * For annotations on a type argument or nested array of an object creation
     * expression.
     */
    NEW_GENERIC_OR_ARRAY(0x05, EnumSet.of(HasLocation)),


    /** For annotations on the method receiver. */
    METHOD_RECEIVER(0x06, EnumSet.noneOf(TargetAttribute.class)),

    /**
     * For annotations on a type argument or nested array of the method
     * receiver.
     *
     * Deprecated because such annotations are not allowed (yet), but included
     * so that the numbering works out.
     */
    //@Deprecated METHOD_RECEIVER_GENERIC_OR_ARRAY(0x07, EnumSet.of(HasLocation)),

    /** For annotations on local variables. */
    LOCAL_VARIABLE(0x08, EnumSet.noneOf(TargetAttribute.class)),

    /** For annotations on a type argument or nested array of a local. */
    LOCAL_VARIABLE_GENERIC_OR_ARRAY(0x09, EnumSet.of(HasLocation)),

    /**
     * For annotations on a method return type.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    //@Deprecated METHOD_RETURN(0x0A, EnumSet.noneOf(TargetAttribute.class)),

    /**
     * For annotations on a type argument or nested array of a method return
     * type.
     */
    METHOD_RETURN_GENERIC_OR_ARRAY(0x0B, EnumSet.of(HasLocation)),

    /**
     * For annotations on a method parameter.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    //@Deprecated METHOD_PARAMETER(0x0C, EnumSet.noneOf(TargetAttribute.class)),

    /** For annotations on a type argument or nested array of a method parameter. */
    METHOD_PARAMETER_GENERIC_OR_ARRAY(0x0D, EnumSet.of(HasLocation)),

    /**
     * For annotations on a field.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    //@Deprecated FIELD(0x0E, EnumSet.noneOf(TargetAttribute.class)),

    /** For annotations on a type argument or nested array of a field. */
    FIELD_GENERIC_OR_ARRAY(0x0F, EnumSet.of(HasLocation)),

    /** For annotations on a bound of a type parameter of a class. */
    CLASS_TYPE_PARAMETER_BOUND(0x10, EnumSet.of(HasBound, HasParameter)),

    /**
     * For annotations on a type argument or nested array of a bound of a type
     * parameter of a class.
     */
    CLASS_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY(0x11, EnumSet.of(HasBound, HasLocation, HasParameter)),

    /** For annotations on a bound of a type parameter of a method. */
    METHOD_TYPE_PARAMETER_BOUND(0x12, EnumSet.of(HasBound, HasParameter)),

    /**
     * For annotations on a type argument or nested array of a bound of a type
     * parameter of a method.
     */
    METHOD_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY(0x13, EnumSet.of(HasBound, HasLocation, HasParameter)),

    /** For annotations on the type of an "extends" or "implements" clause. */
    CLASS_EXTENDS(0x14, EnumSet.noneOf(TargetAttribute.class)),

    /** For annotations on the inner type of an "extends" or "implements" clause. */
    CLASS_EXTENDS_GENERIC_OR_ARRAY(0x15, EnumSet.of(HasLocation)),

    /** For annotations on a throws clause in a method declaration. */
    THROWS(0x16, EnumSet.noneOf(TargetAttribute.class)),
    //@Deprecated THROWS_GENERIC_OR_ARRAY(0x17, EnumSet.of(HasLocation)),

    /** For annotations in type arguments of object creation expressions. */
    NEW_TYPE_ARGUMENT(0x18, EnumSet.noneOf(TargetAttribute.class)),
    NEW_TYPE_ARGUMENT_GENERIC_OR_ARRAY(0x19, EnumSet.of(HasLocation)),

    METHOD_TYPE_ARGUMENT(0x1A, EnumSet.noneOf(TargetAttribute.class)),
    METHOD_TYPE_ARGUMENT_GENERIC_OR_ARRAY(0x1B, EnumSet.of(HasLocation)),

    WILDCARD_BOUND(0x1C, EnumSet.of(HasBound)),
    WILDCARD_BOUND_GENERIC_OR_ARRAY(0x1D, EnumSet.of(HasBound, HasLocation)),

    CLASS_LITERAL(0x1E, EnumSet.noneOf(TargetAttribute.class)),
    CLASS_LITERAL_GENERIC_OR_ARRAY(0x1F, EnumSet.of(HasLocation)),

    METHOD_TYPE_PARAMETER(0x20, EnumSet.of(HasParameter)),
    //@Deprecated METHOD_TYPE_PARAMETER_GENERIC_OR_ARRAY(0x21, EnumSet.of(HasLocation, HasParameter)),

    CLASS_TYPE_PARAMETER(0x22, EnumSet.of(HasParameter)),
    //@Deprecated CLASS_TYPE_PARAMETER_GENERIC_OR_ARRAY(0x23, EnumSet.of(HasLocation, HasParameter)),

    /** For annotations with an unknown target. */
    UNKNOWN(-1, EnumSet.noneOf(TargetAttribute.class));

    static final int MAXIMUM_TARGET_TYPE_VALUE = 0x22;

    private final int targetTypeValue;
    private Set<TargetAttribute> flags;

    TargetType(int targetTypeValue, Set<TargetAttribute> flags) {
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
        return flags.contains(HasLocation);
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
        return flags.contains(HasParameter);
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is a type parameter bound.
     *
     * @return true if this TargetType represents an type parameter bound
     *         annotation, false otherwise
     */
    public boolean hasBound() {
        return flags.contains(HasBound);
    }

    public int targetTypeValue() {
        return this.targetTypeValue;
    }

    private static TargetType[] targets = null;

    private static TargetType[] buildTargets() {
        TargetType[] targets = new TargetType[MAXIMUM_TARGET_TYPE_VALUE + 1];
        TargetType[] alltargets = values();
        for (TargetType target : alltargets)
            if (target.targetTypeValue >= 0)
                targets[target.targetTypeValue] = target;
        for (int i = 0; i <= MAXIMUM_TARGET_TYPE_VALUE; ++i)
            if (targets[i] == null)
                targets[i] = UNKNOWN;
        return targets;
    }

    public static TargetType fromTargetTypeValue(int tag) {
        if (targets == null)
            targets = buildTargets();

        if (tag == UNKNOWN.targetTypeValue)
            return UNKNOWN;
        // we can optimize the algorithm a bit: binary search?
        if (tag < 0 || tag >= targets.length)
            throw new IllegalArgumentException("Unknown TargetType: " + tag);
        return targets[tag];
    }

    static enum TargetAttribute {
        HasLocation, HasParameter, HasBound;
    }
}
