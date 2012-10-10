package mockstock.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockstock.web.TraderManager;

/**
 * This class ensures that admin and user view are protected and only 
 * accessible if a trader is logged in
 * @author Kenny Lienhard
 */
public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    public LoginFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        TraderManager traderManager = (TraderManager) req.getSession().getAttribute("traderManager");

        if (!((HttpServletRequest) request).getServletPath().startsWith("/index") && traderManager.getTrader() == null) {
            res.sendRedirect("/MockStockWebClientTwo/index.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
}