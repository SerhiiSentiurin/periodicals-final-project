<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Admin page</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <button onclick="location.href='/app/admin/adminHome.jsp'"><lang:print message = "admin.managePeriodicals.jsp.button.back_home"/></button>
        <br><br>

        <table id="createPeriodical">
            <tr>
                <th><lang:print message = "admin.managePeriodicals.jsp.table.write_name"/></th>
                <th><lang:print message = "admin.managePeriodicals.jsp.table.write_topic"/></th>
                <th><lang:print message = "admin.managePeriodicals.jsp.table.write_cost"/></th>
                <th><lang:print message = "admin.managePeriodicals.jsp.table.write_description"/></th>
                <th><lang:print message = "admin.managePeriodicals.jsp.table.create_new_periodical"/></th>
            </tr>
            <tr>
                <td><textarea form = "create" name = "name" rows = "3" cols="25" wrap="soft" required></textarea></td>
                <td><textarea form = "create" name = "topic" rows = "3" cols="25" wrap="soft" required></textarea></td>
                <td><input form = "create" type= "number" min = "0" step = 0.01 name="cost" required></td>
                <td><textarea form = "create" name = "description" rows = "3" cols="125" wrap="soft" required> </textarea></td>
                <td  align="center">
                    <form action = "/app/periodicals/admin/createNewPeriodical" method = "POST" id = "create">
                        <input type = "submit" value = '<lang:print message = "admin.managePeriodicals.jsp.button.create"/>'>
                    </form>
                </td>
            </tr>
        </table>

        <table id="listOfPeriodicals">
            <caption><lang:print message = "admin.managePeriodicals.jsp.table.caption.periodicals"/></caption>
                <tr>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.name"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.topic"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.cost"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.description"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.status"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.delete_periodical_for_users"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.restore_periodical_for_users"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.completely_delete_periodical"/></th>
                    <th><lang:print message = "admin.managePeriodicals.jsp.table.edit_periodical"/></th>
                </tr>
            <c:forEach items="${periodicals}" var="periodical">
                <tr>
                    <td>${periodical.name}</td>
                    <td>${periodical.topic}</td>
                    <td>${periodical.cost}</td>
                    <td>${periodical.description}</td>
                        <c:if test="${periodical.isDeleted == 'true'}" >
                            <td><lang:print message = "admin.managePeriodicals.jsp.table.status.blocked"/></td>
                        </c:if>
                        <c:if test="${periodical.isDeleted == 'false'}" >
                            <td><lang:print message = "admin.managePeriodicals.jsp.table.status.available"/></td>
                        </c:if>
                    <td align="center">
                        <form action = "/app/periodicals/admin/deletePeriodicalForReaders" method = "POST">
                            <input type = "hidden" name = "periodicalId" value = "${periodical.id}">
                            <input type = "submit" value = '<lang:print message = "admin.managePeriodicals.jsp.table.button.delete"/>'>
                        </form>
                    </td>
                    <td align="center">
                        <form action = "/app/periodicals/admin/restorePeriodicalForReaders" method = "POST">
                            <input type = "hidden" name = "periodicalId" value = "${periodical.id}">
                            <input type = "submit" value = '<lang:print message = "admin.managePeriodicals.jsp.table.button.restore"/>'>
                        </form>
                    </td>
                    <td align="center">
                        <form action = "/app/periodicals/admin/deletePeriodical" method = "POST" onsubmit = "return confirm('Are you sure you want to delete periodical ${periodical.name}?');">
                            <input type = "hidden" name = "periodicalId" value = "${periodical.id}">
                            <input type = "submit" value = '<lang:print message = "admin.managePeriodicals.jsp.table.button.warning_absolute_delete"/>'>
                        </form>
                    </td>
                    <td align="center">
                        <form action = "/app/periodicals/admin/getPeriodicalForEdit" method = "GET">
                            <input type = "hidden" name = "periodicalId" value = "${periodical.id}">
                            <input type = "submit" value = '<lang:print message = "admin.managePeriodicals.jsp.table.button.edit"/>'>
                        </form>
                    </td>
                </tr>
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