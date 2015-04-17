/**
 * Created on 2009-12-15
 * WXL 2009
 * $Id$
 */
package puerta.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author tiyi
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WxlDomain {
	public abstract java.lang.String value() default "";

}
