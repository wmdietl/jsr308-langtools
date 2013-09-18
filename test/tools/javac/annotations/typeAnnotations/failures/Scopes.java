/*
 * @test /nodynamiccopyright/
 * @bug 6843077 8006775
 * @summary check that A is accessible in the class type parameters
 * @author Mahmood Ali
 * @compile/fail/ref=Scopes.out -XDrawDiagnostics Scopes.java
 */
class Scopes<T extends @UniqueInner Object> {
  // UniqueInner is not visible in the type parameters.
  // One has to use Scopes.UniqueInner.
  @interface UniqueInner { };
}
