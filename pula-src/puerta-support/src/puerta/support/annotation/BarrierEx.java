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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BarrierEx {

	// value 为空表示只检查在线与否
	BarrierExClz[] value();
}
