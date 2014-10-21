/*
 * Test of UW local extension: type annotations in comments with Java 6.
 */

/*
 * @test /nodynamiccopyright/
 * @bug 1111111
 * @summary Java 6: type annotations not allowed outside comments
 * @compile/fail/ref=UWExCompatibility2.out -XDrawDiagnostics -source 6 -target 6 UWExCompatibility2.java
 * @author Werner Dietl
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import java.util.List;

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@interface TA {}

public class UWExCompatibility2 {
    List<@TA Object> f;
}
