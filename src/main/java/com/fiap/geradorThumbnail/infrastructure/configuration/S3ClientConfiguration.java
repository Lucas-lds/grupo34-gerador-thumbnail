// package com.fiap.geradorThumbnail.infrastructure.configuration;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
// import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.s3.S3Client;
// import software.amazon.awssdk.services.s3.S3Configuration;

// import java.net.URI;


// @Configuration
// public class S3ClientConfiguration {

//     @Value("${aws.s3.access-key}")
//     private String accessKey;

//     @Value("${aws.s3.secret-key}")
//     private String secretKey;

//     @Value("${aws.s3.region}")
//     private String region;

//     @Value("${aws.s3.path-style:false}")
//     private boolean pathStyle;

//     @Bean
//     public S3Client s3Client() {
//         return S3Client.builder()
//                 .region(Region.of(region))
//                 .credentialsProvider(
//                         StaticCredentialsProvider.create(
//                                 AwsBasicCredentials.create(accessKey, secretKey)
//                         )
//                 )
//                 .serviceConfiguration(
//                         S3Configuration.builder()
//                                 .pathStyleAccessEnabled(pathStyle)
//                                 .build()
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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3ClientConfiguration {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.path-style:false}")
    private boolean pathStyle;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create()) // <- usa IRSA
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(pathStyle)
                                .build()
                )
                .build();
    }
}