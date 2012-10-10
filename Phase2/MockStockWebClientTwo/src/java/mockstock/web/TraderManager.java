package mockstock.web;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import middleware.common.MSTweet;
import middleware.common.StockProduct;
import mockstock.db.Portfolioproduct;
import mockstock.db.SPHistory;
import mockstock.db.Trader;
import mockstock.db.TraderTransaction;
import mockstock.db.enumeration.TransactionType;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;
import mockstock.ejb.front.TraderFacadeRemote;
import mockstock.ejb.front.TraderTransactionRemote;
import mockstock.helper.PasswordEncrypter;
import twitter4j.*;

/**
 * managed bean for trader, handles all the trader activities
 *
 * @author Kenny Lienhard
 */
@ManagedBean
@SessionScoped
public class TraderManager {

    private HashMap<Integer, StockProduct> stockproducts;
    private HashMap<Integer, String> priceDifferences;
    private int transQtty;
    private int transStockID;
    private double totalResult = 0.0;
    private TransactionType transType;
    private Trader trader;
    private boolean allTransactions;
    private HashMap<String, Integer> portfolioanalyzer = new HashMap<String, Integer>();
    private List<Portfolioproduct> portfolio;
    private List<TraderTransaction> traderHistory;
    private HashMap<Long, MSTweet> mstweets = new HashMap<Long, MSTweet>();
    //edit trader
    private String editedPassword;
    private String editedNewPassword;
    private String editedFirstname;
    private String editedLastname;
    private String editedEmail;
    //login
    private String email;
    private String password;
    //signup
    private String firstname;
    private String lastname;
    private String emailsignup;
    private String pwdsignup;
    @EJB
    private TraderTransactionRemote traderTransaction;
    @EJB
    TraderFacadeRemote traderFacade;

    /**
     * handles the sign in process, put error message if login was not possible.
     * trader is redirected according to his user rights (user or admin)
     *
     * @return
     */
    public String signIn() {
        String navigateTo = null;
        try {
            String hash = PasswordEncrypter.encryptPassword(password);
            this.trader = traderFacade.checkLoginRemote(email, hash);
            if (this.trader == null) {
                addMessage("Username or password is incorrect.");
            } else if (this.trader.getStatus().toString().equals("NOT_ACTIVE") || this.trader.getStatus().toString().equals("DISABLED")) {
                this.trader = null;
                addMessage("This trader is not active, please contact your manager.");
            } else {
                traderHistory = traderTransaction.getSortedTransactionListRemote(this.trader.getEmail());
                portfolio = traderTransaction.getPortfolioproductsRemote(this.trader.getEmail());
                totalResult = getTotalResult();
                if (this.trader.getUsrgroup().toString().equals("ADMIN")) {
                    navigateTo = "startadmin";
                    this.email = null;
                    this.password = null;
                } else {
                    navigateTo = "starttrader";
                    this.email = null;
                    this.password = null;
                }
            }
        } catch (Exception e) {
            addMessage("Login not possible.");
        }
        return navigateTo;
    }

    /**
     * add a new trader, user for sign up on homepage
     *
     * @return navigate to...
     */
    public String addNewTrader() {
        String navigateTo = null;
        try {
            //checks if there is already a user with this email adress
            if (this.verifyIfEmailExists(emailsignup)) {
                addEmailMessage("There is already a trader with this email address.");
            } else {
                Trader t = new Trader();
                t.setEmail(emailsignup);
                t.setFirstname(firstname);
                t.setLastname(lastname);
                t.setPassword(PasswordEncrypter.encryptPassword(pwdsignup));
                //sign up = activated trader
                t.setStatus(UserStatus.ACTIVATED);
                //not possible to do sign up, and to give myself admin rights
                t.setUsrgroup(UserRight.USER);
                this.trader = traderFacade.addTraderRemote(t);
                traderHistory = traderTransaction.getSortedTransactionListRemote(this.trader.getEmail());
                portfolio = traderTransaction.getPortfolioproductsRemote(this.trader.getEmail());
                totalResult = getTotalResult();
                navigateTo = "starttrader";
                this.emailsignup = null;
                this.firstname = null;
                this.lastname = null;
                this.pwdsignup = null;
            }
        } catch (Exception e) {
            addMessage("Trader could not be added.");
        }
        return navigateTo;
    }

    /**
     * gets the latest two (int i defines number of tweets) tweets from
     * financial times. an existing java library is used to do so.
     *
     * @return
     */
    public HashMap<Long, MSTweet> getMstweets() {
        try {
            // The factory instance is re-useable and thread safe.
            Twitter twitter = new TwitterFactory().getInstance();
            twitter4j.Query query = new twitter4j.Query("from:FinancialTimes");
            QueryResult result = twitter.search(query);
            int i = 0;
            for (Tweet tweet : result.getTweets()) {

                if (i == 2) {
                    break;
                }
                MSTweet mstweet = new MSTweet();
                mstweet.setCreatedAt(tweet.getCreatedAt());
                mstweet.setUrl("http://twitter.com/#!/FinancialTimes/status/" + tweet.getId());
                mstweet.setText(tweet.getText());
                this.mstweets.put(new Long(tweet.getId()), mstweet);
                i++;
            }
        } catch (TwitterException ex) {
            System.out.println(ex);
        }
        return mstweets;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    /**
     * do a buy transaction
     */
    public void submitBuyTransaction() {
        this.transType = TransactionType.BUY;
        StockProduct transactionStock = stockproducts.get(transStockID);
        //get latest price
        SPHistory history = traderTransaction.findLatestSPHistoryForSPRemote(this.stockproducts.get(transStockID).getStockName());
        createNewTransction(transactionStock, history);

        String stockname = transactionStock.getStockName();

        Portfolioproduct pp = null;
        try {
            //checks if user bought already stocks from this product
            if (traderTransaction.productExistsInPortfolioRemote(stockname, this.trader.getEmail())) {
                //comment here
                portfolio = traderTransaction.getPortfolioproductsRemote(this.trader.getEmail());
                for (Portfolioproduct portfolioproduct : portfolio) {
                    if (portfolioproduct.getStockproduct().getName().equals(stockname)) {
                        //update existing product
                        pp = portfolioproduct;
                        pp.setPrice(history.getPrice());
                        pp.setQuantity(pp.getQuantity() + transQtty);
                        traderTransaction.updatePortfolioproductRemote(pp);
                        break;
                    }
                }
                //never bought stocks of this product, create a new product
            } else {
                pp = new Portfolioproduct();
                pp.setPrice(history.getPrice());
                pp.setQuantity(transQtty);
                pp.setStockproduct(traderTransaction.getStockproductRemote(stockname));
                pp.setTrader(this.trader);
                traderTransaction.createPortfolioproductRemote(pp);
            }
        } catch (Exception e) {
            //addMessage("Transaction aborted, an error occured.");
            System.out.println(e.getMessage());
        }
        portfolio = traderTransaction.getPortfolioproductsRemote(this.trader.getEmail());

        this.trader.setPortfolioproducts(portfolio);
        totalResult = getTotalResult();
        this.transQtty = 0;
    }

    /**
     * do sell transaction
     */
    public void submitSellTransaction() {
        this.transType = TransactionType.SELL;
        StockProduct transactionStock = stockproducts.get(transStockID);
        String stockname = transactionStock.getStockName();
        Portfolioproduct pp = null;
        try {
            //checks if i have stocks of this product
            if (traderTransaction.productExistsInPortfolioRemote(stockname, this.trader.getEmail())) {
                pp = traderTransaction.getPortfolioproductRemote(stockname, this.trader.getEmail());
                //checks if i have enough stock to do this transaction
                if (pp.getQuantity() >= this.transQtty) {
                    SPHistory history = traderTransaction.findLatestSPHistoryForSPRemote(this.stockproducts.get(transStockID).getStockName());
                    createNewTransction(transactionStock, history);

                    pp.setQuantity(pp.getQuantity() - transQtty);

                    double adjust = ((transQtty * history.getPrice()) - (transQtty * pp.getPrice()));
                    pp.setStockresult(pp.getStockresult() + Math.round(adjust));
                    pp.setPrice(history.getPrice());

                    traderTransaction.updatePortfolioproductRemote(pp);
                    portfolio = traderTransaction.getPortfolioproductsRemote(this.trader.getEmail());
                    totalResult = getTotalResult();
                    this.trader.setPortfolioproducts(portfolio);

                } else {
                    addMessage("You cannot sell more stocks than you own.");
                }

            } else {
                addMessage("You do not own any stocks of " + transactionStock.getStockName() + ".");
            }
        } catch (Exception e) {
            addMessage("Oops, an unknown error occurred.");
        }
        this.transQtty = 0;
    }

    /**
     * create a new transaction for traders personal history
     *
     * @param transactionStock
     * @param history latest price for stockproduct
     */
    private void createNewTransction(StockProduct transactionStock, SPHistory history) {
        TraderTransaction transaction = new TraderTransaction();

        Date date = new Date();
        transaction.setActiondate(new Timestamp(date.getTime()));
        transaction.setQuantitiy(this.transQtty);
        transaction.setSphistory(history);
        transaction.setTrader(this.trader);
        transaction.setType(this.transType);

        try {
            traderTransaction.createTransactionRemote(
                    transaction,
                    transactionStock);
            if (!allTransactions && traderHistory.size() >= 3) {
                traderHistory.remove(2);
            }
            traderHistory.add(0, transaction);
            this.trader.setTransactions(traderHistory);
        } catch (Exception e) {
            addMessage("Transaction aborted, an error occured.");
        }
    }

    /**
     * add message on view for error event
     *
     * @param message the message tho show
     */
    private void addMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    /**
     * add message for signup that email is already used
     *
     * @param message
     */
    private void addEmailMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("signupform:emailsignup", new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    /**
     * add info message on view
     *
     * @param message the message to show
     */
    private void addSuccessMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    /**
     * get traders transaction history, either the last three or all
     * transactions
     */
    public void setTransactions() {
        try {
            if (!allTransactions) {
                traderHistory = traderTransaction.getSortedTransactionListRemote(this.trader.getEmail());
                this.trader.setTransactions(traderHistory);
            } else {
                traderHistory = traderTransaction.getSortedTransactionListAllRemote(this.trader.getEmail());
                this.trader.setTransactions(traderHistory);
            }
        } catch (Exception e) {
        }
    }

    /**
     * get the stockproducts from sessionbean, which gets it from singleton bean
     *
     * @return the stockproduct list
     */
    public HashMap<Integer, StockProduct> getStockproducts() {
        try {
            this.stockproducts = traderTransaction.getAllStockProductsRemote();
        } catch (Exception e) {
            addMessage("Market is down, stock products could not be loaded.");
        }
        return this.stockproducts;
    }

    /**
     * get the price changes from last to second last market update
     *
     * @return list of price differences with stock product id
     */
    public HashMap<Integer, String> getPriceDifferences() {
        try {
            this.priceDifferences = traderTransaction.getAllStockDifferencesRemote();
        } catch (Exception e) {
            addMessage("Market is down, price information could not be loaded.");
        }
        return priceDifferences;
    }

    /**
     * this method is used to draw the graph in analytics
     *
     * @return hash map with stock product name and percentage of stock product
     * compared to total stocks
     */
    public HashMap<String, Integer> getPortfolioanalyzer() {
        double totalstocks = 0;
        Portfolioproduct product;
        for (int i = 0; i < portfolio.size(); i++) {
            product = (Portfolioproduct) portfolio.get(i);
            totalstocks += product.getQuantity();
        }
        for (int i = 0; i < portfolio.size(); i++) {
            product = (Portfolioproduct) portfolio.get(i);
            int percent = (int) (100 * (product.getQuantity() / totalstocks));
            portfolioanalyzer.put(product.getStockproduct().getName(), new Integer(percent));
        }
        return portfolioanalyzer;
    }

    /**
     * logout a trader
     *
     * @return navigate to...
     */
    public String logout() {
        String navigateTo = null;
        try {
            this.trader = null;
            navigateTo = "logoutok";
        } catch (Exception e) {
            System.out.println(e);
        }
        return navigateTo;
    }

    /**
     * method to load a traders personal data in settings view
     *
     * @return navigate to...
     */
    public String editTrader() {
        String navigateTo = null;
        try {
            this.editedFirstname = this.trader.getFirstname();
            this.editedLastname = this.trader.getLastname();
            this.editedEmail = this.trader.getEmail();
            navigateTo = "editmytrader";
        } catch (Exception e) {
            addMessage("Error in loading trader data.");
        }
        return navigateTo;
    }

    /**
     * save changes which a trader made to his settings
     *
     * @return navigate to...
     */
    public String saveChangesForTrader() {
        String navigateTo = null;
        boolean emailExists = false;
        try {
            //when trader puts a new email address, check if this email address already exists
            if (!this.trader.getEmail().equals(this.editedEmail)) {
                emailExists = this.verifyIfEmailExists(this.editedEmail);
            }
            if (!emailExists) {
                //trader has to provide his password to do changes
                if (!this.trader.getPassword().equals(PasswordEncrypter.encryptPassword(this.editedPassword))) {
                    addMessage("Your Password is not correct.");
                    //check that new password has min. length of 6 characters
                } else if (!this.editedNewPassword.equals("") && this.editedNewPassword.length() < 6) {
                    addMessage("New password must be a min. of 6 characters.");
                } else {
                    this.trader.setFirstname(this.editedFirstname);
                    this.trader.setLastname(this.editedLastname);
                    this.trader.setEmail(this.editedEmail);
                    if (!this.editedNewPassword.equals("")) {
                        this.trader.setPassword(PasswordEncrypter.encryptPassword(this.editedNewPassword));
                    }

                    this.trader = traderTransaction.updateSettingsRemote(this.trader);
                    addSuccessMessage("Changes were successfully saved.");
                    this.editedPassword = "";
                    this.editedNewPassword = "";
                }

            } else {
                addMessage("There is already a trader with this e-mail address.");
            }
            navigateTo = "editmytraderok";
        } catch (Exception e) {
            addMessage("Changes could not be saved.");
            navigateTo = "editmytraderok";
        }
        return navigateTo;
    }

    /**
     * verify if email exists already in DB
     *
     * @param email
     * @return true or false
     */
    public boolean verifyIfEmailExists(String email) {
        boolean emailExists = false;
        try {
            emailExists = traderFacade.verifyEmailRemote(email);
        } catch (Exception e) {
            addMessage("Error in verifying e-mail address.");
        }
        return emailExists;
    }

    public String getEmailsignup() {
        return emailsignup;
    }

    public void setEmailsignup(String emailsignup) {
        this.emailsignup = emailsignup;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPwdsignup() {
        return pwdsignup;
    }

    public void setPwdsignup(String pwdsignup) {
        this.pwdsignup = pwdsignup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPortfolioanalyzer(HashMap<String, Integer> portfolioanalyzer) {
        this.portfolioanalyzer = portfolioanalyzer;
    }

    public int getTransQtty() {
        return transQtty;
    }

    public void setTransQtty(int transQtty) {
        this.transQtty = transQtty;
    }

    public int getTransStockID() {
        return transStockID;
    }

    public void setTransStockID(int transStockID) {
        this.transStockID = transStockID;
    }

    public boolean isAllTransactions() {
        return allTransactions;
    }

    public void setAllTransactions(boolean allTransactions) {
        this.allTransactions = allTransactions;
    }

    /**
     * total result of my portfolio
     *
     * @return total result
     */
    public double getTotalResult() {
        double result = 0;
        if (!this.portfolio.isEmpty()) {
            for (Iterator<Portfolioproduct> it = this.portfolio.iterator(); it.hasNext();) {
                Portfolioproduct product = it.next();
                result += product.getStockresult();
            }
        }
        return result;
    }

    public void setTotalResult(double totalResult) {
        this.totalResult = totalResult;
    }

    public List<Portfolioproduct> getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(List<Portfolioproduct> portfolio) {
        this.portfolio = portfolio;
    }

    public List<TraderTransaction> getTraderHistory() {
        return traderHistory;
    }

    public void setTraderHistory(List<TraderTransaction> traderHistory) {
        this.traderHistory = traderHistory;
    }

    public String getEditedEmail() {
        return editedEmail;
    }

    public void setEditedEmail(String editedEmail) {
        this.editedEmail = editedEmail;
    }

    public String getEditedFirstname() {
        return editedFirstname;
    }

    public void setEditedFirstname(String editedFirstname) {
        this.editedFirstname = editedFirstname;
    }

    public String getEditedLastname() {
        return editedLastname;
    }

    public void setEditedLastname(String editedLastname) {
        this.editedLastname = editedLastname;
    }

    public String getEditedPassword() {
        return editedPassword;
    }

    public void setEditedPassword(String editedPassword) {
        this.editedPassword = editedPassword;
    }

    public String getEditedNewPassword() {
        return editedNewPassword;
    }

    public void setEditedNewPassword(String editedNewPassword) {
        this.editedNewPassword = editedNewPassword;
    }
}