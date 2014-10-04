package com.example;

import com.samskivert.mustache.Mustache;

import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.example.MarkedForProcessing")
public class MyAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (final Element e : roundEnv.getElementsAnnotatedWith(MarkedForProcessing.class)) {
                if (e.getKind() == ElementKind.CLASS) {

                    final TypeElement classElement = (TypeElement) e;
                    final PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

                    final String generatedClassName = "Generated" + classElement.getSimpleName().toString();
                    final JavaFileObject jfo = MyAnnotationProcessor.this.processingEnv.getFiler().createSourceFile(
                                                   packageElement.getQualifiedName() + "." + generatedClassName
                                               );

                    final String template = "package {{context_packageElement.qualifiedName}};\n" +
                                            "\n" +
                                            "public class {{context_generatedClassName}} {\n" +
                                            "    \n" +
                                            "}\n";
                    final Object context = new Object() {
                        final PackageElement context_packageElement = packageElement;
                        final String context_generatedClassName = generatedClassName;
                    };
                    final String fileContents = Mustache.compiler().compile(template).execute(context);
                    jfo.openWriter().append(fileContents).close();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return true;
        }
    }
}
