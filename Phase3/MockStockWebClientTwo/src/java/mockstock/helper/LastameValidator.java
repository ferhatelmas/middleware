package mockstock.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * this class validates lastname according to pattern
 * @author Kenny Lienhard
 */
@FacesValidator("mockstock.helper.LastnameValidator")
public class LastameValidator implements Validator {

    private static final String LASTNAME_PATTERN = "[^0-9]*";
    private Pattern pattern;
    private Matcher matcher;

    public LastameValidator() {
        pattern = Pattern.compile(LASTNAME_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches()) {

            FacesMessage msg =
                    new FacesMessage("Last name is not valid.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }
    }
}
