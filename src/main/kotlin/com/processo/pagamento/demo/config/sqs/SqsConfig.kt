package com.processo.pagamento.demo.config.sqs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
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
}
