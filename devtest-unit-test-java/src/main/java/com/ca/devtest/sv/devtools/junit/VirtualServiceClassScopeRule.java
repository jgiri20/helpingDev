package com.ca.devtest.sv.devtools.junit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServer;
import com.ca.devtest.sv.devtools.annotation.processor.DevTestVirtualServerAnnotationProcessor;
import com.ca.devtest.sv.devtools.services.VirtualService;

/**
 * @author gaspa03
 *
 */
public class VirtualServiceClassScopeRule implements TestRule {

	/**
	 * 
	 */
	private static final Log LOGGER = LogFactory.getLog(VirtualServiceClassScopeRule.class);

	/**
	 * @author gaspa03
	 *
	 */
	public class VirtualServiceClassStatement extends Statement {

		private final Statement statement;
		private final Class testClazz;
		private List<VirtualService> listVirtualServicesDeployed = new ArrayList<VirtualService>();

		public VirtualServiceClassStatement(Statement aStatement, @SuppressWarnings("rawtypes") Class aName) {
			statement = aStatement;
			testClazz = aName;
		}

		@Override
		public void evaluate() throws Throwable {

			try {
				beforeClass(testClazz);
				statement.evaluate();
			} finally {

				afterClass(testClazz);
			}

		}

		private void beforeClass(Class clazz) {
			LOGGER.info("before: " + testClazz);

			doCustomBeforeClass(clazz);

			if (clazzNeedVirtualServices(testClazz)) {
				listVirtualServicesDeployed = processClazzAnnotations(testClazz);
				deployVirtualServices(listVirtualServicesDeployed);
			}
		}

		private void afterClass(Class clazz) {

			LOGGER.info("undeploying VS for clazz ");
			try {
				doCustomAfterClass(clazz);
			} finally {
				unDeployVirtualServices(listVirtualServicesDeployed);
			}

		}

	}

	/**
	 * @param clazz
	 */
	protected void doCustomAfterClass(Class clazz) {
		LOGGER.debug("doCustomAfterClass: " + clazz);

	}

	/**
	 * @param clazz
	 */
	protected void doCustomBeforeClass(Class clazz) {
		LOGGER.debug("doCustomBeforeClass: " + clazz);

	}

	/**
	 * 
	 * @param virtualServices
	 *            list of virtual services to undeployy
	 */
	private void unDeployVirtualServices(Collection<VirtualService> virtualServices) {

		if (null != virtualServices) {
			for (VirtualService virtualService : virtualServices) {
				try {
					LOGGER.debug("unDeploy virtual service " + virtualService.getDeployedName() + ".....");
					virtualService.unDeploy();
					LOGGER.debug("Virtual service " + virtualService.getDeployedName() + " unDeployed!");

				} catch (Exception error) {

					throw new RuntimeException(
							"Error when try to unDeploy Virtual Service  " + virtualService.getDeployedName());

				}
			}
		}

	}

	/**
	 * @param virtualServices
	 *            list of virtual services to deploy
	 */
	private void deployVirtualServices(List<VirtualService> virtualServices) {

		if (null != virtualServices) {
			for (VirtualService virtualService : virtualServices) {
				try {
					LOGGER.debug("Deploy virtual service " + virtualService.getDeployedName() + ".....");
					virtualService.deploy();
					LOGGER.debug("Virtual service " + virtualService.getDeployedName() + " deployed!");
				} catch (IOException e) {
					throw new RuntimeException(
							"Error when try to deploy Virtual Service  " + virtualService.getDeployedName());
				}
			}
		}

	}

	/**
	 * Find out SV annotation on class level
	 * 
	 * @param testClass
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	protected List<VirtualService> processClazzAnnotations(Class<?> testClazz) {
		List<VirtualService> virtualServices = new ArrayList<VirtualService>();
		try {
			LOGGER.debug("Process Clazzz annotation  " + testClazz);

			DevTestVirtualServerAnnotationProcessor devtestProcessor = new DevTestVirtualServerAnnotationProcessor(
					testClazz);
			virtualServices.addAll(devtestProcessor.process(testClazz));

		} catch (Exception error) {

			throw new RuntimeException("Error when try to build Virtual Service over " + testClazz, error);
		}

		return virtualServices;

	}

	/**
	 * @param testClass
	 * @return
	 */
	private boolean clazzNeedVirtualServices(Class<?> clazz) {

		return null != clazz.getAnnotation(DevTestVirtualServer.class);

	}

	public VirtualServiceClassScopeRule() {

	}

	@Override
	public Statement apply(Statement statement, Description description) {
		System.out.println("apply: " + description.getTestClass());

		return new VirtualServiceClassStatement(statement, description.getTestClass());
	}

}