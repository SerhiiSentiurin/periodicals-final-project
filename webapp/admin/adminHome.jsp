<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Admin page</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <lang:print message = "admin.adminHome.jsp.welcome_home_page"/>, ${sessionScope.user.login}
        </p>

        <p>
            <form action = "/app/periodicals/admin/managePeriodicals" method = "GET">
                <input type = "submit" value = '<lang:print message = "admin.adminHome.jsp.button.manage_periodicals"/>'>
            </form><br>
        </p>

        <p>
            <form action = "/app/periodicals/admin/manageReaders" method = "GET">
                <input type = "submit" value = '<lang:print message = "admin.adminHome.jsp.button.manage_readers"/>'>
            </form><br>
        </p>

        <p>
            <form action = "/app/periodicals/logout" method = "POST">
                <input type = "submit" value = '<lang:print message = "admin.adminHome.jsp.button.logout"/>'>
            </form><br>
        </p>
    </body>

</html>