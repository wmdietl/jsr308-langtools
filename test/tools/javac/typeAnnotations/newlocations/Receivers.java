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
 * @summary new type annotation location: receivers
 * @author Mahmood Ali, Werner Dietl
 * @compile -source 1.8 Receivers.java
 */
class DefaultUnmodified {
  void plain(@A DefaultUnmodified this) { }
  <T> void generic(@A DefaultUnmodified this) { }
  void withException(@A DefaultUnmodified this) throws Exception { }
  String nonVoid(@A DefaultUnmodified this) { return null; }
  <T extends Runnable> void accept(@A DefaultUnmodified this, T r) throws Exception { }
}

class PublicModified {
  public final void plain(@A PublicModified this) { }
  public final <T> void generic(@A PublicModified this) { }
  public final void withException(@A PublicModified this) throws Exception { }
  public final String nonVoid(@A PublicModified this) { return null; }
  public final <T extends Runnable> void accept(@A PublicModified this, T r) throws Exception { }
}

class WithValue {
  void plain(@B("m") WithValue this) { }
  <T> void generic(@B("m") WithValue this) { }
  void withException(@B("m") WithValue this) throws Exception { }
  String nonVoid(@B("m") WithValue this) { return null; }
  <T extends Runnable> void accept(@B("m") WithValue this, T r) throws Exception { }
}

class WithFinal {
  void plain(final @B("m") WithFinal this) { }
  <T> void generic(final @B("m") WithFinal this) { }
  void withException(final @B("m") WithFinal this) throws Exception { }
  String nonVoid(final @B("m") WithFinal this) { return null; }
  <T extends Runnable> void accept(final @B("m") WithFinal this, T r) throws Exception { }
}

class WithBody {
  Object f;

  void plain(@A WithBody this) {
    this.f = null;
  }
}

class Outer {
  class Inner {
    void none(Outer.Inner this) {}
    void outer(@A Outer.Inner this) {}
    void inner(Outer. @B Inner this) {}
    void both(@A Outer.@B Inner this) {}

    void innerOnlyNone(Inner this) {}
    void innerOnly(@A Inner this) {}
  }
}

class GenericOuter<A, B> {
  class GenericInner<C, D> {
    void none(GenericOuter<A, B>.GenericInner<C, D> this) {}
    void outer(@A GenericOuter<A, B>.GenericInner<C, D> this) {}
    void inner(GenericOuter<A, B>. @B GenericInner<C, D> this) {}
    void both(@A GenericOuter<A, B>.@B GenericInner<C, D> this) {}

    void innerOnlyNone(GenericInner<C, D> this) {}
    void innerOnly(@A GenericInner<C, D> this) {}
  }
}


@interface A {}
@interface B { String value(); }
