package sun.tools.javap;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class AnnotationData {

    public static boolean isAnyAnnotationsAttribute(String name) {
        return (isAnnotationsAttribute(name) ||
                isParameterAnnotationsAttribute(name) ||
                isTypeAnnotationsAttribute(name));
    }

    public static boolean isAnnotationsAttribute(String name) {
        return (name.equals("RuntimeVisibleAnnotations") ||
                name.equals("RuntimeInvisibleAnnotations"));
    }

    public static boolean isParameterAnnotationsAttribute(String name) {
        return (name.equals("RuntimeVisibleParameterAnnotations") ||
                name.equals("RuntimeInvisibleParameterAnnotations"));
    }

    public static boolean isTypeAnnotationsAttribute(String name) {
        return (name.equals("RuntimeVisibleTypeAnnotations") ||
                name.equals("RuntimeInvisibleTypeAnnotations"));
    }

    public static AnnotationData[][] readParameterAnnotations(DataInputStream in) throws IOException {

        byte num_parameters = in.readByte();

        AnnotationData[][] paramAnnotations = new AnnotationData[num_parameters][];

        for (int i = 0; i < num_parameters; i++)
            paramAnnotations[i] = readAnnotations(in);

        return paramAnnotations;
    }

    public static AnnotationData[] readAnnotations(DataInputStream in) throws IOException {

        short num_annotations = in.readShort();
        AnnotationData[] annotations = new AnnotationData[num_annotations];
        for (int i = 0; i < num_annotations; i++)
            annotations[i] = new AnnotationData(in);
        return annotations;
    }

    private AnnotationData() {}


    short type_index;
    short num_element_value_pairs;
    ElementValuePair[] element_value_pairs;

    protected AnnotationData(DataInputStream in) throws IOException {
        type_index = in.readShort();
        num_element_value_pairs = in.readShort();
        element_value_pairs = new ElementValuePair[num_element_value_pairs];
        for (int i = 0; i < num_element_value_pairs; i++)
            element_value_pairs[i] = new ElementValuePair(in);
    }

    public static class ElementValuePair {
        short element_name_index;
        ElementValue element_value;

        public ElementValuePair(DataInputStream in) throws IOException {
            element_name_index = in.readShort();
            element_value = new ElementValue(in);
        }
    }

    public static class ElementValue {
        byte tag;
        short const_value_index;

        short type_name_index;
        short const_name_index;

        short class_info_index;

        AnnotationData annotation_value;

        short num_values;
        ElementValue[] values;

        public ElementValue(DataInputStream in) throws IOException {
            tag = in.readByte();
            switch (tag) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
            case 's':
                const_value_index = in.readShort();
                break;

            case 'e':
                type_name_index = in.readShort();
                const_name_index = in.readShort();
                break;

            case 'c':
                class_info_index = in.readShort();
                break;

            case '@':
                annotation_value = new AnnotationData(in);
                break;

            case '[':
                num_values = in.readShort();
                values = new ElementValue[num_values];
                for (int i = 0; i < num_values; i++)
                    values[i] = new ElementValue(in);
            }
        }
    }
}
