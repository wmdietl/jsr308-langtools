/*
 * Copyright (c) 2008, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 6843077
 * @summary new type annotation location: nested types
 * @author Werner Dietl
 * @compile -source 1.8 NestedTypes.java
 */
class Outer {
    class Inner {
        class Inner2 {
            // m1a-c all have the same parameter type.
            // How can I ensure this?
            void m1a(@A Inner2 p) {}
            void m1b(Inner.@A Inner2 p) {}
            void m1c(Outer.Inner.@A Inner2 p) {}

            // m2a-b both have the same parameter type.
            void m2a(@A Inner.Inner2 p) {}
            void m2b(Outer.@A Inner.Inner2 p) {}
        }
    }
    Inner i;
    class GInner<X> {
        class GInner2<Y> {}
    }

    static class Static {}
    static class GStatic<X, Y> {}
}

class Test1 {
    MyList<@A Outer . @B Inner. @C Inner2> f;
    @A Outer f1;
    @A Outer . @B Inner f2 = f1.new @B Inner();
    @A Outer . @B GInner<@C Object> f3 = f1.new @B GInner<@C Object>();

    MyList<@A Outer . @B GInner<@C Object>. @D GInner2<@E Integer>> f4;
    // MyList<Outer.GInner<Object>.GInner2<Integer>> f4clean;

    MyList<@A Outer . @B Static> f5;
    @A Outer . Static f6;
    @Av("A") Outer . @Bv("B") GStatic<@Cv("C") String, @Dv("D") Object> f7;
    @A Outer . @Cv("Data") Static f8;
    MyList<@A Outer . @Cv("Data") Static> f9;
}

class Test2 {
    void m() {
        @A Outer f1 = null;
        @A Outer.@B Inner f2 = null;
        @A Outer.@B Static f3 = null;
        @A Outer.@C Inner f4 = null;

        @A Outer . @B Static f5 = null;
        @A Outer . @Cv("Data") Static f6 = null;
        MyList<@A Outer . @Cv("Data") Static> f7 = null;
    }
}

class Test3 {
    void monster(@A Outer p1,
        @A Outer.@B Inner p2,
        @A Outer.@B Static p3,
        @A Outer.@Cv("Test") Inner p4,
        @A Outer . @B Static p5,
        @A Outer . @Cv("Data") Static p6,
        MyList<@A Outer . @Cv("Data") Static> p7) {
    }
}

class Test4 {
    void m() {
        @A Outer p1 = new @A Outer();
        @A Outer.@B Inner p2 = p1.new @B Inner();
        @A Outer.@B Static p3 = new @A Outer.@B Static();
        @A Outer.@Cv("Test") Inner p4 = p1.new @Cv("Test") Inner();
        @A Outer . @B Static p5 = new @A Outer . @B Static();
        @A Outer . @Cv("Data") Static p6 = new @A Outer . @Cv("Data") Static();
        MyList<@A Outer . @Cv("Data") Static> p7 = new MyList<@A Outer . @Cv("Data") Static>();
    }
}

class MyList<K> { }


@interface A { }
@interface B { }
@interface C { }
@interface D { }
@interface E { }
@interface F { }

@interface Av { String value(); }
@interface Bv { String value(); }
@interface Cv { String value(); }
@interface Dv { String value(); }
@interface Ev { String value(); }
@interface Fv { String value(); }

