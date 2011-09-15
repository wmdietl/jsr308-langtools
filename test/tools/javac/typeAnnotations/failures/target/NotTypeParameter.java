/*
 * @test /nodynamiccopyright/
 * @bug 6843077
 * @summary test invalid location of TypeUse
 * @author Mahmood Ali
 * @compile/fail/ref=NotTypeParameter.out -XDrawDiagnostics -source 1.8 NotTypeParameter.java
 */

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

class VoidMethod<@A K> {
  @A void test() { }
}

@Target(ElementType.TYPE_USE)
@interface A { }
