package sun.tools.javap;

import java.io.*;
import java.util.Arrays;

import com.sun.tools.javac.comp.TargetType;

public class ExtendedAnnotationData extends AnnotationData {

    byte target_type;

    short offset; // for typecasts, instanceof, new
    short start_pc, length, index; // for locals
    short bound, parameter;

    short location_length;
    byte[] location;

    public static ExtendedAnnotationData[] readExtendedAnnotations(DataInputStream in) throws IOException {
        short num_annotations = in.readShort();
        ExtendedAnnotationData[] annotations = new ExtendedAnnotationData[num_annotations];
        for (int i = 0; i < num_annotations; i++)
            annotations[i] = new ExtendedAnnotationData(in);
        return annotations;
    }

    protected ExtendedAnnotationData(DataInputStream in) throws IOException {

        super(in);


        final TargetType[] targetTypes = TargetType.values();

        target_type = in.readByte();
        assert target_type >= 0 && target_type < targetTypes.length;

        TargetType type = targetTypes[target_type];

        switch (type) {
        case TYPECAST:
        case INSTANCEOF:
        case NEW:
        case TYPECAST_GENERIC_OR_ARRAY:
        case INSTANCEOF_GENERIC_OR_ARRAY:
        case NEW_GENERIC_OR_ARRAY:
            offset = in.readShort();
            break;

        case METHOD_PARAMETER:
        case METHOD_PARAMETER_GENERIC_OR_ARRAY:
            break;

        case LOCAL_VARIABLE:
        case LOCAL_VARIABLE_GENERIC_OR_ARRAY:
            start_pc = in.readShort();
            length = in.readShort();
            index = in.readShort();
            break;
        }

        if (type.hasParameter())
            parameter = in.readByte();
        if (type.hasBound())
            bound = in.readByte();

        if (type.hasLocation()) {
            location_length = in.readShort();
            location = new byte[location_length];
            for (int i = 0; i < location_length; i++)
                location[i] = in.readByte();
        }
    }
}
