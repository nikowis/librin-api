package pl.nikowis.librin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.util.SecurityUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        long time = System.currentTimeMillis();
        try {
            chain.doFilter(req, resp);
        } finally {
            time = System.currentTimeMillis() - time;
            HttpServletRequest httpReq = (HttpServletRequest) req;
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            String queryString = httpReq.getQueryString() != null ? "?" + httpReq.getQueryString() : "";
            String currentUserId = SecurityUtils.getCurrentUserId() != null ? SecurityUtils.getCurrentUserId().toString() : "anonymous";
            LOGGER.info("Request {} {}  {}ms (user id {}). Response code {}.", httpReq.getMethod(), httpReq.getRequestURI() + queryString, time, currentUserId, httpResp.getStatus());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
    }

    @Override
    public void destroy() {
        //empty
    }
}
