enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "web-services-technologies-anokhin"

include(":jax-ws-service")
include(":jax-ws-service:client")
include(":jax-ws-service:api-model")
include(":jax-ws-service:api")

include(":jax-ws-service:bundles:cli")
include(":jax-ws-service:bundles:j2ee")
include(":jax-ws-service:bundles:standalone")

include(":service-core:core")
include(":service-core:core-model")
