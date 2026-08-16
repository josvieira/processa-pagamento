package com.processo.pagamento.demo.config.sqs

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName
import java.net.URI

@Configuration
class SqsConfig(
    @Value("\${aws.sqs.endpoint}") private val endpoint: String,
    @Value("\${aws.sqs.region}") private val region: String
) {

    @Bean
    fun sqsClient(): SqsClient =
        SqsClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            // LocalStack não valida credenciais reais, mas o SDK exige que algo seja informado
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test")
                )
            )
            .build()

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient =
        SqsAsyncClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("test", "test")
                )
            )
            .build()

    // Factory usada pelos métodos anotados com @SqsListener quando nenhum `factory` é
    // informado explicitamente. Precisa pedir os MessageSystemAttributes (MESSAGE_GROUP_ID,
    // MESSAGE_DEDUPLICATION_ID etc.) explicitamente, pois por padrão o container não os busca.
    @Bean
    fun defaultSqsListenerContainerFactory(sqsAsyncClient: SqsAsyncClient): SqsMessageListenerContainerFactory<Any> =
        SqsMessageListenerContainerFactory.builder<Any>()
            .sqsAsyncClient(sqsAsyncClient)
            .configure { options ->
                options.messageSystemAttributeNames(listOf(MessageSystemAttributeName.ALL))
            }
            .build()
}
