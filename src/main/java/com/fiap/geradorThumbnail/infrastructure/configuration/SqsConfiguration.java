// package com.fiap.geradorThumbnail.infrastructure.configuration;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
// import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.sqs.SqsAsyncClient;

// import java.net.URI;
// import java.util.Optional;

// @Configuration
// public class SqsConfiguration {

//     @Bean
//     public SqsAsyncClient sqsAsyncClient(
//             @Value("${AWS_REGION}") String region,
//             @Value("${AWS_ACCESS_KEY_ID}") String accessKey,
//             @Value("${AWS_SECRET_ACCESS_KEY}") String secretKey
//     ) {
//         return SqsAsyncClient.builder()
//                 .region(Region.of(region))
//                 .credentialsProvider(
//                         StaticCredentialsProvider.create(
//                                 AwsBasicCredentials.create(accessKey, secretKey)
//                         )
//                 )
//                 .build();
//     } 

// }

package com.fiap.geradorThumbnail.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfiguration {

    @Bean
    public SqsAsyncClient sqsAsyncClient(
            @Value("${AWS_REGION}") String region
    ) {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create()) // <- usa IRSA
                .build();
    }

}