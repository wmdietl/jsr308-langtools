/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/*
 * @test
 * @summary new type annotation location: wildcard bound
 * @author Mahmood Ali
 * @compile Wildcards.java
 */
class Test {
  void wcExtends(List<? extends @A String> l) { }
  void wcSuper(List<? super @A String> l) { }

  List<? extends @A String> returnWcExtends() { return null; }
  List<? super @A String> returnWcSuper() { return null; }
  List<? extends @A List<? super @B("m") String>> complex() { return null; }
}

class WithValue {
  void wcExtends(List<? extends @B("m") String> l) { }
  void wcSuper(List<? super @B(value="m") String> l) { }

  List<? extends @B("m") String> returnWcExtends() { return null; }
  List<? super @B(value="m") String> returnWcSuper() { return null; }
  List<? extends @B("m") List<? super @B("m") String>> complex() { return null; }
}

class List<K> { }

@interface A { }
@interface B { String value(); }
