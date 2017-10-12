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
import com.ca.devtest.sv.devtools.annotation.processor.DevTestAnnotationProcessor;
import com.ca.devtest.sv.devtools.services.VirtualService;

public class VirtualServiceClassScopeRule implements TestRule {

	private static final Log LOGGER = LogFactory.getLog(VirtualServiceClassScopeRule.class);

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
			if (clazzNeedVirtualServices(testClazz)) {
				listVirtualServicesDeployed = processClazzAnnotations(testClazz);
				deployVirtualServices(listVirtualServicesDeployed);
			}
		}

		private void afterClass(Class clazz) {

			LOGGER.info("undeploying VS for clazz ");
			unDeployVirtualServices(listVirtualServicesDeployed);
		}

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
					LOGGER.debug("unDeploy virtual service " + virtualService.getName() + ".....");
					virtualService.unDeploy();
					LOGGER.debug("Virtual service " + virtualService.getName() + " unDeployed!");

				} catch (Exception error) {

					throw new RuntimeException(
							"Error when try to unDeploy Virtual Service  " + virtualService.getName());

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
					LOGGER.debug("Deploy virtual service " + virtualService.getName() + ".....");
					virtualService.deploy();
					LOGGER.debug("Virtual service " + virtualService.getName() + " deployed!");
				} catch (IOException e) {
					throw new RuntimeException("Error when try to deploy Virtual Service  " + virtualService.getName());
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
	private List<VirtualService> processClazzAnnotations(Class<?> testClazz) {
		List<VirtualService> virtualServices = new ArrayList<VirtualService>();
		try {
			LOGGER.debug("Process Clazzz annotation  " + testClazz);

			DevTestAnnotationProcessor devtestProcessor = new DevTestAnnotationProcessor(testClazz);
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