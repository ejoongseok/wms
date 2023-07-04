package leejoongseok.wms.common.user;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserActionRequestWrapperFilterConfig {

    @Bean
    public FilterRegistrationBean<UserActionRequestWrapperFilter> userActionRequestWrapperFilter() {
        final FilterRegistrationBean<UserActionRequestWrapperFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserActionRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
