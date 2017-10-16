/**
 * 
 */
package fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.junit;

import com.ca.devtest.sv.devtools.annotation.processor.AnnotationProcessorFactory;
import com.ca.devtest.sv.devtools.junit.VirtualServiceClassScopeRule;

import fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation.DevTestProxySLDV4;
import fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation.processor.DevTestProxySLDV4AnnotationProcessor;

/**
 * @author gaspa03
 *
 */
public class DevTestProxySLDV4Rules extends VirtualServiceClassScopeRule {
	static {
		// inject SLDV4 Processor in the AnnotationFactory
		AnnotationProcessorFactory.getInstance().addProcessor(DevTestProxySLDV4.class,
				DevTestProxySLDV4AnnotationProcessor.class);
	}

	public DevTestProxySLDV4Rules() {
		super();
	}

	@Override
	protected void doCustomAfterClass(Class clazz) {
		// TODO Auto-generated method stub
		super.doCustomAfterClass(clazz);
	}

	@Override
	protected void doCustomBeforeClass(Class clazz) {
		// TODO Auto-generated method stub
		super.doCustomBeforeClass(clazz);
	}

}
