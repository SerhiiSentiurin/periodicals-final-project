<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<!DOCTYPE html>
<html>
    <head>
        <title>error:403</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <lang:print message = "error.forbidden.jsp.access_forbidden"/>
        </p>
        <button onclick="location.href='/app'"><lang:print message = "error.forbidden.jsp.back"/></button>
    </body>
</html>