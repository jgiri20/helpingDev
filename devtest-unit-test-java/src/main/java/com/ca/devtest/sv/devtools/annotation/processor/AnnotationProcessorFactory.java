package com.ca.devtest.sv.devtools.annotation.processor;

import java.lang.annotation.Annotation;

import com.ca.devtest.sv.devtools.annotation.DevTestVirtualService;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServiceFromVrs;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServices;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServicesFromVrs;

/**
 * @author gaspa03
 *
 */
public class AnnotationProcessorFactory {

	/**
	 * @param annotation
	 * @return
	 */
	public static AnnotationProcessor getProcessor(Annotation annotation) {
		
		AnnotationProcessor processor=new NopAnnotationProcessor();
		if( annotation instanceof DevTestVirtualService)
			processor= new VirtualServiceAnnotationProcessor();
		if(annotation instanceof DevTestVirtualServices)
			processor= new VirtualServicesAnnotationProcessor();
		if( annotation instanceof DevTestVirtualServiceFromVrs)
			processor= new VirtualServiceFromVrsAnnotationProcessor(); 

		if( annotation instanceof DevTestVirtualServicesFromVrs)
			processor= new VirtualServicesFromVrsAnnotationProcessor(); 
					

		return processor;
	}
	

}
