<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Admin page</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <button onclick="location.href='/app/admin/adminHome.jsp'"><lang:print message = "admin.manageReaders.jsp.button.back_home"/></button>
        <br><br>

        <table id="listOfReaders">
            <caption><lang:print message = "admin.manageReaders.jsp.table.caption.readers"/></caption>
                <tr>
                    <th><lang:print message = "admin.manageReaders.jsp.table.reader_login"/></th>
                    <th><lang:print message = "admin.manageReaders.jsp.table.account_balance"/></th>
                    <th><lang:print message = "admin.manageReaders.jsp.table.reader_status"/></th>
                    <th><lang:print message = "admin.manageReaders.jsp.table.lock_reader"/></th>
                    <th><lang:print message = "admin.manageReaders.jsp.table.unlock_reader"/></th>
                </tr>
                <c:forEach items="${readers}" var="reader">
                <tr>
                    <td>${reader.login}</td>
                    <td>${reader.account.amountOfMoney}</td>
                        <c:if test="${reader.lock == 'true'}" >
                            <td><lang:print message = "admin.manageReaders.jsp.table.status.blocked"/></td>
                        </c:if>
                        <c:if test="${reader.lock == 'false'}" >
                            <td><lang:print message = "admin.manageReaders.jsp.table.status.not_blocked"/></td>
                        </c:if>
                    <td align="center">
                        <form action = "/app/periodicals/admin/lockReader" method = "POST">
                            <input type = "hidden" name = "readerId" value = "${reader.id}">
                            <input type = "submit" value = '<lang:print message = "admin.manageReaders.jsp.table.button.lock"/>'>
                        </form>
                    </td>
                    <td align="center">
                        <form action = "/app/periodicals/admin/unlockReader" method = "POST">
                            <input type = "hidden" name = "readerId" value = "${reader.id}">
                            <input type = "submit" value = '<lang:print message = "admin.manageReaders.jsp.table.button.unlock"/>'>
                        </form>
                </c:forEach>
        </table>

        <style>
            caption {
                font-family: annabelle;
                font-weight: bold;
                font-size: 1.5em;
                padding: 10px;
                border: 1px solid #A9E2CC;
            }
            th {
                padding: 10px;
                border: 1px solid #A9E2CC;
            }
            td {
                font-size: 1.0em;
                padding: 5px 7px;
                border: 1px solid #A9E2CC;
            }
        </style>
    </body>
</html>