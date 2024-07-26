/*
 * This code defines a custom annotation named {@code @ValidateRequest} in Java. Custom annotations can be used to
 * add metadata or behavior to code elements like classes or methods. Annotations are a form of metadata that provide
 * additional information about the code.
 *
 * The {@code @ValidateRequest} annotation includes the following elements:
 *
 * - {@code @Target}: Specifies the types of program elements to which this annotation can be applied. In this case,
 *   it can be applied to methods ({@code ElementType.METHOD}) and classes ({@code ElementType.TYPE}).
 *
 * - {@code @Retention}: Specifies the retention policy of the annotation. {@code RetentionPolicy.RUNTIME} indicates
 *   that this annotation will be retained at runtime and can be accessed through reflection. This is useful for
 *   frameworks or libraries that perform operations based on annotations at runtime.
 *
 * - {@code @interface}: Declares that this is an annotation type. It is used to define custom annotations.
 *
 * - {@code String value() default ""}: Defines an optional attribute named {@code value}. This attribute has a default
 *   value of an empty string (""). It can be overridden when the annotation is used, allowing for additional metadata
 *   to be associated with the annotated element.
 *
 * Purpose:
 * The {@code @ValidateRequest} annotation can be used to mark methods or classes that require request validation
 * logic to be applied. For example, it could be used in a web application to indicate that certain methods should
 * perform validation of incoming requests before processing them.
 */

package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateRequest {
    String value() default "";
}
