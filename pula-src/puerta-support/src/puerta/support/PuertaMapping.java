package puerta.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

public class PuertaMapping extends DefaultAnnotationHandlerMapping {
	/**
	 * Common suffix at the end of controller implementation classes. Removed
	 * when generating the URL path.
	 */
	private static final String CONTROLLER_SUFFIX = "Controller";

	private List<String> basePackageList;
	private boolean caseSensitive = false;

	private String pathPrefix;

	// org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping
	// c;

	/**
	 * Set whether to apply case sensitivity to the generated paths, e.g.
	 * turning the class name "BuyForm" into "buyForm".
	 * <p>
	 * Default is "false", using pure lower case paths, e.g. turning the class
	 * name "BuyForm" into "buyform".
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * Specify a prefix to prepend to the path generated from the controller
	 * name.
	 * <p>
	 * Default is a plain slash ("/"). A path like "/mymodule" can be specified
	 * in order to have controller path mappings prefixed with that path, e.g.
	 * "/mymodule/buyform" instead of "/buyform" for the class name "BuyForm".
	 */
	public void setPathPrefix(String prefixPath) {
		this.pathPrefix = prefixPath;
		if (StringUtils.hasLength(this.pathPrefix)) {
			if (!this.pathPrefix.startsWith("/")) {
				this.pathPrefix = "/" + this.pathPrefix;
			}
			if (this.pathPrefix.endsWith("/")) {
				this.pathPrefix = this.pathPrefix.substring(0,
						this.pathPrefix.length() - 1);
			}
		}
	}

	public void setBasePackages(String[] basePackages) {
		basePackageList = new ArrayList<String>(basePackages.length);
		for (String s : basePackages) {
			if (StringUtils.hasLength(s) && !s.endsWith(".")) {
				s = s + ".";
			}
			basePackageList.add(s);
		}

	}

	@Override
	protected String[] determineUrlsForHandler(String beanName) {

		ApplicationContext context = getApplicationContext();
		RequestMapping mapping = context.findAnnotationOnBean(beanName,
				RequestMapping.class);
		Class<?> handlerType = context.getType(beanName);
		if (mapping != null) {
			// do it as DefaultAnnotationHandlerMapping
			return super.determineUrlsForHandler(beanName);
		} else {
			if (isControllerType(handlerType)) {
				return generatePathMappings(handlerType);
			}

		}

		return null;
	}

	private StringBuilder buildPathPrefix(Class<?> beanClass) {
		StringBuilder pathMapping = new StringBuilder();
		if (this.pathPrefix != null) {
			pathMapping.append(this.pathPrefix);
			pathMapping.append("/");
		} else {
			pathMapping.append("/");
		}
		if (this.basePackageList != null && this.basePackageList.size() != 0) {
			for (String bp : basePackageList) {
				String packageName = ClassUtils.getPackageName(beanClass);
				if (packageName.startsWith(bp)) {
					String subPackage = packageName.substring(bp.length())
							.replace('.', '/');
					pathMapping.append(this.caseSensitive ? subPackage
							: subPackage.toLowerCase());
					pathMapping.append("/");
					break;
				}
			}

		}
		return pathMapping;
	}

	protected String[] generatePathMappings(Class<?> beanClass) {
		StringBuilder pathMapping = buildPathPrefix(beanClass);
		String className = ClassUtils.getShortName(beanClass);
		String path = (className.endsWith(CONTROLLER_SUFFIX) ? className
				.substring(0, className.lastIndexOf(CONTROLLER_SUFFIX))
				: className);
		if (path.length() > 0) {
			if (this.caseSensitive) {
				pathMapping.append(path.substring(0, 1).toLowerCase()).append(
						path.substring(1));
			} else {
				pathMapping.append(path.toLowerCase());
			}
		}
		if (isMultiActionControllerType(beanClass)) {
			return new String[] { pathMapping.toString(),
					pathMapping.toString() + "/*" };
		} else {
			return new String[] { pathMapping.toString() + "*" };
		}
	}

	/**
	 * Determine whether the given bean class indicates a controller type that
	 * dispatches to multiple action methods.
	 * 
	 * @param beanClass
	 *            the class to introspect
	 */
	protected boolean isMultiActionControllerType(Class<?> beanClass) {
		return AnnotationUtils.findAnnotation(beanClass, Controller.class) != null;
	}

	/**
	 * Determine whether the given bean class indicates a controller type that
	 * is supported by this mapping strategy.
	 * 
	 * @param beanClass
	 *            the class to introspect
	 */
	protected boolean isControllerType(Class<?> beanClass) {
		return AnnotationUtils.findAnnotation(beanClass, Controller.class) != null;
	}
}
