import java.util.*;

@interface A {}
@interface B {}
@interface C {}
@interface D {}

class ArrayTest {
    
    void test() {
    
        @A String[@B][@C][@D] local;
        List<@A String[@B][@C][@D]>[@B][@C][@D] lst;

        Object o1 = new @A byte[@B][@C][@D] { null };
        Object o2 = new @A String[@B][@C][@D] { null };
        Object o3 = new @A byte[@B 1][@C 2][@D 3];
        Object o4 = new @A String[@B 1][@C 2][@D 3];
        Object o5 = new @A byte[@B 1][@C][@D];
        Object o6 = new @A String[@B 1][@C][@D];

        Class<?> c = @A int[@B][@C].class;
    }

    void testVarArgs(@A Object @B... objs) {

    }
    
}
