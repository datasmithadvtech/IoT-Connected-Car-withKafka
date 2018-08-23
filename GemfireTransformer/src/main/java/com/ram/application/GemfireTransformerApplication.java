package com.ram.application;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import com.ram.gemfire.transformer.JsonTransformer;
import com.ram.repository.CarInfoRepository;
import com.ram.domain.CarInfo;

@SpringBootApplication
@ClientCacheApplication(name = "AccessingGemFire", logLevel = "error")
@EnableEntityDefinedRegions(basePackageClasses = CarInfo.class,
        clientRegionShortcut = ClientRegionShortcut.LOCAL)
@EnableGemfireRepositories(basePackageClasses = CarInfoRepository.class)

public class GemfireTransformerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GemfireTransformerApplication.class, args);
    }

    // passing carRepository refrence to json transformer to save the json object into gemfire' region
    @Bean
    ApplicationRunner run(CarInfoRepository carRepository) {
        JsonTransformer transformer = new JsonTransformer();
        return args -> {
            transformer.run(carRepository);
        };
    }

}
