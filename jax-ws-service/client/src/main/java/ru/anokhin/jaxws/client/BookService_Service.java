
package ru.anokhin.jaxws.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "BookService", targetNamespace = "http://impl.service.jaxws.anokhin.ru/", wsdlLocation = "http://localhost:8080/jaxws/BookService?wsdl")
public class BookService_Service
    extends Service
{

    private final static URL BOOKSERVICE_WSDL_LOCATION;
    private final static WebServiceException BOOKSERVICE_EXCEPTION;
    private final static QName BOOKSERVICE_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "BookService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/jaxws/BookService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        BOOKSERVICE_WSDL_LOCATION = url;
        BOOKSERVICE_EXCEPTION = e;
    }

    public BookService_Service() {
        super(__getWsdlLocation(), BOOKSERVICE_QNAME);
    }

    public BookService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), BOOKSERVICE_QNAME, features);
    }

    public BookService_Service(URL wsdlLocation) {
        super(wsdlLocation, BOOKSERVICE_QNAME);
    }

    public BookService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BOOKSERVICE_QNAME, features);
    }

    public BookService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BookService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BookService
     */
    @WebEndpoint(name = "BookServicePort")
    public BookService getBookServicePort() {
        return super.getPort(new QName("http://impl.service.jaxws.anokhin.ru/", "BookServicePort"), BookService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BookService
     */
    @WebEndpoint(name = "BookServicePort")
    public BookService getBookServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.service.jaxws.anokhin.ru/", "BookServicePort"), BookService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BOOKSERVICE_EXCEPTION!= null) {
            throw BOOKSERVICE_EXCEPTION;
        }
        return BOOKSERVICE_WSDL_LOCATION;
    }

}
