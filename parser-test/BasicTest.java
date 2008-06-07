import java.util.*;
import java.io.*;

@interface A {}
@interface B {}
@interface C {}
@interface D {}

/**
 * Tests basic JSR 308 parser functionality. We don't really care about what
 * the parse tree looks like, just that these annotations can be parsed.
 */
class BasicTest<T extends @A Object> extends @B LinkedList<T> implements @C List<T> {

    void test() {
    
        // Handle annotated class literals/cast types
        Class<?> c = @A String.class;
        Object o = (@A Object) "foo";

        // Handle annotated "new" expressions (except arrays; see ArrayTest)
        String s = new @A String("bar");

        boolean b = o instanceof @A Object;

    
        @A Map<@B List<@C String>, @D String> map = 
            new @A HashMap<@B List<@C String>, @D String>();

        Class<? extends @A String> c2 = @A String.class;
    }

    // Handle receiver annotations
    // Handle annotations on a qualified identifier list
    void test2() @C @D throws @A IllegalArgumentException, @B IOException {

    }

    // Handle annotations on a varargs element type
    void test3(Object @A... objs) {

    }
}
