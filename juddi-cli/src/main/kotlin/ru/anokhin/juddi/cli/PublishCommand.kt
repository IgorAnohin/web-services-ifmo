package ru.anokhin.juddi.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import ru.anokhin.juddi.integration.ServicePublisher

class PublishCommand : CliktCommand(name = "publish", help = "Publish a new service") {

    private val businessKey: String by option(
        help = "This is the UDDI v3 key of the business that should own the service." +
            " (The business should exist in the Registry at time of registration.) "
    ).required()

    private val serviceName: String by option(
        help = "This is the name of the service." +
            " By default, the clerk will use the one name specified in the WebService annotation"
    ).required()

    private val wsdlUrl: String by option(help = "URL of service WSDL").required()

    override fun run() {
        val registered = ServicePublisher().publish(businessKey, serviceName, wsdlUrl)
        if (registered) {
            echo("Successfully registered service [$serviceName] within business [$businessKey]")
        } else {
            echo("Could not register service [$serviceName] within business [$businessKey]")
        }
    }
}
