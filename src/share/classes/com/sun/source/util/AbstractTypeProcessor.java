/*
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package com.sun.source.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.*;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

/**
 * This class is an abstract annotation processor designed to be a
 * convenient superclass for concrete "type processors", processors that
 * require the type information in the processed source.
 *
 * <p>Type processing occurs in one round after the tool analyzes
 * the source (all sources taken as input to the tool and sources generated
 * by other annotation processors).
 *
 * <p>The tool is permitted to ask type processors to process a class once
 * it is analyzed before the rest of classes are analyzed.  The tool is also
 * permitted to stop type processing if any errors are raised.
 *
 * <p>A subclass may override any of the methods in this class, as long as the
 * general {@link javax.annotation.processing.Processor Processor}
 * contract is obeyed, with one notable exception.
 * {@link #process(Set, RoundEnvironment)} may not be overriden, as it
 * is called during the regular annotation phase before classes are analyzed.
 *
 * @author Mahmood Ali
 * @since 1.7
 */
public abstract class AbstractTypeProcessor extends AbstractProcessor {
    private final Set<Name> elements = new HashSet<Name>();
    private JavacProcessingEnvironment env;
    private final AttributionTaskListener listener = new AttributionTaskListener();

    /**
     * Constructor for subclasses to call.
     */
    protected AbstractTypeProcessor() { }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        this.env = (JavacProcessingEnvironment)env;
        prepareContext(this.env.getContext());
    }

    @Override
    public final boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        for (TypeElement elem : ElementFilter.typesIn(roundEnv.getRootElements())) {
            elements.add(elem.getQualifiedName());
        }
        return false;
    }

    /**
     * Processes a fully analyzed class that contains a supported annotation
     * (look {@link #getSupportedAnnotationTypes()}).
     *
     * <p>The passed class is always a valid type-checked Java code.
     *
     * @param element       element of the analyzed class
     * @param tree  the tree path to the element, with the leaf being a
     *              {@link ClassTree}
     */
    public abstract void process(TypeElement element, TreePath tree);

    /**
     * A listener method to be called once all classes have been processed
     * and no error is reported.
     */
    public void processOver() { }

    /**
     * adds a listener for attribution.
     */
    private void prepareContext(Context context) {
        TaskListener otherListener = context.get(TaskListener.class);
        if (otherListener == null) {
            context.put(TaskListener.class, listener);
        } else {
            // handle cases of multiple listeners
            context.put(TaskListener.class, (TaskListener)null);
            TaskListeners listeners = new TaskListeners();
            listeners.add(otherListener);
            listeners.add(listener);
            context.put(TaskListener.class, listeners);
        }
    }

    /**
     * A task listener that invokes the processor whenever a class is fully
     * analyzed.
     */
    private final class AttributionTaskListener implements TaskListener {

        @Override
        public void finished(TaskEvent e) {
            assert e.getTypeElement() != null;
            assert e.getCompilationUnit() != null;

            if (e.getKind() != TaskEvent.Kind.ANALYZE
                || !elements.remove(e.getTypeElement().getQualifiedName()))
                return;

            Log log = Log.instance(env.getContext());
            if (log.nerrors != 0)
                return;

            TypeElement elem = e.getTypeElement();
            TreePath p = Trees.instance(env).getPath(elem);

            process(elem, p);

            if (elements.isEmpty() && log.nerrors == 0)
                processOver();
        }

        @Override
        public void started(TaskEvent e) { }

    }

    /**
     * A task listener multiplexer.
     */
    private static class TaskListeners implements TaskListener {
        private final List<TaskListener> listeners = new ArrayList<TaskListener>();

        public void add(TaskListener listener) {
            listeners.add(listener);
        }

        public void remove(TaskListener listener) {
            listeners.remove(listener);
        }

        @Override
        public void finished(TaskEvent e) {
            for (TaskListener listener : listeners)
                listener.finished(e);
        }

        @Override
        public void started(TaskEvent e) {
            for (TaskListener listener : listeners)
                listener.started(e);
        }
    }
}
