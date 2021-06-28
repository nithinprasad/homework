package com.prasad.nithin.luminorgroup.filter;

import com.prasad.nithin.luminorgroup.rest.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class CustomLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        logClientGeo(req);
         filterChain.doFilter(servletRequest, servletResponse);
    }

    private void logClientGeo(HttpServletRequest req){
        String ipAddress= Optional.ofNullable(req.getHeader("X-FORWARDED-FOR"))
                .filter(String::isEmpty)
                .orElse(req.getRemoteAddr());

        CompletableFuture.supplyAsync(()->RestClient.GeoIpRestClient.getGeoLocation(ipAddress)).
                thenAccept(this::logClientGeo);

    }

    private void logClientGeo(ResponseEntity<Map> responseEntity){
        log.info("Response status{}",responseEntity);
        log.info("Geo Details{}",responseEntity.getBody());
    }
}
