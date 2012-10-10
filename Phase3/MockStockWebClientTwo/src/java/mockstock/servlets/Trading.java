package mockstock.servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.common.StockProduct;
import mockstock.db.Portfolioproduct;
import mockstock.db.SPHistory;
import mockstock.db.Trader;
import mockstock.db.TraderTransaction;
import mockstock.db.enumeration.TransactionType;
import mockstock.ejb.front.TraderFacadeRemote;
import mockstock.ejb.front.TraderTransactionRemote;
import mockstock.helper.json.GsonWriter;

/**
 *
 * @author felmas
 */
public class Trading extends HttpServlet {

    @EJB(name="TraderFacade")
    private TraderFacadeRemote traderBean;

    @EJB(name="TraderTransaction")
    private TraderTransactionRemote traderTransactionBean;

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
            
            String email = (String)session.getAttribute("email");
            
            /* If you close session open this parameter */
            //String email = request.getParameter("email");
            
            // TODO: make shorter action names
            String action = request.getParameter("action");
            if("getTransactions".equals(action)) {
                try {
                    json.writeTransactions(traderTransactionBean
                        .getSortedTransactionListRemote(email));
                } catch(Exception e) {
                    json.writeStringResponse("error", "Transactions couldn't be retrieved");
                }
            } else if("getAllTransactions".equals(action)) {
                try {
                    json.writeTransactions(traderTransactionBean
                        .getSortedTransactionListAllRemote(email));
                } catch(Exception e) {
                    json.writeStringResponse("error", "All transactions couldn't be retrieved");
                }
            } else if("getPortfolioProduct".equals(action)) {
                try {    
                    json.writePortfolioProduct(traderTransactionBean
                        .getPortfolioproductRemote(request.getParameter("stock"), email));
                } catch(Exception e) {
                    json.writeStringResponse("error", "Portfolio product couldn't be retrieved");
                }
            } else if("getPortfolioProducts".equals(action)) {
                try {
                    json.writePortfolioProducts(traderTransactionBean
                        .getPortfolioproductsRemote(email));
                } catch(Exception e) {
                    json.writeStringResponse("error", "Portfolio product list couldn't be retrieved");
                }
            } else if("isProductInPortfolio".equals(action)) {
                try {
                    json.writeBoolResponse("isProductExist", traderTransactionBean
                        .productExistsInPortfolioRemote(request.getParameter("stock"), email));
                } catch(Exception e) {
                    json.writeStringResponse("error", "Portfolio product couldn't be checked in portfolio");
                }
            } else if("getStockProduct".equals(action)) {
                try {
                    json.writeStockProduct(traderTransactionBean
                        .getStockproductRemote(request.getParameter("stock")));
                } catch(Exception e) {
                    json.writeStringResponse("error", "Stock product couldn't be retrieved");
                }
            } else if("getStockProducts".equals(action)) {
                try {
                    json.writeStockProducts(traderTransactionBean
                        .getAllStockProductsRemote());
                } catch(Exception e) {
                    json.writeStringResponse("error", "Stock products couldn't be retrieved");
                }
            } else if("getStockDiffs".equals(action)) {
                try {
                    json.writeStockDiffs(traderTransactionBean
                        .getAllStockDifferencesRemote());
                } catch(Exception e) {
                    json.writeStringResponse("error", "Stock product price differences couldn't be retrieved");
                }
            } else if("getSPHistory".equals(action)) {
                try {
                    json.writeSPHistory(traderTransactionBean
                        .findLatestSPHistoryForSPRemote(request.getParameter("stock")));
                } catch(Exception e) {
                    json.writeStringResponse("error", "Stock product history couldn't be retrieved");
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

            HttpSession session = request.getSession(false);
            if(session == null) {
                json.writeStringResponse("error", "No valid session, please login first");
                return;
            }
            
            String email = (String)session.getAttribute("email");
            String password = (String)session.getAttribute("password");
            
            /* If you close session, open these parameters */
            //String email = request.getParameter("email");
            //String password = request.getParameter("password");

            // TODO: make shorter action names
            String action = request.getParameter("action");
            if("createTransaction".equals(action)) {
                try {
                    Trader trader = traderBean.checkLoginRemote(email, password);
                    
                    String stockName = request.getParameter("stock");
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    SPHistory history = traderTransactionBean.findLatestSPHistoryForSPRemote(stockName);
                    
                    if("BUY".equals(request.getParameter("type"))) {
                        
                        try {
                            createTransaction(trader, quantity, TransactionType.BUY, history, stockName);
                        
                            if(traderTransactionBean.productExistsInPortfolioRemote(stockName, email)) {
                                for(Portfolioproduct p : traderTransactionBean.getPortfolioproductsRemote(email)) {
                                    if(p.getStockproduct().getName().equals(stockName)) {
                                        p.setPrice(history.getPrice());
                                        p.setQuantity(p.getQuantity() + quantity);
                                        traderTransactionBean.updatePortfolioproductRemote(p);
                                    }
                                }
                            } else {
                                Portfolioproduct p = new Portfolioproduct();
                                p.setPrice(history.getPrice());
                                p.setQuantity(quantity);
                                p.setStockproduct(traderTransactionBean.getStockproductRemote(stockName));
                                p.setTrader(trader);
                                traderTransactionBean.createPortfolioproductRemote(p);
                            }
                        
                            List<Portfolioproduct> portfolio = traderTransactionBean.getPortfolioproductsRemote(email);
                            trader.setPortfolioproducts(portfolio);
                            double totalResult = getTotalResult(portfolio);

                            json.writeTransactionResult(totalResult, portfolio);
                        
                        } catch(Exception e) {
                            json.writeStringResponse("error", "An exception is occurred and stock couldn't be bought");
                        }
                    } else {
                        try {
                            if(traderTransactionBean.productExistsInPortfolioRemote(stockName, email)) {
                                Portfolioproduct p = traderTransactionBean.getPortfolioproductRemote(stockName, email);
                                if(p.getQuantity() >= quantity) {
                                    createTransaction(trader, quantity, TransactionType.SELL, history, stockName);

                                    p.setQuantity(p.getQuantity() - quantity);
                                    double adjust = ((quantity * history.getPrice()) - (quantity * p.getPrice()));
                                    p.setStockresult(p.getStockresult() + Math.round(adjust));
                                    p.setPrice(history.getPrice());
                                    traderTransactionBean.updatePortfolioproductRemote(p);

                                    List<Portfolioproduct> portfolio = traderTransactionBean.getPortfolioproductsRemote(email);
                                    trader.setPortfolioproducts(portfolio);
                                    double totalResult = getTotalResult(portfolio);

                                    json.writeTransactionResult(totalResult, portfolio);
                                } else {
                                    json.writeStringResponse("error", "Oh no, you can't sell more than you own");
                                }
                            } else {
                                json.writeStringResponse("error", "Oh no, you don't have any stocks to sell");
                            }
                        } catch(Exception e) {
                            json.writeStringResponse("error", "An exception is occurred and stock couldn't be sold");
                        }
                    }
                } catch(Exception e) {
                    json.writeStringResponse("error", "Something has gone wrong, we couldn't even get your portfolio");
                } 

            } else if("updateSettings".equals(action)) {
                try {
                    String newEmail = request.getParameter("newEmail");
                    if(newEmail == null || newEmail.isEmpty()) {
                        newEmail = email;
                    } else if(!newEmail.equals(email) && traderBean.verifyEmailRemote(newEmail)) {
                        json.writeStringResponse("error", "Chosen email is already in use");
                        return;
                    } 
                   
                    Trader trader = traderBean.checkLoginRemote(email, password);
                    trader.setEmail(newEmail);

                    String newPassword = request.getParameter("newPassword");
                    if(newPassword == null || newPassword.length() < 32) newPassword = password;
                    
                    trader.setPassword(newPassword);

                    trader.setFirstname(request.getParameter("firstName"));
                    trader.setLastname(request.getParameter("lastName"));

                    json.writeTrader(traderTransactionBean.updateSettingsRemote(trader));
                    
                    session.setAttribute("email", newEmail);
                    session.setAttribute("password", newPassword);
                } catch (Exception e) {
                    json.writeStringResponse("error", "An exception is ocurred and settings couldn't be updated");
                }
            } else if("logout".equals(action)) {
                try {
                    session.invalidate();
                    json.writeStringResponse("success", "You successfully log out");
                } catch(Exception e) {
                    json.writeStringResponse("error", "Logout couldn't be completed");
                }
            }else {
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
        return "Trading Servlet";
    }
    
    private StockProduct getStockProduct(String stockName) {
        HashMap<Integer, StockProduct> stocks = traderTransactionBean.getAllStockProductsRemote();
        for(int id : stocks.keySet()) {
            if(stocks.get(id).getStockName().equals(stockName)) return stocks.get(id); 
        }
        return null;
    }
    
    private void createTransaction(Trader trader, int quantity, 
            TransactionType type, SPHistory history, String stockName) {
        
        TraderTransaction transaction = new TraderTransaction();
        transaction.setTrader(trader);
        transaction.setQuantitiy(quantity);
        transaction.setActiondate(new Timestamp(System.currentTimeMillis()));
        transaction.setType(type);
        transaction.setSphistory(history);
        traderTransactionBean.createTransactionRemote(transaction, getStockProduct(stockName));
    }
    
    public double getTotalResult(List<Portfolioproduct> portfolio) {
        double result = 0;
        
        for(Portfolioproduct p : portfolio) {
            result += p.getStockresult();
        }
        
        return result;
    }
}
