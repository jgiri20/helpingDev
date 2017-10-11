package com.ca.devtest.sv.devtools;

import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.ca.devtest.sv.devtools.annotation.Config;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServer;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualService;
import com.ca.devtest.sv.devtools.annotation.DevTestVirtualServiceFromVrs;
import com.ca.devtest.sv.devtools.annotation.Parameter;
import com.ca.devtest.sv.devtools.annotation.VirtualServiceType;
import com.ca.devtest.sv.devtools.junit.VirtualServicesRule;

@DevTestVirtualServer(deployServiceToVse = "VSE")
@DevTestVirtualService(serviceName = "Proxy", type = VirtualServiceType.VSM, workingFolder = "mar/vsm/Bouygues_Demo")
public class GlobalVirtualServiceTest {

	@Rule
	public VirtualServicesRule rules = new VirtualServicesRule();
	
	@ClassRule
	public static VirtualServicesRule clazzRule = new VirtualServicesRule();
	
	@DevTestVirtualServiceFromVrs(serviceName = "demo", workingFolder = "rrpairs/soapWithVrs", vrsConfig = @Config(value = "transport.vrs", parameters = {
			@Parameter(name = "port", value = "9002"), @Parameter(name = "basePath", value = "/lisa") }))
	@Test
	public void test1() {
		fail("Not yet implemented");
	}

	@DevTestVirtualServiceFromVrs(serviceName = "demo2", workingFolder = "rrpairs/soapWithVrs", vrsConfig = @Config(value = "transport.vrs", parameters = {
			@Parameter(name = "port", value = "9002"), @Parameter(name = "basePath", value = "/lisa") }))
	@Test
	public void test2() {
		fail("Not yet implemented");
	}
}
