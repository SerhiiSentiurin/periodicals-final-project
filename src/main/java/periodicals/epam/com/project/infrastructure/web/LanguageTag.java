package periodicals.epam.com.project.infrastructure.web;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.tagext.TagSupport;
import lombok.extern.log4j.Log4j2;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Log4j2
public class LanguageTag extends TagSupport {


    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int doStartTag() {
        HttpSession session = pageContext.getSession();
        Locale locale = (Locale) session.getAttribute("selectedLocale");
        if (message != null && !message.isEmpty()) {
            ResourceBundle messages = ResourceBundle.getBundle("i18n.resources", locale);
            String locMessage = messages.getString(message);
            try {
                pageContext.getOut().print(locMessage);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return SKIP_BODY;
    }
}
