package fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation.processor;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ca.devtest.sv.devtools.DevTestClient;
import com.ca.devtest.sv.devtools.annotation.processor.AnnotationProcessor;
import com.ca.devtest.sv.devtools.exception.VirtualServiceProcessorException;
import com.ca.devtest.sv.devtools.services.VirtualService;
import com.ca.devtest.sv.devtools.services.builder.VirtualServiceBuilder;

import fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation.DevTestProxySLDV4;

/**
 * @author gaspa03
 *
 */
public  class DevTestProxySLDV4AnnotationProcessor implements AnnotationProcessor {

	
	
	
	/* (non-Javadoc)
	 * @see com.ca.devtest.sv.devtools.annotation.processor.AnnotationProcessor#process(com.ca.devtest.sv.devtools.DevTestClient, java.lang.annotation.Annotation)
	 */
	public List<VirtualService> process(DevTestClient devTestClient, Annotation annotation)
			throws VirtualServiceProcessorException {
		
		try {
			DevTestProxySLDV4 myAnnotation =(DevTestProxySLDV4)annotation;
			VirtualServiceBuilder virtualServiceBuilder = null;
			URL url = getClass().getClassLoader().getResource("vsm/proxy");
			File workingFolder = new File(url.toURI());
			virtualServiceBuilder= devTestClient.fromVSMVSI("ProxySLDV4", workingFolder);
			virtualServiceBuilder.addKeyValue("devTestAgents",myAnnotation.devTestAgents());
			virtualServiceBuilder.addKeyValue("port",myAnnotation.port());
			List<VirtualService> result = new ArrayList<VirtualService>(1);
			result.add(virtualServiceBuilder.build());
			return result;
		} catch (Exception error) {
			throw new VirtualServiceProcessorException("Error during building virtual service", error);
		}
	}

}
