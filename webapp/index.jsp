<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Welcome page</title>
        <meta charset="UTF-8">
    </head>
    <h1>
        <lang:print message = "index.jsp.welcome_to_app"/>
     </h1>
    <body>
        <form action="/app/periodicals/login" method="POST">
            <label for="name"><lang:print message = "index.jsp.label.login"/>:</label><br>
            <input type="text" name="login" pattern=".{3,}" title='<lang:print message = "index.jsp.input.login_validation"/>' required ><br><br>
            <label for="pass"><lang:print message = "index.jsp.label.password"/>:</label><br>
            <input type="password" name="password" pattern="(?=.*\d)(?=.*[a-zа-яії])(?=.*[A-ZА-ЯІЇ]).{8,}" title='<lang:print message = "index.jsp.input.password_validation"/>' required><br><br>
            <input type = "submit" style="width: 8%" value='<lang:print message = "index.jsp.button.sign_in"/>'>
            <br><br>
            <input type = "submit" style="width: 8%" formaction="/app/periodicals/reader/create" value='<lang:print message = "index.jsp.button.sign_up"/>'>
        </form>

        <br><br>
        <h3>
            <lang:print message = "index.jsp.if_u_are_not_registered"/>
        </h3>
        <form accept-charset="UTF-8" method="GET" action="/app/periodicals/periodical/watch">
            <input type="hidden" name="page" value="1">
            <input type = "submit" style="width: 10%" value='<lang:print message = "index.jsp.button.watch_periodicals"/>'>
        </form><br><br>

        <form action = "/app/periodicals/changeLocale" method = "POST">
            <input type = "hidden" name = "view" value = "/index.jsp"/>
                <select name = "selectedLocale">
                    <c:forEach var = "locale" items = "${sessionScope.locales}">
                        <option value = "${locale}">
                            ${locale}
                        </option>
                    </c:forEach>
                </select>
            <input type = "submit" value = 'Update'>
        </form>
    </body>
</html>