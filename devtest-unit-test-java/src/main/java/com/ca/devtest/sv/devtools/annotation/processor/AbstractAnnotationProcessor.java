/**
 * 
 */
package com.ca.devtest.sv.devtools.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ca.devtest.sv.devtools.DevTestClient;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServer;
import com.ca.devtest.sv.devtools.exception.VirtualServiceProcessorException;
import com.ca.devtest.sv.devtools.services.VirtualService;

/**
 * @author gaspa03
 *
 */
public class AbstractAnnotationProcessor {

	private final DevTestClient devtestClient;
	private static final Log LOGGER = LogFactory.getLog(AbstractAnnotationProcessor.class);

	/**
	 * @param ownerClazz
	 */
	public AbstractAnnotationProcessor(Class<?> ownerClazz) {
		super();
		devtestClient = buildDevtestClient(ownerClazz);

	}

	/**
	 * @param clazz
	 * @return
	 */
	private DevTestClient buildDevtestClient(Class<?> clazz) {
		DevTestVirtualServer virtualServer = clazz.getAnnotation(DevTestVirtualServer.class);
		return new DevTestClient(virtualServer.registryHost(), virtualServer.deployServiceToVse(),
				virtualServer.login(), virtualServer.password(), virtualServer.groupName());

	}
	public  List<VirtualService> process(Class<?> clazz) throws VirtualServiceProcessorException {

		Annotation[] annotations = clazz.getDeclaredAnnotations();
		List<VirtualService> virtualServices = new ArrayList<VirtualService>();
		for (Annotation annotation : annotations) {
			// get Annotation processor 
			AnnotationProcessor processor = AnnotationProcessorFactory.getProcessor(annotation);
			List<VirtualService> services=processor.process(devtestClient,annotation);
			if( null!=services)
			virtualServices.addAll(services);
		}

		return virtualServices;
	}
	public  List<VirtualService> process(Method method) throws VirtualServiceProcessorException {

		Annotation[] annotations = method.getDeclaredAnnotations();
		List<VirtualService> virtualServices = new ArrayList<VirtualService>();
		for (Annotation annotation : annotations) {
			// get Annotation processor 
			AnnotationProcessor processor = AnnotationProcessorFactory.getProcessor(annotation);
			List<VirtualService> services=processor.process(devtestClient,annotation);
			if( null!=services)
			virtualServices.addAll(services);
		}

		return virtualServices;
	}

}
