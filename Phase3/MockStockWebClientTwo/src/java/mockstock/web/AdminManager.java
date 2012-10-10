package mockstock.web;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mockstock.db.Trader;
import mockstock.db.enumeration.UserRight;
import mockstock.db.enumeration.UserStatus;
import mockstock.ejb.front.AdminRemote;
import mockstock.ejb.front.TraderFacadeRemote;
import mockstock.helper.PasswordEncrypter;

/**
 * managed bean for admin, providing additional functionalities for admin
 *
 * @author Kenny Lienhard
 */
@ManagedBean
@SessionScoped
public class AdminManager {

    private List<Trader> traders; //to list all traders
    private Long idOfTraderToEdit; //edit a trader
    @EJB
    TraderFacadeRemote traderFacade;
    //variables to edit trader
    private Trader traderToEdit;
    private String editedPassword;
    private String editedFirstname;
    private String editedLastname;
    private String editedEmail;
    private UserStatus editedStatus;
    private UserRight editedRights;
    //variables to add trader
    private String newPassword;
    private String newFirstname;
    private String newLastname;
    private String newEmail;
    private UserStatus newStatus = UserStatus.ACTIVATED;
    private UserRight newRights = UserRight.USER;
    @EJB
    AdminRemote adminRemote;

    /**
     * getters and setters
     */
    public Long getIdOfTraderToEdit() {
        return idOfTraderToEdit;
    }

    public void setIdOfTraderToEdit(Long idOfTraderToEdit) {
        this.idOfTraderToEdit = idOfTraderToEdit;
    }

    public Trader getTraderToEdit() {
        return traderToEdit;
    }

    public void setTraderToEdit(Trader traderToEdit) {
        this.traderToEdit = traderToEdit;
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

    public UserRight getEditedRights() {
        return editedRights;
    }

    public void setEditedRights(UserRight editedRights) {
        this.editedRights = editedRights;
    }

    public UserStatus getEditedStatus() {
        return editedStatus;
    }

    public void setEditedStatus(UserStatus editedStatus) {
        this.editedStatus = editedStatus;
    }

    public String getEditedPassword() {
        return editedPassword;
    }

    public void setEditedPassword(String editedPassword) {
        this.editedPassword = editedPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewFirstname() {
        return newFirstname;
    }

    public void setNewFirstname(String newFirstname) {
        this.newFirstname = newFirstname;
    }

    public String getNewLastname() {
        return newLastname;
    }

    public void setNewLastname(String newLastname) {
        this.newLastname = newLastname;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UserRight getNewRights() {
        return newRights;
    }

    public void setNewRights(UserRight newRights) {
        this.newRights = newRights;
    }

    public UserStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(UserStatus newStatus) {
        this.newStatus = newStatus;
    }

    /**
     * lists all traders
     *
     * @return trader list
     */
    public List<Trader> getTraders() {
        try {
            this.traders = adminRemote.getTraderListRemote();
        } catch (Exception e) {
            addMessage("Error in loading trader data.");
        }
        return this.traders;
    }

    /**
     * edit a trader, load trader data of selected trader
     *
     * @return navigate to...
     */
    public String editTrader() {
        String navigateTo = null;
        try {
            this.traderToEdit = adminRemote.getTraderRemote(this.idOfTraderToEdit);
            this.editedFirstname = this.traderToEdit.getFirstname();
            this.editedLastname = this.traderToEdit.getLastname();
            this.editedEmail = this.traderToEdit.getEmail();
            this.editedRights = this.traderToEdit.getUsrgroup();
            this.editedStatus = this.traderToEdit.getStatus();
            navigateTo = "edittrader";
        } catch (Exception e) {
            addMessage("Error in loading trader data.");
        }
        return navigateTo;
    }

    /**
     * save changes for an edited trader
     *
     * @return navigate to...
     */
    public String saveChangesForTrader() {
        String navigateTo = null;
        boolean emailExists = false;
        boolean pwdValidation = false;
        try {
            if (!this.editedPassword.equals("") && this.editedPassword.length() < 6) {
                pwdValidation = true;
                addMessage("New password must be a min. of 6 characters.");
            }
            if (!this.traderToEdit.getEmail().equals(this.editedEmail)) {
                emailExists = this.verifyIfEmailExists(this.editedEmail);
                if (emailExists) {
                    addMessage("There is already a trader with this e-mail address.");
                }
            }
            if (!emailExists && !pwdValidation) {
                this.traderToEdit.setFirstname(this.editedFirstname);
                this.traderToEdit.setLastname(this.editedLastname);
                if (!this.editedPassword.equals("")) {
                    this.traderToEdit.setPassword(
                            PasswordEncrypter.encryptPassword(this.editedPassword));
                }
                this.traderToEdit.setEmail(this.editedEmail);
                this.traderToEdit.setStatus(this.editedStatus);
                this.traderToEdit.setUsrgroup(this.editedRights);
                this.traderToEdit = adminRemote.updateTraderRemote(this.traderToEdit);
                addSuccessMessage("Changes were successfully saved.");
            }
            navigateTo = "edittraderok";
        } catch (Exception e) {
            addMessage("Changes could not be saved.");
            navigateTo = "edittraderok";
        }
        return navigateTo;
    }

    /**
     * delete a trader
     *
     * @return navigate to...
     */
    public String deleteTrader() {
        String navigateTo = null;
        try {
            adminRemote.removeTraderRemote(this.traderToEdit);
            addSuccessMessage("Trader was successfully deleted.");
            navigateTo = "deletetraderok";
        } catch (Exception e) {
            addMessage("Trader could not be deleted.");
            navigateTo = "deletetraderok";
        }
        return navigateTo;
    }

    /*
     * Admin has a separated method to add trader, as he has the rights to
     * decide about user rights and user status, which is not the case for a
     * trader who whishes to sign up.
     */
    public String addNewTrader() {
        String navigateTo = null;
        try {
            if (this.verifyIfEmailExists(this.newEmail)) {
                addMessage("There is already a trader with this e-mail address.");
            } else {
                Trader trader = new Trader();
                trader.setEmail(newEmail);
                trader.setFirstname(newFirstname);
                trader.setLastname(newLastname);
                trader.setPassword(PasswordEncrypter.encryptPassword(newPassword));
                trader.setStatus(newStatus);
                trader.setUsrgroup(newRights);

                traderFacade.addTraderRemote(trader);

                /*
                 * reset new fields so that they are empty when page is
                 * reloaded, facilitates input for next new trader
                 */
                this.newEmail = null;
                this.newFirstname = null;
                this.newLastname = null;
                this.newPassword = null;
                addSuccessMessage("Trader was successfully added.");
            }
            navigateTo = "admaddtraderok";
        } catch (Exception e) {
            addMessage("Trader could not be added.");
            navigateTo = "admaddtraderok";
        }
        return navigateTo;
    }

    /**
     * email is unique in DB, checks if there is already a user with this email
     *
     * @param email email to check
     * @return email exists or doesn't exists
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

    /**
     * add event message to view for error
     *
     * @param message the message to show
     */
    private void addMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    /**
     * add event message to view for info
     *
     * @param message the message to show
     */
    private void addSuccessMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }
}
