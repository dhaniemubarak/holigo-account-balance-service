package id.holigo.services.holigoaccountbalanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@EnableCaching
public class HoligoAccountBalanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HoligoAccountBalanceServiceApplication.class, args);
    }

}