package mockstock.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockstock.web.TraderManager;

/**
 * This filter ensures, that an admin cannot access a user view. In order to do so
 * it checks the managed bean in session and requests the url.
 * @author Kenny Lienhard
 */
public class AdminFilter implements Filter {

    private FilterConfig filterConfig = null;

    public AdminFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        TraderManager traderManager = (TraderManager) req.getSession().getAttribute("traderManager");

        if (traderManager.getTrader() != null && traderManager.getTrader().getUsrgroup().toString().equals("ADMIN")) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect("/MockStockWebClientTwo/index.xhtml");
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