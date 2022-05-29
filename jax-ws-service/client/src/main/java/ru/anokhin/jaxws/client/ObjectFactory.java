
package ru.anokhin.jaxws.client;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.anokhin.jaxws.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BookSoapDto_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "bookSoapDto");
    private final static QName _DeleteBookById_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "deleteBookById");
    private final static QName _DeleteBookByIdResponse_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "deleteBookByIdResponse");
    private final static QName _FindByFilter_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "findByFilter");
    private final static QName _FindByFilterResponse_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "findByFilterResponse");
    private final static QName _Create_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "create");
    private final static QName _CreateResponse_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "createResponse");
    private final static QName _Update_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "update");
    private final static QName _UpdateResponse_QNAME = new QName("http://impl.service.jaxws.anokhin.ru/", "updateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.anokhin.jaxws.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteBookById }
     * 
     */
    public DeleteBookById createDeleteBookById() {
        return new DeleteBookById();
    }

    /**
     * Create an instance of {@link BookSoapDto }
     * 
     */
    public BookSoapDto createBookSoapDto() {
        return new BookSoapDto();
    }

    /**
     * Create an instance of {@link DeleteBookByIdResponse }
     * 
     */
    public DeleteBookByIdResponse createDeleteBookByIdResponse() {
        return new DeleteBookByIdResponse();
    }

    /**
     * Create an instance of {@link FindByFilter }
     * 
     */
    public FindByFilter createFindByFilter() {
        return new FindByFilter();
    }

    /**
     * Create an instance of {@link FindByFilterResponse }
     * 
     */
    public FindByFilterResponse createFindByFilterResponse() {
        return new FindByFilterResponse();
    }

    /**
     * Create an instance of {@link Create }
     * 
     */
    public Create createCreate() {
        return new Create();
    }

    /**
     * Create an instance of {@link CreateResponse }
     * 
     */
    public CreateResponse createCreateResponse() {
        return new CreateResponse();
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link UpdateResponse }
     * 
     */
    public UpdateResponse createUpdateResponse() {
        return new UpdateResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookSoapDto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "bookSoapDto")
    public JAXBElement<BookSoapDto> createBookSoapDto(BookSoapDto value) {
        return new JAXBElement<BookSoapDto>(_BookSoapDto_QNAME, BookSoapDto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteBookById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "deleteBookById")
    public JAXBElement<DeleteBookById> createDeleteBookById(DeleteBookById value) {
        return new JAXBElement<DeleteBookById>(_DeleteBookById_QNAME, DeleteBookById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteBookByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "deleteBookByIdResponse")
    public JAXBElement<DeleteBookByIdResponse> createDeleteBookByIdResponse(DeleteBookByIdResponse value) {
        return new JAXBElement<DeleteBookByIdResponse>(_DeleteBookByIdResponse_QNAME, DeleteBookByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "findByFilter")
    public JAXBElement<FindByFilter> createFindByFilter(FindByFilter value) {
        return new JAXBElement<FindByFilter>(_FindByFilter_QNAME, FindByFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindByFilterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "findByFilterResponse")
    public JAXBElement<FindByFilterResponse> createFindByFilterResponse(FindByFilterResponse value) {
        return new JAXBElement<FindByFilterResponse>(_FindByFilterResponse_QNAME, FindByFilterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Create }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "create")
    public JAXBElement<Create> createCreate(Create value) {
        return new JAXBElement<Create>(_Create_QNAME, Create.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "createResponse")
    public JAXBElement<CreateResponse> createCreateResponse(CreateResponse value) {
        return new JAXBElement<CreateResponse>(_CreateResponse_QNAME, CreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Update }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "update")
    public JAXBElement<Update> createUpdate(Update value) {
        return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.service.jaxws.anokhin.ru/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
        return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
    }

}
