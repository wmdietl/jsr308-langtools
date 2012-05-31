/*
 * @test /nodynamiccopyright/
 * @bug 6843077
 * @summary test invalid location of TypeUse
 * @author Mahmood Ali
 * @compile/fail/ref=Constructor.out -XDrawDiagnostics -source 1.8 Constructor.java
 */

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

class Constructor {
  // Declaration annotation, not type use annotation
  @A Constructor() { }

  // Declaration annotation, not type parameter annotation
  @B Constructor(int x) { }

  // Allowed!
  Constructor(@A Constructor this, Object o) { }

  // Forbidden location for type parameter annotation
  Constructor(@B Constructor this, Object o1, Object o2) { }
}

class Constructor2 {
  class Inner {
    // OK
    Inner(@A Inner this) { }
  }
}

@Target(ElementType.TYPE_USE)
@interface A { }

@Target(ElementType.TYPE_PARAMETER)
@interface B { }

