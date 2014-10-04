package com.example;

public class MainClass {
    public static void main(String[] args) {
        /* The com.example.MyAnnotationProcessor class is registered as an
        annotation processor (in javax.annotation.processing.Processor). Since
        it supports the com.example.MarkedForProcessing annotation and
        com.example.Thing is annotated with @MarkedForProcessing,
        MyAnnotationProcessor creates the com.example.GeneratedThing class. */
        final GeneratedThing gt = new GeneratedThing();
    }
}
