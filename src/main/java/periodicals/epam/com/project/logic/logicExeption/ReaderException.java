package periodicals.epam.com.project.logic.logicExeption;

import periodicals.epam.com.project.infrastructure.web.exception.ApplicationException;

public class ReaderException extends ApplicationException {
    public ReaderException(String s) {
        super(s);
    }
}
