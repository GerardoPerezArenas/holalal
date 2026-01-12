/**
 * RGIServicesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package rgi.lanbide.net;

public class RGIServicesServiceLocator extends org.apache.axis.client.Service implements rgi.lanbide.net.RGIServicesService {

    public RGIServicesServiceLocator() {
    }


    public RGIServicesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RGIServicesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RGIServicesPort
    private java.lang.String RGIServicesPort_address = "http://pavdeslan.lanbide.lan:7003/rgi/services/RGIServices";

    public java.lang.String getRGIServicesPortAddress() {
        return RGIServicesPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RGIServicesPortWSDDServiceName = "RGIServicesPort";

    public java.lang.String getRGIServicesPortWSDDServiceName() {
        return RGIServicesPortWSDDServiceName;
    }

    public void setRGIServicesPortWSDDServiceName(java.lang.String name) {
        RGIServicesPortWSDDServiceName = name;
    }

    public rgi.lanbide.net.RGIServices getRGIServicesPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RGIServicesPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRGIServicesPort(endpoint);
    }

    public rgi.lanbide.net.RGIServices getRGIServicesPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            rgi.lanbide.net.RGIServicesPortSoapBindingStub _stub = new rgi.lanbide.net.RGIServicesPortSoapBindingStub(portAddress, this);
            _stub.setPortName(getRGIServicesPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRGIServicesPortEndpointAddress(java.lang.String address) {
        RGIServicesPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (rgi.lanbide.net.RGIServices.class.isAssignableFrom(serviceEndpointInterface)) {
                rgi.lanbide.net.RGIServicesPortSoapBindingStub _stub = new rgi.lanbide.net.RGIServicesPortSoapBindingStub(new java.net.URL(RGIServicesPort_address), this);
                _stub.setPortName(getRGIServicesPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RGIServicesPort".equals(inputPortName)) {
            return getRGIServicesPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("net.lanbide.rgi", "RGIServicesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("net.lanbide.rgi", "RGIServicesPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RGIServicesPort".equals(portName)) {
            setRGIServicesPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
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
