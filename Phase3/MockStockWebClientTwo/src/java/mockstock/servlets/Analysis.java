package mockstock.servlets;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mockstock.ejb.front.AnalyticsBeanRemote;
import mockstock.helper.json.GsonWriter;

/**
 *
 * @author felmas
 */
public class Analysis extends HttpServlet {

    @EJB(name="Analytics")
    private AnalyticsBeanRemote analyticsBean;

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        GsonWriter json = new GsonWriter(response.getWriter());
        
        try {
            HttpSession session = request.getSession(true);
            if(session == null) {
                json.writeStringResponse("error", "No valid session");
                return;
            }
            
            // TODO: make shorter action names
            String action = request.getParameter("action");
            if("getStockEvolution".equals(action)) {
                try {
                    json.writeSPHistorys(analyticsBean.getStockEvolutionRemote(request.getParameter("stock")));
                } catch(Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and stock history couldn't be retrieved");
                }
            } else {
                json.writeStringResponse("error", "No valid action");
            }
        } finally {
            response.getWriter().close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Analytics Servlet";
    }
}
