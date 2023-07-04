package leejoongseok.wms.common.user;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class UserActionRequestWrapperFilter implements jakarta.servlet.Filter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final UserActionRequestWrapper wrapper = new UserActionRequestWrapper((HttpServletRequest) request);
        chain.doFilter(wrapper, response);
    }
}
