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

        // For locals.
        public int length = -1;
        public int index = -1;

        public int bound = -1;
        public int parameter = -1;

        @Override
        public String toString() {
            return "[" + this.type +
                    (location.isEmpty() ? "" : " (" + this.location + ")")
                    + " @" + this.pos + "]";
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
