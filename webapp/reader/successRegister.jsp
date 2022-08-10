<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Welcome page</title>
        <meta charset="UTF-8">
    </head>
    <h1>
        <lang:print message = "reader.successRegister.jsp.registration_successful"/>
    </h1>
    <body>
        <form accept-charset="UTF-8" method="POST" action="/app/periodicals/login">
            <label for="name"><lang:print message = "reader.successRegister.jsp.label.login"/>:</label><br>
            <input type="text" name="login" pattern=".{3,}" title='<lang:print message = "index.jsp.input.login_validation"/>'><br><br>
            <label for="pass"><lang:print message = "reader.successRegister.jsp.label.password"/>:</label><br>
            <input type="password" name="password" pattern="(?=.*\d)(?=.*[a-zа-яії])(?=.*[A-ZА-ЯІЇ]).{8,}" title='<lang:print message = "index.jsp.input.password_validation"/>' required><br><br>
            <input type = "submit" style="width: 8%" value='<lang:print message = "reader.successRegister.jsp.button.sign_in"/>'>
            <br><br>
        </form>
    </body>
</html>