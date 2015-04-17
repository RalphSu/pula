package puerta.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 栅栏，访问前过一道！
 * 
 * @author tiyi
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Barrier {

	// value 为空表示只检查在线与否
	String[] value() default "";

	boolean ignore() default false;

	boolean check() default true;
}
