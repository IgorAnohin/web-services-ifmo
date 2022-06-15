package ru.anokhin.juddi.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import ru.anokhin.juddi.integration.ServiceBrowser
import ru.anokhin.juddi.model.BusinessServiceDto

class BrowseCommand : CliktCommand(name = "browse", help = "Find service by name") {

    private val serviceName: String by argument(help = "Name of service")

    override fun run() {
        val businessServices: List<BusinessServiceDto> = ServiceBrowser().findBusinessServiceByName(serviceName)
        when {
            businessServices.isEmpty() -> echo("Could not find any business service by name [$serviceName]")
            (businessServices.size == 1) -> {
                echo("Found a single service by name [$serviceName]:")
                echo(businessServices.first())
            }
            else -> {
                echo("Found a [${businessServices.size}] services by name [$serviceName]")
                businessServices.forEach(::echo)
            }
        }
    }
}
