package mockstock.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mockstock.db.Trader;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;
import mockstock.ejb.front.AdminRemote;
import mockstock.ejb.front.TraderFacadeRemote;
import mockstock.ejb.front.TraderTransactionRemote;
import mockstock.helper.json.GsonWriter;

/**
 *
 * @author felmas
 */
public class Admin extends HttpServlet {

    @EJB(name="Admin")
    private AdminRemote adminBean;
    
    @EJB(name="TraderFacade")
    private TraderFacadeRemote traderBean;
    
    @EJB(name="TraderTransaction")
    private TraderTransactionRemote traderTransactionBean;
    
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // nothing 
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
        
        GsonWriter json = new GsonWriter(response.getWriter());
        
        try {
            
            HttpSession session = request.getSession(false);
            if(session == null) {
                json.writeStringResponse("error", "No valid session, please login first");
                return;
            }
            
            if((UserRight)session.getAttribute("userRight") != UserRight.ADMIN) {
                json.writeStringResponse("error", "You aren't admin and don't have the permission");
                return;
            }
             
            // TODO: make shorter action names
            String action = request.getParameter("action");
            if("getTraders".equals(action)) {
                try {
                    json.writeTraders(sort(adminBean
                        .getTraderListRemote()));
                } catch(Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and traders couldn't be loaded");
                }
            } else if("getTrader".equals(action)) {
                try {
                    json.writeTrader(adminBean
                        .getTraderRemote(Long.parseLong(request.getParameter("id"))));
                } catch(Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and trader couldn't be loaded");
                }
            } else {
                json.writeStringResponse("error", "No valid action");
            }
        } catch(Exception e) {
            json.writeStringResponse("error", " Session couldn't be retrieved");
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
            
            HttpSession session = request.getSession(true);
            if(session == null) {
                json.writeStringResponse("error", "No valid session, please login first");
                return;
            }
            
            if((UserRight)session.getAttribute("userRight") != UserRight.ADMIN) {
                json.writeStringResponse("error", "You aren't admin and don't have the permission");
                return;
            }
            
            // TODO: make shorter action names
            String action = request.getParameter("action");
            if("updateTrader".equals(action)) {
                try {
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    
                    if(email == null || email.isEmpty() || password == null || password.isEmpty()) {
                        json.writeStringResponse("error", "Email and/or password is empty");
                        return;
                    }
                    
                    String newEmail = request.getParameter("newEmail");
                    if(newEmail == null || newEmail.isEmpty()) {
                        newEmail = email;
                    } else if(!newEmail.equals(email) && traderBean.verifyEmailRemote(newEmail)) {
                        json.writeStringResponse("error", "New email is already in use");
                        return;
                    } 
                    
                    String newPassword = request.getParameter("newPassword");
                    if(newPassword == null || newPassword.isEmpty()) {
                        newPassword = password;
                    } 
                    
                    Trader trader = traderBean.checkLoginRemote(email, password);
                    trader.setEmail(newEmail);
                    trader.setPassword(newPassword);
                    trader.setFirstname(request.getParameter("firstName"));
                    trader.setLastname(request.getParameter("lastName"));

                    trader.setUsrgroup("ADMIN".equals(request.getParameter("usrRight")) ? UserRight.ADMIN : UserRight.USER);

                    String status = request.getParameter("usrStatus");
                    if("DISABLED".equals(status)) trader.setStatus(UserStatus.DISABLED);
                    else if(("NOT_ACTIVE").equals(status)) trader.setStatus(UserStatus.NOT_ACTIVE);
                    else trader.setStatus(UserStatus.ACTIVATED);

                    json.writeTrader(adminBean.updateTraderRemote(trader));
                    
                } catch(Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and trader couldn't be updated");
                } 
            } else if("removeTrader".equals(action)) {
                try {
                    adminBean.removeTraderRemote(traderBean
                        .checkLoginRemote(request.getParameter("email"), request.getParameter("password")));
                    json.writeStringResponse("success", "deleted");
                } catch (Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and trader couldn't be deleted");
                }
            } else if("addTrader".equals(action)) {
                try {
                    String email = request.getParameter("email");
                    if(email == null || email.isEmpty()) {
                        json.writeStringResponse("error", "Email is empty");
                    } else if(traderBean.verifyEmailRemote(email)) {
                        json.writeStringResponse("error", "Email is already in use");
                    } else {
                        Trader trader = new Trader();
                        trader.setEmail(email);
                        trader.setPassword(request.getParameter("password"));
                        trader.setFirstname(request.getParameter("firstName"));
                        trader.setLastname(request.getParameter("lastName"));

                        trader.setUsrgroup("ADMIN".equals(request.getParameter("usrRight")) ? UserRight.ADMIN : UserRight.USER);

                        String status = request.getParameter("usrStatus");
                        if("DISABLED".equals(status)) trader.setStatus(UserStatus.DISABLED);
                        else if(("NOT_ACTIVE").equals(status)) trader.setStatus(UserStatus.NOT_ACTIVE);
                        else trader.setStatus(UserStatus.ACTIVATED);

                        trader = traderBean.addTraderRemote(trader);
                        trader.setPortfolioproducts(traderTransactionBean.getPortfolioproductsRemote(request.getParameter("email")));
                        trader.setTransactions(traderTransactionBean.getSortedTransactionListRemote(request.getParameter("email")));

                        json.writeTrader(trader);
                    }
                } catch (Exception e) {
                    json.writeStringResponse("error", "An exception is occurred and trader couldn't be added");
                }                 
            } else {
                json.writeStringResponse("error", "No valid action");
            }
            
        } catch(Exception e) {
            json.writeStringResponse("error", "Session couldn't be retrieved");
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
        return "Admin Servlet";
    }
    
    private List<Trader> sort(List<Trader> traders) {
        Collections.sort(traders, new Comparator<Trader>() {

            @Override
            public int compare(Trader t1, Trader t2) {
                String f1 = t1.getLastname();
                String f2 = t2.getLastname();
                
                if(f1 == null && f2 != null) return -1;
                else if(f1 == null && f2 == null) return 0;
                else if(f1 != null && f2 == null) return 1;
                else return f1.compareToIgnoreCase(f2);
            }
        });
        
        return traders;
    }
}
