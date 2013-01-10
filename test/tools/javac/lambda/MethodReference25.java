/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8003280
 * @summary Add lambda tests
 *  check that non-boxing method references conversion has the precedence
 * @run main MethodReference25
 */

public class MethodReference25 {

    static void assertTrue(boolean cond) {
        assertionCount++;
        if (!cond)
            throw new AssertionError();
    }

    static int assertionCount = 0;

    static void m(Integer i) { assertTrue(true); }

    interface SAM1 {
        void m(int x);
    }

    interface SAM2 {
        void m(Integer x);
    }

    static void call(int i, SAM1 s) { s.m(i); assertTrue(false); }
    static void call(int i, SAM2 s) { s.m(i);  }

    public static void main(String[] args) {
        call(1, MethodReference25::m); //resolves to call(int, SAM2)
        assertTrue(assertionCount == 1);
    }
}