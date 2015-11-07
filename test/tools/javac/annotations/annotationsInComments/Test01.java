/*
 * @test
 * @bug 1234567
 * @summary Annotations in comments are recognized
 * @author Werner Dietl
 * @compile/fail/ref=Test01.out -XDrawDiagnostics Test01.java
 */
class Test01 {
    // Recognized comment
    /*@AnnoString("Hi There!")*/
    void t01() {}
    // Recognized comment, bad symbol
    /*@AnnoStringBad("Hi There!")*/
    void t02() {}

    // Recognized comment
    /*@AnnoString("HiThere(((")*/
    void t03() {}
    // Recognized comment, bad symbol
    /*@AnnoStringBad("HiThere(((")*/
    void t04() {}
    // Recognized comment, bad type
    /*@AnnoInt("HiThere(((")*/
    void t05() {}

    // Recognized comment, bad type
    /*@AnnoStringBad("HiThere) ")*/
    void t06() {}

    @interface AnnoString {
	String value();
    }

    @interface AnnoInt {
	int value();
    }
}
