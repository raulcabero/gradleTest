package utility

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo
import org.spockframework.runtime.model.SpecInfo

/**
 * Spock Environment Annotation Extension
 */
class EnvironmentEndPointExtension extends AbstractAnnotationDrivenExtension<EnvironmentEndPoint> {

	private static final Log LOG = LogFactory.getLog(getClass());

	private static def config = new ConfigSlurper().parse(new File('src/test/resources/SpockConfig.groovy').toURL())

	/**
	 * env environment variable
	 * <p>
	 * Defaults to {@code LOCAL_END_POINT}
	 */
	private static final String envString = System.getProperties().getProperty("env", config.envHost);

	static {
		LOG.info("Environment End Point [" + envString + "]")
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void visitFieldAnnotation(EnvironmentEndPoint annotation, FieldInfo field) {
		def interceptor = new EnvironmentInterceptor(field, envString)

		interceptor.install(field.parent.getTopSpec())
	}
}

/**
 * 
 * Environment Intercepter
 *
 */
class EnvironmentInterceptor extends AbstractMethodInterceptor {
	private final FieldInfo field
	private final String envString

	EnvironmentInterceptor(FieldInfo field, String envString) {
		this.field = field
		this.envString = envString
	}

	private void injectEnvironmentHost(target) {
		field.writeValue(target, envString)
	}

	@Override
	void interceptSetupMethod(IMethodInvocation invocation) {
		injectEnvironmentHost(invocation.target)
		invocation.proceed()
	}

	@Override
	void install(SpecInfo spec) {
		spec.setupMethod.addInterceptor this
	}
}
