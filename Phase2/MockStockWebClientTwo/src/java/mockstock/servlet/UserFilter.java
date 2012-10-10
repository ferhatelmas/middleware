package mockstock.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockstock.web.TraderManager;

/**
 * This filter ensures, that a user cannot access an admin view. In order to do so
 * it checks the managed bean in session and requests the url
 * @author Kenny Lienhard
 */
public class UserFilter implements Filter {
    
    private FilterConfig filterConfig = null;

    public UserFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        TraderManager traderManager = (TraderManager) req.getSession().getAttribute("traderManager");

            if (traderManager.getTrader() != null && traderManager.getTrader().getUsrgroup().toString().equals("USER")) {
                chain.doFilter(request, response);
            }
         else {
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
