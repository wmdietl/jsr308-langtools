/*
 * @test /nodynamiccopyright/
 * @bug 1234567
 * @summary Import clauses cannot use annotations.
 * @author Werner Dietl
 * @compile/fail/ref=AnnotatedImport.out -XDrawDiagnostics AnnotatedImport.java
 */

import java.@A util.List;
import @A java.util.Map;
import java.util.@A HashMap;

class AnnotatedImport { }

@interface A { }
