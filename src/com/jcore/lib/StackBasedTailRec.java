package com.jcore.lib;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

/**
 * Stack-based recursion is harmful, because it is slow and can lead to StackOverflowErrors.
 * If you define a method without the TailCall hack, add this annotation to indicate
 * the implementation is not optimized to a heap-based solution.
 */
@Target(METHOD)
public @interface StackBasedTailRec {
}
