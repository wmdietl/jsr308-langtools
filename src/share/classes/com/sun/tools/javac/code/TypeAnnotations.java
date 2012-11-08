/*
 * Copyright (c) 2009, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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

package com.sun.tools.javac.code;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeKind;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeScanner;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

/**
 * Contains operations specific to processing type annotations.
 */
public class TypeAnnotations {
    // Class cannot be instantiated.
    private TypeAnnotations() {}

    public static void organizeTypeAnnotations(Symtab syms, Names names, JCTree tree) {
        new AnnotationsKindSeparator(syms, names).scan(tree);
    }

    public static void completeTypeAnnotationsInSignatures(Names names, JCCompilationUnit tree) {
        new TypeAnnotationPositions(names) {
            public void visitVarDef(JCVariableDecl tree) {
                push(tree);
                scan(tree.mods);
                scan(tree.vartype);
                // scan(tree.init);
                pop();
            }

            public void visitMethodDef(JCMethodDecl tree) {
                push(tree);
                scan(tree.mods);
                scan(tree.restype);
                scan(tree.typarams);
                scan(tree.recvparam);
                scan(tree.params);
                scan(tree.thrown);
                scan(tree.defaultValue);
                // scan(tree.body);
                pop();
            }

            public void visitBlock(JCBlock tree) {
                // Don't do anything for top-level blocks,
                // e.g. static and instance initializers
            }
        }.scan(tree);
    }

    public static void completeTypeAnnotations(Names names, JCClassDecl tree) {
        new TypeAnnotationPositions(names).scan(tree);
    }

    /**
     * Separates type annotations from declaration annotations.
     * TODO: explain more and rename. Also adds annotations
     * to symbols.
     */
    private static class AnnotationsKindSeparator extends TreeScanner {

        private enum AnnotationType { DECLARATION, TYPE, BOTH };

        private final Symtab syms;
        private final Names names;

        AnnotationsKindSeparator(Symtab syms, Names names) {
            this.syms = syms;
            this.names = names;
        }

        @Override
        public void visitClassDef(JCClassDecl tree) {
            // This visitor is only meant for variable declarations
            // and methods. If we encounter a nested class, stop.
            // The nested class will be visited separately later.
        }

        @Override
        public void visitMethodDef(final JCMethodDecl tree) {
            // clear all annotations
            {
                TypeAnnotationPosition pos = new TypeAnnotationPosition();
                pos.type = TargetType.METHOD_RETURN;
                if (tree.sym.isConstructor()) {
                    pos.pos = tree.pos;
                    // Use null to mark that the annotations go with the symbol.
                    separateAnnotationsKinds(tree, null, tree.sym, pos);
                } else {
                    pos.pos = tree.restype.pos;
                    separateAnnotationsKinds(tree.restype, tree.sym.type.getReturnType(),
                            tree.sym, pos);
                }
            }
            if (tree.recvparam != null && tree.recvparam.sym != null) {
                // TODO: make sure there are no declaration annotations.
                TypeAnnotationPosition pos = new TypeAnnotationPosition();
                pos.type = TargetType.METHOD_RECEIVER;
                pos.pos = tree.recvparam.vartype.pos;
                separateAnnotationsKinds(tree.recvparam.vartype, tree.recvparam.sym.type,
                        tree.recvparam.sym, pos);
            }
            int i = 0;
            for (JCVariableDecl param : tree.params) {
                TypeAnnotationPosition pos = new TypeAnnotationPosition();
                pos.type = TargetType.METHOD_PARAMETER;
                pos.parameter_index = i;
                pos.pos = param.vartype.pos;
                separateAnnotationsKinds(param.vartype, param.sym.type, param.sym, pos);
                ++i;
            }

            super.visitMethodDef(tree);
        }

        @Override
        public void visitVarDef(final JCVariableDecl tree) {
            if (tree.sym.getKind() == ElementKind.FIELD) {
                TypeAnnotationPosition pos = new TypeAnnotationPosition();
                pos.type = TargetType.FIELD;
                pos.pos = tree.pos;
                separateAnnotationsKinds(tree.vartype, tree.sym.type, tree.sym, pos);
            } else if (tree.sym.getKind() == ElementKind.LOCAL_VARIABLE) {
                TypeAnnotationPosition pos = new TypeAnnotationPosition();
                pos.type = TargetType.LOCAL_VARIABLE;
                pos.pos = tree.pos;
                separateAnnotationsKinds(tree.vartype, tree.sym.type, tree.sym, pos);
            } else if (tree.sym.getKind() == ElementKind.EXCEPTION_PARAMETER) {
                // System.out.println("Found exception param: " + tree);
                TypeAnnotationPosition pos = new TypeAnnotationPosition();
                pos.type = TargetType.EXCEPTION_PARAMETER;
                pos.pos = tree.pos;
                separateAnnotationsKinds(tree.vartype, tree.sym.type, tree.sym, pos);
            } else {
                // There is nothing else in a variable declaration that needs separation.
                // System.out.println("Have a: " + )
            }
            super.visitVarDef(tree);
        }

        private void separateAnnotationsKinds(JCTree typetree, Type type, Symbol sym, TypeAnnotationPosition pos) {
            /*
            System.out.printf("separateAnnotationsKinds(typetree: %s, type: %s, symbol: %s, pos: %s%n",
                    typetree, type, sym, pos);
            */
            List<Attribute.Compound> annotations = sym.getAnnotationMirrors();
            ListBuffer<Attribute.Compound> declAnnos = new ListBuffer<Attribute.Compound>();
            ListBuffer<Attribute.TypeCompound> typeAnnos = new ListBuffer<Attribute.TypeCompound>();
            // Also include existing type annotations.
            typeAnnos.appendList(sym.getTypeAnnotationMirrors());

            for (Attribute.Compound a : annotations) {
                switch (annotationType(a, sym)) {
                case DECLARATION:
                    declAnnos.append(a);
                    break;
                case BOTH: {
                    declAnnos.append(a);
                    Attribute.TypeCompound ta = toTypeCompound(a, pos);
                    typeAnnos.append(ta);
                    break;
                }
                case TYPE: {
                    Attribute.TypeCompound ta = toTypeCompound(a, pos);
                    typeAnnos.append(ta);
                    break;
                }
                }
            }

            sym.annotations.reset();
            sym.annotations.setDeclarationAttributes(declAnnos.toList());

            List<Attribute.TypeCompound> typeAnnotations = typeAnnos.toList();

            if (type == null) {
                // When type is null, put the type annotations to the symbol.
                // This is used for constructor return annotations, for which
                // no appropriate type exists.
                sym.annotations.setTypeAttributes(typeAnnotations);
                return;
            }

            // type is non-null and annotations are added to that type
            Type atype = typeWithAnnotations(typetree, type, typeAnnotations);

            if (sym.getKind() == ElementKind.METHOD) {
                sym.type.asMethodType().restype = atype;
            } else {
                sym.type = atype;
            }

            sym.annotations.appendUniqueTypes(typeAnnotations);
            if (sym.getKind() == ElementKind.PARAMETER &&
                    sym.getQualifiedName().equals(names._this)) {
                sym.owner.type.asMethodType().recvtype = atype;
                // note that the typeAnnotations will also be added to the owner below.
            }
            if (sym.getKind() == ElementKind.PARAMETER ||
                    sym.getKind() == ElementKind.LOCAL_VARIABLE ||
                    sym.getKind() == ElementKind.EXCEPTION_PARAMETER) {
                sym.owner.annotations.appendUniqueTypes(typeAnnotations);
            }
        }

        // I think this has a similar purpose as
        // {@link com.sun.tools.javac.parser.JavacParser.insertAnnotationsToMostInner(JCExpression, List<JCTypeAnnotation>, boolean)}
        private static Type typeWithAnnotations(JCTree typetree, Type type,
                List<Attribute.TypeCompound> annotations) {
            // System.out.printf("typeWithAnnotations(typetree: %s, type: %s, annotations: %s)%n",
            //         typetree, type, annotations);
            if (!type.hasTag(TypeTag.ARRAY)) {
                Type enclTy = type;
                Element enclEl = type.asElement();
                JCTree enclTr = typetree;

                // The genericLocation for the annotation.
                // Start at -1 to adjust for the numbers of iterations below.
                int index = -1;
                {
                    List<TypeSymbol> typeparams = type.asElement().getTypeParameters();
                    if (typeparams.nonEmpty()) {
                        // The "top-level" generics are an offset for the index.
                        index += typeparams.size();
                    }
                }
                // Whether we've seen an appropriate member select and therefore
                // whether to make the annotation generic or not.
                boolean seenselect = false;
                while (enclEl != null &&
                        enclEl.getKind() != ElementKind.PACKAGE &&
                        enclTy != null &&
                        enclTy.getKind() != TypeKind.NONE &&
                        (enclTr.getKind() == JCTree.Kind.MEMBER_SELECT ||
                         enclTr.getKind() == JCTree.Kind.PARAMETERIZED_TYPE ||
                         enclTr.getKind() == JCTree.Kind.ANNOTATED_TYPE)) {
                    // Iterate also over the type tree, not just the type: the type is already
                    // completely resolved and we cannot distinguish where the annotation
                    // belongs for a nested type.
                    if (enclTr.getKind() == JCTree.Kind.MEMBER_SELECT) {
                        // only change encl in this case.
                        enclTy = enclTy.getEnclosingType();
                        enclEl = enclEl.getEnclosingElement();
                        enclTr = ((JCFieldAccess)enclTr).getExpression();
                        // Only count going through an outer class select, don't
                        // also count parameterized, packages, or annotated types on the way.
                        if (enclEl.getKind() != ElementKind.PACKAGE) {
                            ++index;
                            seenselect = true;
                        }
                    } else if (enclTr.getKind() == JCTree.Kind.PARAMETERIZED_TYPE) {
                        enclTr = ((JCTypeApply)enclTr).getType();
                    } else {
                        // only other option because of while condition
                        enclTr = ((JCAnnotatedType)enclTr).getUnderlyingType();
                    }
                }

                if (seenselect) {
                    // Only need to change the annotation positions
                    // if they are on an enclosed type.
                    for (Attribute.TypeCompound a : annotations) {
                        TypeAnnotationPosition p = a.position;
                        p.location = p.location.append(index);
                        p.type = p.type.getGenericComplement();
                    }
                }

                // TODO: method receiver type annotations don't work. There is a strange
                // interaction with arrays.
                enclTy.typeAnnotations = annotations;
                return type;
            } else {
                Type.ArrayType arType = (Type.ArrayType) type;
                JCArrayTypeTree arTree = arrayTypeTree(typetree);

                int depth = 0;
                while (arType.elemtype.hasTag(TypeTag.ARRAY)) {
                    arType = (Type.ArrayType) arType.elemtype;
                    arTree = arrayTypeTree(arTree.elemtype);
                    depth++;
                }
                arType.elemtype = typeWithAnnotations(arTree.elemtype, arType.elemtype, annotations);
                for (Attribute.TypeCompound a : annotations) {
                    TypeAnnotationPosition p = a.position;
                    p.location = p.location.prepend(depth);
                    p.type = p.type.getGenericComplement();
                }
            }

            return type;
        }
        // where
        private static JCArrayTypeTree arrayTypeTree(JCTree typetree) {
            if (typetree.getKind() == JCTree.Kind.ARRAY_TYPE) {
                return (JCArrayTypeTree) typetree;
            } else if (typetree.getKind() == JCTree.Kind.ANNOTATED_TYPE) {
                return (JCArrayTypeTree) ((JCAnnotatedType)typetree).underlyingType;
            } else {
                throw new AssertionError("Could not determine array type from type tree: " + typetree);
            }
        }

        private static Attribute.TypeCompound toTypeCompound(Attribute.Compound a, TypeAnnotationPosition p) {
            // It is safe to alias the position.
            return new Attribute.TypeCompound(a, p);
        }

        private AnnotationType annotationType(Attribute.Compound a, Symbol s) {
            Attribute.Compound atTarget =
                a.type.tsym.attribute(syms.annotationTargetType.tsym);
            if (atTarget == null) {
                return inferTargetMetaInfo(a, s);
            }
            Attribute atValue = atTarget.member(names.value);
            if (!(atValue instanceof Attribute.Array)) {
                System.out.printf("Bad @Target argument %s (%s)%n", atValue, atValue.getClass());
                return AnnotationType.DECLARATION; // error recovery
            }
            Attribute.Array arr = (Attribute.Array) atValue;
            boolean isDecl = false, isType = false;
            for (Attribute app : arr.values) {
                if (!(app instanceof Attribute.Enum)) {
                    System.out.printf("annotationType(): unrecognized app=%s (%s)%n", app, app.getClass());
                    isDecl = true;
                    continue;
                }
                Attribute.Enum e = (Attribute.Enum) app;
                if (e.value.name == names.TYPE) {
                    if (s.kind == Kinds.TYP)
                        isDecl = true;
                } else if (e.value.name == names.FIELD) {
                    if (s.kind == Kinds.VAR &&
                            s.owner.kind != Kinds.MTH)
                        isDecl = true;
                } else if (e.value.name == names.METHOD) {
                    if (s.kind == Kinds.MTH &&
                            !s.isConstructor())
                        isDecl = true;
                } else if (e.value.name == names.PARAMETER) {
                    if (s.kind == Kinds.VAR &&
                            s.owner.kind == Kinds.MTH &&
                            (s.flags() & Flags.PARAMETER) != 0)
                        isDecl = true;
                } else if (e.value.name == names.CONSTRUCTOR) {
                    if (s.kind == Kinds.MTH &&
                            s.isConstructor())
                        isDecl = true;
                } else if (e.value.name == names.LOCAL_VARIABLE) {
                    if (s.kind == Kinds.VAR &&
                            s.owner.kind == Kinds.MTH &&
                            (s.flags() & Flags.PARAMETER) == 0)
                        isDecl = true;
                } else if (e.value.name == names.ANNOTATION_TYPE) {
                    if (s.kind == Kinds.TYP &&
                            (s.flags() & Flags.ANNOTATION) != 0)
                        isDecl = true;
                } else if (e.value.name == names.PACKAGE) {
                    if (s.kind == Kinds.PCK)
                        isDecl = true;
                } else if (e.value.name == names.TYPE_USE) {
                    if (s.kind == Kinds.TYP ||
                            s.kind == Kinds.VAR ||
                            (s.kind == Kinds.MTH && !s.isConstructor() &&
                            !s.type.getReturnType().hasTag(TypeTag.VOID)) ||
                            (s.kind == Kinds.MTH && s.isConstructor()))
                        isType = true;
                } else if (e.value.name == names.TYPE_PARAMETER) {
                    /* Irrelevant in this case */
                    // TYPE_PARAMETER doesn't aid in distinguishing between
                    // Type annotations and declaration annotations on an
                    // Element
                } else {
                    System.out.printf("annotationType(): unrecognized e.value.name=%s (%s)%n", e.value.name, e.value.name.getClass());
                    isDecl = true;
                }
            }
            if (isDecl && isType)
                return AnnotationType.BOTH;
            else
                return isType ? AnnotationType.TYPE : AnnotationType.DECLARATION;
        }

        /** Infer the target annotation kind, if none is give.
         * We only infer declaration annotations.
         */
        private static AnnotationType inferTargetMetaInfo(Attribute.Compound a, Symbol s) {
            return AnnotationType.DECLARATION;
        }
    }

    private static class TypeAnnotationPositions extends TreeScanner {

        private final Names names;

        TypeAnnotationPositions(Names names) {
            this.names = names;
        }

        private ListBuffer<JCTree> frames = ListBuffer.lb();
        protected void push(JCTree t) { frames = frames.prepend(t); }
        protected JCTree pop() { return frames.next(); }
        private JCTree peek2() { return frames.toList().tail.head; }

        @Override
        public void scan(JCTree tree) {
            push(tree);
            super.scan(tree);
            pop();
        }

        private TypeAnnotationPosition resolveFrame(JCTree tree, JCTree frame,
                List<JCTree> path, TypeAnnotationPosition p, Names names) {
            /*
            System.out.println("Resolving tree: " + tree + " kind: " + tree.getKind());
            System.out.println("    Framing tree: " + frame + " kind: " + frame.getKind());
            */
            switch (frame.getKind()) {
                case TYPE_CAST:
                    p.type = TargetType.TYPECAST;
                    p.pos = frame.pos;
                    return p;

                case INSTANCE_OF:
                    p.type = TargetType.INSTANCEOF;
                    p.pos = frame.pos;
                    return p;

                case NEW_CLASS:
                    JCNewClass frameNewClass = (JCNewClass)frame;
                    if (frameNewClass.typeargs.contains(tree)) {
                        p.type = TargetType.NEW_TYPE_ARGUMENT;
                        p.type_index = frameNewClass.typeargs.indexOf(tree);
                    } else {
                        p.type = TargetType.NEW;
                    }
                    p.pos = frame.pos;
                    return p;

                case NEW_ARRAY:
                    p.type = TargetType.NEW;
                    p.pos = frame.pos;
                    return p;

                case ANNOTATION_TYPE:
                case CLASS:
                case ENUM:
                case INTERFACE:
                    p.pos = frame.pos;
                    if (((JCClassDecl)frame).extending == tree) {
                        p.type = TargetType.CLASS_EXTENDS;
                        p.type_index = -1;
                    } else if (((JCClassDecl)frame).implementing.contains(tree)) {
                        p.type = TargetType.CLASS_EXTENDS;
                        p.type_index = ((JCClassDecl)frame).implementing.indexOf(tree);
                    } else if (((JCClassDecl)frame).typarams.contains(tree)) {
                        p.type = TargetType.CLASS_TYPE_PARAMETER;
                        p.parameter_index = ((JCClassDecl)frame).typarams.indexOf(tree);
                    } else {
                        throw new AssertionError("Could not determine position of tree " + tree +
                                " within frame " + frame);
                    }
                    return p;

                case METHOD: {
                    JCMethodDecl frameMethod = (JCMethodDecl) frame;
                    p.pos = frame.pos;
                    if (frameMethod.thrown.contains(tree)) {
                        p.type = TargetType.THROWS;
                        p.type_index = frameMethod.thrown.indexOf(tree);
                    } else if (frameMethod.restype == tree) {
                        p.type = TargetType.METHOD_RETURN;
                    } else if (frameMethod.typarams.contains(tree)) {
                        p.type = TargetType.METHOD_TYPE_PARAMETER;
                        p.parameter_index = frameMethod.typarams.indexOf(tree);
                    } else {
                        throw new AssertionError("Could not determine position of tree " + tree +
                                " within frame " + frame);
                    }
                    return p;
                }

                case PARAMETERIZED_TYPE: {
                    if (((JCTypeApply)frame).clazz == tree) {
                        // generic: RAW; noop
                    } else if (((JCTypeApply)frame).arguments.contains(tree)) {
                        p.location = p.location.prepend(
                                ((JCTypeApply)frame).arguments.indexOf(tree));
                    } else {
                        throw new AssertionError("Could not determine position of tree " + tree +
                                " within frame " + frame);
                    }

                    List<JCTree> newPath = path.tail;
                    return resolveFrame(newPath.head, newPath.tail.head, newPath, p, names);
                }

                case ARRAY_TYPE: {
                    int index = 0;
                    List<JCTree> newPath = path.tail;
                    while (true) {
                        JCTree npHead = newPath.tail.head;
                        if (npHead.hasTag(JCTree.Tag.TYPEARRAY)) {
                            newPath = newPath.tail;
                            index++;
                        } else if (npHead.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
                            newPath = newPath.tail;
                        } else {
                            break;
                        }
                    }
                    p.location = p.location.prepend(index);
                    return resolveFrame(newPath.head, newPath.tail.head, newPath, p, names);
                }

                case TYPE_PARAMETER:
                    if (path.tail.tail.head.hasTag(JCTree.Tag.CLASSDEF)) {
                        JCClassDecl clazz = (JCClassDecl)path.tail.tail.head;
                        p.type = TargetType.CLASS_TYPE_PARAMETER_BOUND;
                        p.parameter_index = clazz.typarams.indexOf(path.tail.head);
                        p.bound_index = ((JCTypeParameter)frame).bounds.indexOf(tree);
                        if (((JCTypeParameter)frame).bounds.get(0).type.isInterface()) {
                            // Account for an implicit Object as bound 0
                            p.bound_index += 1;
                        }
                    } else if (path.tail.tail.head.hasTag(JCTree.Tag.METHODDEF)) {
                        JCMethodDecl method = (JCMethodDecl)path.tail.tail.head;
                        p.type = TargetType.METHOD_TYPE_PARAMETER_BOUND;
                        p.parameter_index = method.typarams.indexOf(path.tail.head);
                        p.bound_index = ((JCTypeParameter)frame).bounds.indexOf(tree);
                        if (((JCTypeParameter)frame).bounds.get(0).type.isInterface()) {
                            // Account for an implicit Object as bound 0
                            p.bound_index += 1;
                        }
                    } else {
                        throw new AssertionError("Could not determine position of tree " + tree +
                                " within frame " + frame);
                    }
                    p.pos = frame.pos;
                    return p;

                case VARIABLE:
                    VarSymbol v = ((JCVariableDecl)frame).sym;
                    p.pos = frame.pos;
                    switch (v.getKind()) {
                        case LOCAL_VARIABLE:
                            p.type = TargetType.LOCAL_VARIABLE;
                            break;
                        case FIELD:
                            p.type = TargetType.FIELD;
                            break;
                        case PARAMETER:
                            if (v.getQualifiedName().equals(names._this)) {
                                // TODO: Intro a separate ElementKind?
                                p.type = TargetType.METHOD_RECEIVER;
                            } else {
                                p.type = TargetType.METHOD_PARAMETER;
                                p.parameter_index = methodParamIndex(path, frame);
                            }
                            break;
                        case EXCEPTION_PARAMETER:
                            p.type = TargetType.EXCEPTION_PARAMETER;
                            break;
                        default:
                            throw new AssertionError("Found unexpected type annotation for variable: " + v + " with kind: " + v.getKind());
                    }
                    return p;

                case ANNOTATED_TYPE: {
                    JCAnnotatedType atypetree = (JCAnnotatedType) frame;
                    if (!atypetree.onRightType) {
                        final Type utype = atypetree.underlyingType.type;
                        Symbol tsym = utype.tsym;
                        // The number of "steps" to get from the full type to the
                        // left-most outer type.
                        int steps = 0;
                        Symbol encl = tsym.getEnclosingElement();
                        if (tsym.getKind().equals(ElementKind.TYPE_PARAMETER)) {
                            // Type parameters have the declaring class/method as enclosing elements.
                            // There is actually nothing to do for them.
                            steps = -1;
                        } else {
                            while (encl != null && encl.getKind() != ElementKind.PACKAGE) {
                                tsym = encl;
                                encl = encl.getEnclosingElement();
                                ++steps;
                            }
                        }

                        if (steps > 0) {
                            // Now we go up the actual AST and see how many steps we can take.
                            JCTree realframe = frame;
                            int tooksteps;
                            loop: for (tooksteps = 0; tooksteps<steps; ++tooksteps) {
                                switch (realframe.getKind()) {
                                case MEMBER_SELECT:
                                    realframe = ((JCFieldAccess)realframe).selected;
                                    break;
                                case ANNOTATED_TYPE:
                                    realframe = ((JCAnnotatedType)realframe).underlyingType;
                                    // Going through an annotated type doesn't count.
                                    --tooksteps;
                                    break;
                                case PARAMETERIZED_TYPE:
                                    realframe = ((JCTypeApply)realframe).getType();
                                    // Going through a parameterized type doesn't count.
                                    --tooksteps;
                                    break;
                                case IDENTIFIER:
                                    // We already reached the end of the AST. This happens when a short name is used
                                    // for a nested class. E.g. for type "Outer.Inner" we have "@A Inner"
                                    break loop;
                                default:
                                    System.out.println("unhandled frame: " + realframe + " kind: " + realframe.getKind());
                                    System.out.println("    tsym: " + tsym + " kind: " + tsym.getKind());
                                }
                            }

                            if (tooksteps > 0 && isWithin(tree, realframe)) {
                                // If tooksteps == 0, the annotation is on an inner class, but no
                                // outer class is specified. Therefore, nothing is to do.
                                List<TypeSymbol> typeparams = utype.asElement().getTypeParameters();
                                if (typeparams.nonEmpty()) {
                                    // The "top-level" generics are an offset for the index
                                    tooksteps += typeparams.size();
                                }
                                // Take off one initial step.
                                --tooksteps;
                                if (tooksteps >= 0) {
                                    p.location = p.location.prepend(tooksteps);
                                }
                            }
                        }
                    }

                    List<JCTree> newPath = path.tail;
                    TypeAnnotationPosition rec = resolveFrame(newPath.head, newPath.tail.head,
                            newPath, p, names);
                    return rec;
                }

                case METHOD_INVOCATION: {
                    JCMethodInvocation invocation = (JCMethodInvocation)frame;
                    if (!invocation.typeargs.contains(tree)) {
                        throw new AssertionError("{" + tree + "} is not an argument in the invocation: " + invocation);
                    }
                    p.type = TargetType.METHOD_TYPE_ARGUMENT;
                    p.pos = invocation.pos;
                    p.type_index = invocation.typeargs.indexOf(tree);
                    return p;
                }

                case EXTENDS_WILDCARD:
                case SUPER_WILDCARD: {
                    // Annotations in wildcard bounds always add a 0
                    p.location = p.location.prepend(0);
                    List<JCTree> newPath = path.tail;
                    return resolveFrame(newPath.head, newPath.tail.head, newPath, p, names);
                }

                case MEMBER_SELECT: {
                    int index = 0;
                    List<JCTree> newPath = path.tail;
                    JCTree npHead;
                    while (true) {
                        npHead = newPath.tail.head;
                        if (npHead.hasTag(JCTree.Tag.SELECT)) {
                            // Count each dot we see
                            newPath = newPath.tail;
                            index++;
                        } else if (npHead.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
                            // Skip over annotated types, we already count the dots
                            newPath = newPath.tail;
                        } else if (npHead.hasTag(JCTree.Tag.TYPEAPPLY)) {
                            JCTypeApply apply = (JCTypeApply) npHead;
                            if (apply.arguments.contains(newPath.head)) {
                                break;
                            } else {
                                // Skip over parameterized types on the same level
                                newPath = newPath.tail;
                            }
                        } else {
                            break;
                        }
                    }

                    // We are going up a level!
                    if (newPath.head.hasTag(JCTree.Tag.TYPEAPPLY)) {
                        // The type (of which we are a part) has type arguments.
                        // Add the size as an offset. Go to element to account for raw types!
                        index += ((JCTypeApply) newPath.head).type.tsym.getTypeParameters().size();
                    } else if (newPath.head.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
                        JCExpression under = ((JCAnnotatedType) newPath.head).getUnderlyingType();
                        if (under.hasTag(JCTree.Tag.TYPEAPPLY)) {
                            index += ((JCTypeApply) under).type.tsym.getTypeParameters().size();
                        }
                    } else if (newPath.head.hasTag(JCTree.Tag.SELECT)) {
                        index += ((JCFieldAccess) newPath.head).type.tsym.getTypeParameters().size();
                    }

                    p.location = p.location.prepend(index);
                    return resolveFrame(newPath.head, newPath.tail.head, newPath, p, names);
                }
                default:
                    throw new AssertionError("Unresolved frame: " + frame + " of kind: " + frame.getKind() +
                            "\n    Looking for tree: " + tree);
            }
        }

        /** Determine whether we can reach frame from tree.
         * 
         * @param tree the "inner" tree
         * @param frame the "framing" tree
         * @return true, iff tree is within frame
         */
        private boolean isWithin(JCTree tree, JCTree frame) {
            boolean lastWasUp = false;
            loop: while (true) {
                if (tree == frame) {
                    return lastWasUp;
                }
                switch (tree.getKind()) {
                case ANNOTATED_TYPE:
                    if (!((JCAnnotatedType)tree).onRightType) {
                        return true;
                    }
                    tree = ((JCAnnotatedType)tree).underlyingType;
                    break;
                case MEMBER_SELECT:
                    lastWasUp = false;
                    tree = ((JCFieldAccess)tree).selected;
                    break;
                case PARAMETERIZED_TYPE:
                    lastWasUp = true;
                    tree = ((JCTypeApply)tree).clazz;
                    break;
                default:
                    break loop;
                }
            }
            return false;
        }

        private static void setTypeAnnotationPos(List<JCTypeAnnotation> annotations, TypeAnnotationPosition position) {
            for (JCTypeAnnotation anno : annotations) {
                anno.attribute_field.position = position;
            }
        }

        @Override
        public void visitNewArray(JCNewArray tree) {
            findPosition(tree, tree, tree.annotations);
            int dimAnnosCount = tree.dimAnnotations.size();

            // handle annotations associated with dimensions
            for (int i = 0; i < dimAnnosCount; ++i) {
                TypeAnnotationPosition p = new TypeAnnotationPosition();
                p.pos = tree.pos;
                if (i == 0) {
                    p.type = TargetType.NEW;
                } else {
                    p.type = TargetType.NEW_COMPONENT;
                    p.location = p.location.append(i - 1);
                }

                setTypeAnnotationPos(tree.dimAnnotations.get(i), p);
            }

            // handle "free" annotations
            int i = dimAnnosCount == 0 ? 0 : dimAnnosCount - 1;
            JCExpression elemType = tree.elemtype;
            while (elemType != null) {
                if (elemType.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
                    JCAnnotatedType at = (JCAnnotatedType)elemType;
                    TypeAnnotationPosition p = new TypeAnnotationPosition();
                    p.type = TargetType.NEW_COMPONENT;
                    p.pos = tree.pos;
                    p.location = p.location.append(i);
                    setTypeAnnotationPos(at.annotations, p);
                    elemType = at.underlyingType;
                } else if (elemType.hasTag(JCTree.Tag.TYPEARRAY)) {
                    ++i;
                    elemType = ((JCArrayTypeTree)elemType).elemtype;
                } else {
                    break;
                }
            }

            // TODO: Is this needed?
            scan(tree.elems);
        }

        @Override
        public void visitAnnotatedType(JCAnnotatedType tree) {
            push(tree);
            findPosition(tree, tree, tree.annotations);
            pop();
            super.visitAnnotatedType(tree);
        }

        @Override
        public void visitTypeParameter(JCTypeParameter tree) {
            findPosition(tree, peek2(), tree.annotations);
            super.visitTypeParameter(tree);
        }

        private void findPosition(JCTree tree, JCTree frame, List<JCTypeAnnotation> annotations) {
            if (!annotations.isEmpty()) {
                /*
                System.out.println("Finding pos for: " + annotations);
                System.out.println("    tree: " + tree);
                System.out.println("    frame: " + frame);
                */
                TypeAnnotationPosition p =
                        resolveFrame(tree, frame, frames.toList(),
                                new TypeAnnotationPosition(), names);
                if (!p.location.isEmpty())
                    p.type = p.type.getGenericComplement();
                setTypeAnnotationPos(annotations, p);
            }
        }

        private int methodParamIndex(List<JCTree> path, JCTree param) {
            List<JCTree> curr = path;
            while (curr.head.getTag() != Tag.METHODDEF) {
                curr = curr.tail;
            }
            JCMethodDecl method = (JCMethodDecl)curr.head;
            return method.params.indexOf(param);
        }
    }
}
