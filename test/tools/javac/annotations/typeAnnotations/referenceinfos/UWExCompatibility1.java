/*
 * Test of UW local extension: type annotations in comments with Java 6.
 */

/*
 * @test
 * @bug 1111111
 * @summary Java 6/7: type annotations allowed with annotations in comments
 * @compile -g Driver.java ReferenceInfoUtil.java
 * @compile -source 6 -target 6 UWExCompatibility1.java
 * @compile -source 7 -target 7 UWExCompatibility1.java
 * @run main Driver UWExCompatibility1
 * @author Werner Dietl
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import static com.sun.tools.classfile.TypeAnnotation.TargetType.*;

import java.util.List;

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface TA {}

public class UWExCompatibility1 {
    @TADescription(annotation = "TA", type = FIELD, genericLocation = { 3, 0 })
    public String typeAnnoInComment() {
        return "List</*@TA*/ Object> f;";
    }

    @TADescriptions({
        @TADescription(annotation = "TA", type = METHOD_TYPE_PARAMETER_BOUND, paramIndex=0, boundIndex=0),
        @TADescription(annotation = "TB", type = METHOD_RETURN),
        @TADescription(annotation = "TC", type = METHOD_FORMAL_PARAMETER, paramIndex=0)
    })
    public String typeAnnoInComment2() {
        return "<T extends /*@TA*/ Object> /*@TB*/ T foo(/*@TC*/ T p) { return null; }";
    }

    @TADescription(annotation = "TA", type = METHOD_RETURN)
    public String typeAnnoInComment3() {
        return "<T> /*@TA*/ T foobar() { return null; }";
    }

    @TADescription(annotation = "TA", type = FIELD, genericLocation = { 3, 0 })
    public String typeAnnoInVoodooComment() {
        return "List</*>>> @TA */ Object> g;";
    }
}
