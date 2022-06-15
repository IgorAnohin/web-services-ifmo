package ru.anokhin.juddi.integration

import org.apache.juddi.v3.client.UDDIConstants
import org.apache.juddi.v3.client.config.UDDIClient
import org.uddi.api_v3.AuthToken
import org.uddi.api_v3.BusinessInfo
import org.uddi.api_v3.BusinessInfos
import org.uddi.api_v3.BusinessList
import org.uddi.api_v3.BusinessService
import org.uddi.api_v3.Description
import org.uddi.api_v3.DiscardAuthToken
import org.uddi.api_v3.FindBusiness
import org.uddi.api_v3.FindQualifiers
import org.uddi.api_v3.GetAuthToken
import org.uddi.api_v3.GetServiceDetail
import org.uddi.api_v3.Name
import org.uddi.api_v3.ServiceInfo
import org.uddi.api_v3.ServiceInfos
import org.uddi.v3_service.UDDIInquiryPortType
import org.uddi.v3_service.UDDISecurityPortType
import ru.anokhin.juddi.exception.UddiException
import ru.anokhin.juddi.model.AccessPointDto
import ru.anokhin.juddi.model.BindingTemplateDto
import ru.anokhin.juddi.model.BusinessServiceDto

class ServiceBrowser {

    private lateinit var security: UDDISecurityPortType

    private lateinit var inquiry: UDDIInquiryPortType

    /**
     * This sets up the ws proxies using uddi.xml in META-INF
     */
    init {
        // create a manager and read the config in the archive;
        // you can use your config file name
        val client = UDDIClient("META-INF/uddi.xml")

        // a UDDIClient can be a client to multiple UDDI nodes, so
        // supply the nodeName (defined in your uddi.xml.
        // The transport can be WS, inVM etc which is defined in the uddi.xml
        val transport = client.getTransport("default")

        // Now you create a reference to the UDDI API
        this.security = transport.uddiSecurityService
        this.inquiry = transport.uddiInquiryService
    }

    fun findBusinessServiceByName(name: String): List<BusinessServiceDto> = buildList {
        val token = buildAuthToken("uddiadmin", "da_password1")
        try {
            val businessList: BusinessList = findAllBusiness(token)
            val businessServices: List<BusinessService> = findBusinessServices(businessList, token)

            for (businessService: BusinessService in businessServices) {
                if (name in joinNamesToString(businessService.name)) {
                    add(toBusinessServiceDto(businessService))
                }
            }
        } catch (ex: Exception) {
            throw UddiException(ex)
        } finally {
            security.discardAuthToken(DiscardAuthToken(token))
        }
    }

    @Throws(Exception::class)
    private fun findBusinessServices(
        businessList: BusinessList,
        token: String?,
    ): List<BusinessService> = buildList {
        val businessInfos: BusinessInfos = businessList.businessInfos

        for (businessInfo: BusinessInfo in businessInfos.businessInfo) {
            val servicesInfos: ServiceInfos = businessInfo.serviceInfos ?: continue
            val serviceInfoList: List<ServiceInfo> = servicesInfos.serviceInfo.takeIf(List<*>::isNotEmpty) ?: continue

            val serviceDetail = inquiry.getServiceDetail(
                GetServiceDetail().apply {
                    this.authInfo = token
                    this.serviceKey.addAll(serviceInfoList.map(ServiceInfo::getServiceKey))
                }
            )
            addAll(serviceDetail.businessService)
        }
    }

    private fun toBusinessServiceDto(service: BusinessService): BusinessServiceDto =
        BusinessServiceDto(
            name = joinNamesToString(service.name),
            description = joinDescriptionsToString(service.description),
            serviceKey = service.serviceKey,
            bindingTemplates = service.bindingTemplates?.bindingTemplate?.map { bindingTemplate ->
                val accessPoint = bindingTemplate.accessPoint
                BindingTemplateDto(
                    accessPoint = AccessPointDto(
                        value = accessPoint.value,
                        useType = accessPoint.useType
                    )
                )
            }.orEmpty()
        )

    /**
     * Find all the registered businesses. This list may be filtered
     * based on access control rules
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun findAllBusiness(token: String?): BusinessList =
        inquiry.findBusiness(
            FindBusiness().apply {
                this.authInfo = token
                this.findQualifiers = FindQualifiers().apply {
                    this.findQualifier.add(UDDIConstants.APPROXIMATE_MATCH)
                }
                this.name.add(
                    Name(
                        /* value = */ UDDIConstants.WILDCARD,
                        /* lang = */ null
                    )
                )
            }
        )

    /**
     * Gets a UDDI style auth token, otherwise, appends credentials to the
     * ws proxies (not yet implemented)
     *
     * @param username
     * @param password
     * @return
     */
    private fun buildAuthToken(username: String, password: String): String? {
        // Making API call that retrieves the authentication token for the user.
        val rootAuthToken: AuthToken = security.getAuthToken(
            GetAuthToken(
                /* username = */ username,
                /* password = */ password
            )
        )
        return rootAuthToken.authInfo
    }

    private fun joinNamesToString(names: List<Name>): String =
        names.joinToString(separator = " ", transform = Name::getValue)

    private fun joinDescriptionsToString(descriptions: List<Description>): String =
        descriptions.joinToString(separator = " ", transform = Description::getValue)
}
