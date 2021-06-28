package com.prasad.nithin.luminorgroup.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Component
public class CustomeRequestLoggingFilter extends CommonsRequestLoggingFilter {

    public CustomeRequestLoggingFilter(){
       super.setMaxPayloadLength(2000);
       super.setIncludePayload(true);
       super.setIncludeQueryString(true);
       super.setIncludeHeaders(true);
   }

   @Bean
   public CommonsRequestLoggingFilter requestLoggingFilter() {
      CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
      loggingFilter.setIncludeClientInfo(true);
      loggingFilter.setIncludeQueryString(true);
      loggingFilter.setIncludePayload(true);
      loggingFilter.setIncludeHeaders(true);
      loggingFilter.setMaxPayloadLength(2000);
      return loggingFilter;
   }
}
