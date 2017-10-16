package fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation.processor;

import java.lang.annotation.Annotation;
import java.util.List;

import com.ca.devtest.sv.devtools.DevTestClient;
import com.ca.devtest.sv.devtools.annotation.processor.AnnotationProcessor;
import com.ca.devtest.sv.devtools.exception.VirtualServiceProcessorException;
import com.ca.devtest.sv.devtools.services.VirtualService;

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
			
		return null;
	}

}
