package ru.anokhin.juddi.integration

import org.apache.juddi.api_v3.AccessPointType
import org.apache.juddi.v3.client.config.UDDIClerk
import org.apache.juddi.v3.client.config.UDDIClient
import org.uddi.api_v3.AccessPoint
import org.uddi.api_v3.BindingTemplate
import org.uddi.api_v3.BindingTemplates
import org.uddi.api_v3.BusinessEntity
import org.uddi.api_v3.BusinessService
import org.uddi.api_v3.Name
import ru.anokhin.juddi.exception.UddiException

/**
 * This shows you to interact with a UDDI server by publishing a Business,
 * Service and Binding Template. It uses code that is specific to the jUDDI
 * client jar's and represents an easier, simpler way to do things. (UDDIClient
 * and UDDIClerk classes). Credentials and URLs are all set via uddi.xml
 */
class ServicePublisher {

    private val clerk: UDDIClerk = run {
        UDDIClient("META-INF/uddi.xml")
            .getClerk("default")
            ?: throw Exception("The clerk wasn't found, check the config file!")
    }

    fun publish(
        businessKey: String,
        wsdlAddress: String,
        serviceName: String,
    ): Boolean = try {
        val businessEntity: BusinessEntity = clerk.getBusinessDetail(businessKey)
            ?: throw UddiException("Could not find business by key [$businessKey]")

        val businessService: BusinessService = BusinessService().apply {
            this.businessKey = businessKey
            this.name += Name(
                /* value = */ serviceName,
                /* lang = */ null
            )
        }

        val bindingTemplates = BindingTemplates().apply {
            this.bindingTemplate += BindingTemplate().apply {
                this.accessPoint = AccessPoint(
                    /* value = */ wsdlAddress,
                    /* useType = */ AccessPointType.WSDL_DEPLOYMENT.toString(),
                )
            }.let(UDDIClient::addSOAPtModels)
        }
        businessService.bindingTemplates = bindingTemplates

        val registeredBusinessService: BusinessService? = clerk.register(businessService)
        (registeredBusinessService != null)
    } catch (ex: Exception) {
        throw UddiException(ex)
    } finally {
        clerk.discardAuthToken()
    }
}
