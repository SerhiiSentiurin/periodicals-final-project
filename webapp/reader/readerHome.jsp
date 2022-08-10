<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Reader page</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <lang:print message = "reader.readerHome.jsp.welcome"/> ${sessionScope.user.login}, <lang:print message = "reader.readerHome.jsp.home_page"/>
        </p><br><br>

        <p>
            <form action ="/app/periodicals/periodical/readerSubscriptions" method = "GET">
                <label for="name"><lang:print message = "reader.readerHome.jsp.label.click_to_watch_subscriptions"/>:</label><br>
                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                <input type = "submit" value ='<lang:print message = "reader.readerHome.jsp.button.watch"/>'>
            </form><br>
        </p>

        <p>
            <form action = "/app/periodicals/periodical/periodicalsForSubscribing" method = "GET">
                <label for="name"><lang:print message = "reader.readerHome.jsp.label.click_to_subscribe_on_other_periodicals"/>:</label><br>
                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                <input type = "submit" value ='<lang:print message = "reader.readerHome.jsp.button.choose_periodical"/>'>
            </form><br>
        </p>

        <p>
            <form action ="/app/periodicals/account/getAccountInfo" method = "GET">
                <label for="name"><lang:print message = "reader.readerHome.jsp.label.manage_your_account"/>:</label><br>
                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                <input type = "submit" value ='<lang:print message = "reader.readerHome.jsp.button.manage"/>'>
            </form><br>
        </p>

        <p>
            <form action = "/app/periodicals/logout" method = "POST">
                <input type = "submit" value = '<lang:print message = "reader.readerHome.jsp.button.logout"/>'>
            </form>
        </p>
    </body>
</html>