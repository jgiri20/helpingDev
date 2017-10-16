package com.ca.devtest.sv.devtools;

import static org.junit.Assert.fail;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.ca.devtest.sv.devtools.annotation.Config;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServer;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServiceFromVrs;
import com.ca.devtest.sv.devtools.annotation.Parameter;
import com.ca.devtest.sv.devtools.junit.VirtualServicesRule;

import fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.annotation.DevTestProxySLDV4;
import fr.pe.gssi.progiciel.oi043.plugins.oi043ascode.junit.DevTestProxySLDV4Rules;

@DevTestVirtualServer(deployServiceToVse = "VSE",groupName="Test")
@DevTestProxySLDV4(devTestAgents = "ibbo8800_audep", port ="9002", environnement="ENV_LOCAL_NGUPI", configLD="src/test/resources/configLD.xml")
public class GlobalVirtualServiceTest {

	// handle VS with Class scope
	@ClassRule
	public static DevTestProxySLDV4Rules clazzRule = new DevTestProxySLDV4Rules();
	//
	@Rule
	public VirtualServicesRule rules = new VirtualServicesRule();
	
	
	
	@DevTestVirtualServiceFromVrs(serviceName = "demo", workingFolder = "rrpairs/soapWithVrs", 
			vrsConfig = @Config(value = "transport.vrs",
			parameters = { @Parameter(name = "port", value = "9002"), @Parameter(name = "basePath", value = "/lisa") }))
	
	
	
	@Test
	public void test1() {
		fail("Not yet implemented");
	}

	@DevTestVirtualServiceFromVrs(serviceName = "demo", workingFolder = "rrpairs/soapWithVrs", vrsConfig = @Config(value = "transport.vrs", parameters = {
			@Parameter(name = "port", value = "9002"), @Parameter(name = "basePath", value = "/lisa") }))
	@Test
	public void test2() {
		fail("Not yet implemented");
	}
}
