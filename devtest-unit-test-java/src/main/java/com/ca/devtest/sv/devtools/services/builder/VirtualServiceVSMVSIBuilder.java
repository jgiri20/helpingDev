/**
 * 
 */
package com.ca.devtest.sv.devtools.services.builder;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ca.devtest.sv.devtools.VirtualServiceEnvironment;
import com.ca.devtest.sv.devtools.annotation.VirtualServiceType;
import com.ca.devtest.sv.devtools.utils.PackMarFile;

/**
 * @author gaspa03
 *
 * 
 */
public final class VirtualServiceVSMVSIBuilder extends VirtualServiceBuilder {
	
	private File workingFolder=null;

	public VirtualServiceVSMVSIBuilder(String name, VirtualServiceEnvironment vse,File workingFolder) {
		super(name, vse);
		this.workingFolder=workingFolder;
		setType(VirtualServiceType.VSM);
	}

	protected File packVirtualService() throws IOException {

		Map<String, String> config = new HashMap<String, String>();
		// add Info to maraudit File
		config.put("dateOfMar", getDateOfMar());
		config.put("hostname", getHostName());
		config.put("serviceName", getServiceName());
		config.putAll(getParameters());
		return PackMarFile.packVirtualService(getWorkingFolder(),config );
	}

	/**
	 * @return
	 */
	private String getHostName() {
		String result = "UNKNOWN";
		try {
			InetAddress address = InetAddress.getLocalHost();

			result = address.getHostName();
		} catch (UnknownHostException e) {

		}
		return result;
	}
	/**
	 * @return
	 */
	private String getDateOfMar() {
		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd.HH:mm:ss.SSS.Z");
		
		return df.format(new Date());
	}
	/**
	 * @return the workingFolder
	 */
	protected File getWorkingFolder() {
		return workingFolder;
	}

	/**
	 * @param workingFolder the workingFolder to set
	 */
	protected void setWorkingFolder(File workingFolder) {
		this.workingFolder = workingFolder;
	}
}
