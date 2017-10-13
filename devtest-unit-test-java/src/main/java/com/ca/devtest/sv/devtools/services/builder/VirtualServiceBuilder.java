/**
 * 
 */
package com.ca.devtest.sv.devtools.services.builder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ca.devtest.sv.devtools.VirtualServiceEnvironment;
import com.ca.devtest.sv.devtools.annotation.VirtualServiceType;
import com.ca.devtest.sv.devtools.protocol.DataProtocolDefinition;
import com.ca.devtest.sv.devtools.protocol.TransportProtocolDefinition;
import com.ca.devtest.sv.devtools.protocol.builder.ParamatrizedBuilder;
import com.ca.devtest.sv.devtools.protocol.builder.TransportProtocolBuilderImpl;
import com.ca.devtest.sv.devtools.services.VirtualService;
import com.ca.devtest.sv.devtools.type.TransportProtocolType;
import com.ca.devtest.sv.devtools.utils.VelocityRender;

/**
 * @author gaspa03
 *
 */
public abstract class VirtualServiceBuilder implements ParamatrizedBuilder {

	private VirtualServiceEnvironment vse;
	private String serviceName;
	private TransportProtocolDefinition transportProtocol;
	private final String DEFAULT_SERVICE_PROPERTIES_TPL = "<?xml version=\"1.0\" ?><recording><name>${virtualService.deployedName}</name><binary>false</binary><group>$virtualService.group</group></recording>";
	private final Map<String, String> parameters = new HashMap<String, String>();
	private VirtualServiceType type = VirtualServiceType.RRPAIRS;

	public VirtualServiceBuilder(String name, VirtualServiceEnvironment vse) {
		super();
		this.serviceName = name;
		this.vse = vse;
		transportProtocol = new TransportProtocolBuilderImpl(TransportProtocolType.HTTP.getType())
				.addParameter("listenPort", "8080").addParameter("basePath", "/").build();
	}

	/**
	 * @param transportProtocol
	 * @return
	 */
	public VirtualServiceBuilder overHttp(int port, String basePath) {

		this.transportProtocol = new TransportProtocolBuilderImpl(TransportProtocolType.HTTP.getType())
				.addParameter("listenPort", String.valueOf(port)).addParameter("basePath", basePath).build();
		return this;
	}

	/**
	 * @param transportProtocol
	 * @return
	 */
	public VirtualServiceBuilder over(TransportProtocolDefinition transportProtocol) {

		this.transportProtocol = transportProtocol;
		return this;
	}
	
	/**
	 * @return
	 */
	public String getUserName(){
		return getVse().getUserName();
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public final VirtualService build() throws IOException {
		VirtualService virtualService = new VirtualService(getServiceName(), getType(), getVse());
		virtualService.setDeployedName(getDeployedName());
		virtualService.setPackedVirtualService(packVirtualService());
		return virtualService;
	}

	/**
	 * @return the type
	 */
	public final VirtualServiceType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public final void setType(VirtualServiceType type) {
		this.type = type;
	}

	/**
	 * @return the vse
	 */
	protected final VirtualServiceEnvironment getVse() {
		return vse;
	}

	/**
	 * @return the transportProtocol
	 */
	protected final TransportProtocolDefinition getTransportProtocol() {
		return transportProtocol;
	}

	/**
	 * @return the serviceName
	 */
	public final String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the group
	 */
	public final String getGroup() {
		return getVse().getGroup();
	}

	/**
	 * @return the serviceName
	 */
	public final String getDeployedName() {
		return getGroup() + "." + getServiceName();
	}

	/**
	 * @return
	 */
	protected String generateServicePropertiesContent() {

		Map<String, Object> config = new HashMap<String, Object>();
		config.put("virtualService", this);
		return VelocityRender.render(DEFAULT_SERVICE_PROPERTIES_TPL, config);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected abstract File packVirtualService() throws IOException;

	/**
	 * @return
	 */
	protected String generateVrsContent() {

		return null != transportProtocol ? transportProtocol.toVrsContent() : "";
	}

	/**
	 * @param dataProtocol
	 * @return
	 */
	public VirtualServiceBuilder addRequestDataProtocol(DataProtocolDefinition dataProtocol) {

		transportProtocol.getRequestSide().add(dataProtocol);
		return this;
	}

	/**
	 * @param dataProtocol
	 * @return
	 */
	public VirtualServiceBuilder addRespondDataProtocol(DataProtocolDefinition dataProtocol) {

		transportProtocol.getResponseSide().add(dataProtocol);
		return this;
	}

	protected Map<String, String> getParameters() {
		return parameters;
	}

	@Override
	public void addKeyValue(String key, String value) {
		parameters.put(key, value);

	}
}
