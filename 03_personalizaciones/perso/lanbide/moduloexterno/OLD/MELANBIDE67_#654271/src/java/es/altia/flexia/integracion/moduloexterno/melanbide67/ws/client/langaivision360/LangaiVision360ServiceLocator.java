/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.langaivision360;

import org.apache.axis.client.Service;

/**
 *
 * @author pablo.bugia
 */
public class LangaiVision360ServiceLocator extends Service implements LangaiVision360Service {

    public LangaiVision360ServiceLocator() {
    }

    public LangaiVision360ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LangaiVision360ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LangaiVision360Port
    private java.lang.String LangaiVision360Port_address = "http://10.168.212.21:17003/langaiWS_Final/langaiVision360";

    public java.lang.String getLangaiVision360PortAddress() {
        return LangaiVision360Port_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LangaiVision360PortWSDDServiceName = "LangaiVision360Port";

    public java.lang.String getLangaiVision360PortWSDDServiceName() {
        return LangaiVision360PortWSDDServiceName;
    }

    public void setLangaiVision360PortWSDDServiceName(java.lang.String name) {
        LangaiVision360PortWSDDServiceName = name;
    }

    public LangaiVision360 getLangaiVision360Port() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LangaiVision360Port_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLangaiVision360Port(endpoint);
    }

    public LangaiVision360 getLangaiVision360Port(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            LangaiVision360PortBindingStub _stub = new LangaiVision360PortBindingStub(portAddress, this);
            _stub.setPortName(getLangaiVision360PortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLangaiVision360PortEndpointAddress(java.lang.String address) {
        LangaiVision360Port_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (LangaiVision360.class.isAssignableFrom(serviceEndpointInterface)) {
                LangaiVision360PortBindingStub _stub = new LangaiVision360PortBindingStub(new java.net.URL(LangaiVision360Port_address), this);
                _stub.setPortName(getLangaiVision360PortWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("LangaiVision360Port".equals(inputPortName)) {
            return getLangaiVision360Port();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://langaiVision360.services.langaiWS.es/", "LangaiVision360Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://langaiVision360.services.langaiWS.es/", "LangaiVision360Port"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("LangaiVision360Port".equals(portName)) {
            setLangaiVision360PortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
