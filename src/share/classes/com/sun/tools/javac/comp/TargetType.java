package com.sun.tools.javac.comp;


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
    TYPECAST,

    /** For annotations on a type argument or nested array of a typecast. */
    TYPECAST_GENERIC_OR_ARRAY,

    /** For annotations on type tests. */
    INSTANCEOF,

    /** For annotations on a type argument or nested array of a type test. */
    INSTANCEOF_GENERIC_OR_ARRAY,

    /** For annotations on object creation expressions. */
    NEW,

    /**
     * For annotations on a type argument or nested array of an object creation
     * expression.
     */
    NEW_GENERIC_OR_ARRAY,


    /** For annotations on the method receiver. */
    METHOD_RECEIVER,

    /**
     * For annotations on a type argument or nested array of the method
     * receiver.
     *
     * Deprecated because such annotations are not allowed (yet), but included
     * so that the numbering works out.
     */
    @Deprecated METHOD_RECEIVER_GENERIC_OR_ARRAY,

    /** For annotations on local variables. */
    LOCAL_VARIABLE,

    /** For annotations on a type argument or nested array of a local. */
    LOCAL_VARIABLE_GENERIC_OR_ARRAY,

    /**
     * For annotations on a method return type.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    @Deprecated METHOD_RETURN,

    /**
     * For annotations on a type argument or nested array of a method return
     * type.
     */
    METHOD_RETURN_GENERIC_OR_ARRAY,

    /**
     * For annotations on a method parameter.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    @Deprecated METHOD_PARAMETER,

    /** For annotations on a type argument or nested array of a method parameter. */
    METHOD_PARAMETER_GENERIC_OR_ARRAY,

    /**
     * For annotations on a field.
     *
     * Deprecated because such annotations are ordinary (not extended), but
     * included so that the numbering works out.
     */
    @Deprecated FIELD,

    /** For annotations on a type argument or nested array of a field. */
    FIELD_GENERIC_OR_ARRAY,

    /** For annotations on a bound of a type parameter of a class. */
    CLASS_TYPE_PARAMETER_BOUND,

    /**
     * For annotations on a type argument or nested array of a bound of a type
     * parameter of a class.
     */
    CLASS_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY,

    /** For annotations on a bound of a type parameter of a method. */
    METHOD_TYPE_PARAMETER_BOUND,

    /**
     * For annotations on a type argument or nested array of a bound of a type
     * parameter of a method.
     */
    METHOD_TYPE_PARAMETER_BOUND_GENERIC_OR_ARRAY,

    /** For annotations on the type of an "extends" or "implements" clause. */
    CLASS_EXTENDS,

    /** For annotations on the inner type of an "extends" or "implements" clause. */
    CLASS_EXTENDS_GENERIC_OR_ARRAY,

    /** For annotations on a throws clause in a method declaration. */
    THROWS,
    @Deprecated THROWS_GENERIC_OR_ARRAY,

    /** For annotations in type arguments of object creation expressions. */
    NEW_TYPE_ARGUMENT,
    NEW_TYPE_ARGUMENT_GENERIC_OR_ARRAY,

    METHOD_TYPE_ARGUMENT,
    METHOD_TYPE_ARGUMENT_GENERIC_OR_ARRAY,

    WILDCARD_BOUND,
    WILDCARD_BOUND_GENERIC_OR_ARRAY,

    CLASS_LITERAL,
    @Deprecated CLASS_LITERAL_GENERIC_OR_ARRAY,

    METHOD_TYPE_PARAMETER,
    @Deprecated METHOD_TYPE_PARAMETER_GENERIC_OR_ARRAY,

    CLASS_TYPE_PARAMETER,
    @Deprecated CLASS_TYPE_PARAMETER_GENERIC_OR_ARRAY,

    /** For annotations with an unknown target. */
    UNKNOWN;

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is an inner type of a generic or array type.
     *
     * @return true if this TargetType represents an annotation on an inner
     *         type, false otherwise
     */
    @Deprecated // misleading name, because it might represent an array
    public boolean isGeneric() {
        return ordinal() % 2 == 1;
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is an inner type of a generic or array type.
     *
     * @return true if this TargetType represents an annotation on an inner
     *         type, false otherwise
     */
    public boolean hasLocation() {
        return this.name().contains("GENERIC_OR_ARRAY");
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target has a parameter index.
     *
     * @return true if this TargetType has a parameter index,
     *         false otherwise
     */
    public boolean hasParameter() {
        return this.name().contains("TYPE_PARAMETER");
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is a type parameter bound.
     *
     * @return true if this TargetType represents an type parameter bound
     *         annotation, false otherwise
     */
    public boolean hasBound() {
        return this.name().contains("BOUND");
    }

    /**
     * Returns whether or not this TargetType represents an annotation whose
     * target is some part of a method.
     *
     * @return true if this {@code {@link TargetType}} represents some part of
     *         a method
     */
    public boolean hasMethod() {
        return this.name().contains("METHOD");
    }
}
