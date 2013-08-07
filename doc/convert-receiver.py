#!/usr/bin/python
# Change one JSR 308 receiver annotation from old style to new style.
# Moves the annotation from after the parameter list (in C++ location)
# to a new first parameter named "this"."""

import os
import re
import sys

output_dir = "convert_output"

def get_end_index(source, start):
    """Get the index of ';' or '{' starting from start."""
    i = source.find(';', start);
    j = source.find('{', start);
    k = source.find('}', start);
    if i == j and j == k and k == -1:
        return -1
    else:
        m = i < j and i or j
        return (m < k and m or k)

def convert(file_name, in_place):
    """Do the conversion"""

    print "converting", file_name, "..."

    # A stack for keeping class names and curly braces
    stack = []

    # Another stack for keeping the current class name
    current_class = []

    # Regex for class declaration
    class_pattern = re.compile(r".*?(\s+|^)(class|interface)\s+(\w+\s*(<\w+(\s*,\s*\w+)*>)?)")
    method_pattern = re.compile(r".*?(\s+|^)(\w+?\s*\()([^)]*)(\)\s*(((\/\*)?\s*@\w+\s*(\*\/)?)+))")

    # Open file
    file = open(file_name)
    source = file.read()
    file.close()

    length = len(source);
    dest = "";  # The result will be stored in "dest"
    i = 0;
    while i < length:
        end = get_end_index(source, i)
        if end > 0:
            s = source[i:end + 1]    # Include the character ; { }
        else:
            s = source[i:]
        # Match the class declarations
        c = class_pattern.search(s)
        m = method_pattern.search(s)

        if c is not None:
            class_name = c.group(3).strip()
            stack.append(class_name)
            current_class.append(class_name)
            dest = dest + source[i : i + c.end()]
            i = i + c.end()
        elif m is not None:
        # Now we look for the method declaration with annotations
            method_name = m.group(2).strip()
            parameters = m.group(3).strip()
            annotations = m.group(5).strip()
            current = current_class[len(current_class) - 1]
            new_method = method_name + annotations + " " + current + " this"
            if parameters != "":
                new_method = new_method + ", "
            new_method = new_method + parameters + ")"
            dest = dest + source[i : i + m.end(1)]
            dest = dest + new_method
            i = i + m.end(0)
        else:
            dest = dest + s
            # Look for next { or }
            if end > 0:
                if source[end] == '{':
                    stack.append('{')
                elif source[end] == '}':
                    stack.pop()
                    if len(stack) > 0 and stack[len(stack) - 1] != '{':
                        # In this case, we need to pop the class name
                        stack.pop()
                        current_class.pop()
                i = end + 1
            else:
                break

    if not in_place:
        out_file_name = output_dir + os.sep + file_name
        d = os.path.dirname(out_file_name)
        if not os.path.exists(d):
            os.makedirs(d)
        out_file = open(out_file_name, 'w')
        out_file.write(dest)
        out_file.close()


def main():
    if len(sys.argv) < 1:
        print 'Usage:', sys.argv[0], '<fileName> <fileName> ...'
    else:
        for i in range(1, len(sys.argv)):
            convert(sys.argv[i], False)
        print "converted files were written into",output_dir,"directory"

if __name__ == '__main__':
    main()
