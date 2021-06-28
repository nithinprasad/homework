package com.prasad.nithin.luminorgroup.rest;

import com.prasad.nithin.luminorgroup.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Component
public class RestClient {

    @Autowired
    static RestTemplate  restTemplate=new RestTemplate();

    public  static class SepaRestCLient{

        public static ResponseEntity<String> notifySepaCreate(Payment payment){
            HttpHeaders headers=new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity=new HttpEntity<String>(headers);
            return restTemplate.exchange("https://quoters.apps.pcfone.io/api/random", HttpMethod.GET,entity,String.class);
        }

    }

    public  static class Target2RestClient{

        public static ResponseEntity<String> notifyTarget2Create(Payment payment){
            HttpHeaders headers=new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity=new HttpEntity<String>(headers);
            return restTemplate.exchange("https://quoters.apps.pcfone.io/api/random", HttpMethod.GET,entity,String.class);
        }

    }

    public static class GeoIpRestClient{

        @Async
        public static ResponseEntity<Map> getGeoLocation(String ipaddress){

            HttpHeaders headers=new HttpHeaders();
            String url="https://ipapi.co/"+ipaddress+"/json/";
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity=new HttpEntity<String>(headers);
            return restTemplate.exchange(url, HttpMethod.GET,entity, Map.class);
        }

    }

    @Bean
    public RestTemplate getRestTemplate(){
       return new RestTemplateBuilder().build();
    }

}
