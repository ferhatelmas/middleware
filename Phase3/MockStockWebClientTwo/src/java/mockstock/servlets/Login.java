package mockstock.servlets;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mockstock.bonjour.ServiceAnnouncer;
import mockstock.db.Trader;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;
import mockstock.ejb.front.TraderFacadeRemote;
import mockstock.ejb.front.TraderTransactionRemote;
import mockstock.helper.json.GsonWriter;

/**
 *
 * @author felmas
 */
public class Login extends HttpServlet {

    @EJB(name="TraderFacade")
    private TraderFacadeRemote traderBean; 
    
    @EJB(name="TraderTransaction")
    private TraderTransactionRemote traderTransactionBean;

    ServiceAnnouncer service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        service = new ServiceAnnouncer();
        service.registerService(8080);
    }

    @Override
    public void destroy() {
        service.unregisterService();
    }
            
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // I don't see any improvements in using this method
    }
    
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
        
        String action = request.getParameter("action");
        GsonWriter json = new GsonWriter(response.getWriter());
        
        try {
            
            // TODO: make shorter action names
            if("check".equals(action)) {
                try {
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");

                    if(email == null || email.isEmpty() || password == null || password.isEmpty()) {
                        json.writeStringResponse("error", "No user since email and/or password is empty");
                    } else {
                        Trader trader = traderBean.checkLoginRemote(email, password);
                        if(trader != null && trader.getStatus() == UserStatus.ACTIVATED) {
                            HttpSession session = request.getSession(true);
                            if(session.isNew()) {
                                session.setAttribute("userRight", trader.getUsrgroup());
                                session.setAttribute("email", email);
                                session.setAttribute("password", password);
                            }
                            json.writeTrader(trader);
                        } else if(trader == null){
                            json.writeStringResponse("error", "No user with these credentials");
                        } else {
                            json.writeStringResponse("error", "User status isn't activated");
                        }
                    }
                } catch(Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and login couldn't be checked");
                }
            } else if("verify".equals(action)){
                try {
                    String email = request.getParameter("email");
                    if(email == null) json.writeBoolResponse("isExist", true);
                    else json.writeBoolResponse("isExist", traderBean.verifyEmailRemote(email));
                } catch(Exception e) {
                    json.writeStringResponse("error", "An exception is occured and email couldn't be checked");
                }
            } else {
                json.writeStringResponse("error", "No valid action");
            }
            
        } catch(Exception e) {
            json.writeStringResponse("error", "An exception is occured and we couldn't even get what you want to do");
        } finally {            
            response.getWriter().close();
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        GsonWriter json = new GsonWriter(response.getWriter());
        
        try {
        
            String action = request.getParameter("action");
            if("signup".equals(action)) {
                try {
                    String email = request.getParameter("email");
                    if(email == null || email.isEmpty()) {
                        json.writeStringResponse("error", "Email can not be empty");
                        return;
                    } else if(traderBean.verifyEmailRemote(email)) {
                        json.writeStringResponse("error", "Email is already in use");
                        return;
                    }

                    Trader trader = new Trader();
                    trader.setEmail(email);
                    trader.setPassword(request.getParameter("password"));
                    trader.setFirstname(request.getParameter("firstName"));
                    trader.setLastname(request.getParameter("lastName"));
                    trader.setStatus(UserStatus.ACTIVATED);
                    trader.setUsrgroup(UserRight.USER);

                    trader = traderBean.addTraderRemote(trader);
                    trader.setPortfolioproducts(traderTransactionBean.getPortfolioproductsRemote(request.getParameter("email")));
                    trader.setTransactions(traderTransactionBean.getSortedTransactionListRemote(request.getParameter("email")));

                    HttpSession session = request.getSession(true);
                    if(session.isNew()) {
                        session.setAttribute("userRight", trader.getUsrgroup());
                        session.setAttribute("email", trader.getEmail());
                        session.setAttribute("password", request.getParameter("password"));
                    }

                    json.writeTrader(trader);
                } catch (Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and sign up couldn't be completed");
                }
            } else {
                json.writeStringResponse("error", "No valid action");
            }
        } catch(Exception e) {
            json.writeStringResponse("error", "An exception is occured and we couldn't even get what you want to do");
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
        return "Login Servlet";
    }
}
