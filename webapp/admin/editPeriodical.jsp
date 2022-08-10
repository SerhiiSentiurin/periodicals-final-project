<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Admin page</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <table id="editPeriodical">
            <caption><lang:print message ="admin.editPeriodical.jsp.table.caption"/> "${periodical.name}"</caption>
                <tr>
                    <th><lang:print message ="admin.editPeriodical.jsp.table.name"/></th>
                    <th><lang:print message ="admin.editPeriodical.jsp.table.topic"/></th>
                    <th><lang:print message ="admin.editPeriodical.jsp.table.cost"/></th>
                    <th><lang:print message ="admin.editPeriodical.jsp.table.description"/></th>
                    <th><lang:print message ="admin.editPeriodical.jsp.table.edit"/></th>
                </tr>
                <tr>
                    <td><textarea name = "name" form = "edit" rows = "12" cols="25" wrap="soft" required >${periodical.name}</textarea></td>
                    <td><textarea name="topic" form = "edit" rows="12" cols="25" wrap="soft" required >${periodical.topic}</textarea></td>
                    <td><input form = "edit" type = "number" name = "cost" min = "0" step = 0.01 value = "${periodical.cost}" required></td>
                    <td><textarea name="description" form = "edit" rows="12" cols="150" wrap="soft" required >${periodical.description}</textarea></td>
                    <td>
                        <form action = "/app/periodicals/admin/editPeriodical" method = "POST" id = "edit">
                            <input type = "hidden" name = "periodicalId" value = "${periodical.id}">
                            <input type = "submit" value = '<lang:print message ="admin.editPeriodical.jsp.button.edit_periodical"/>'>
                        </form>
                    </td>
                </tr>
        </table>

        <form action = "/app/periodicals/admin/managePeriodicals" method = "GET">
            <input type = "submit" value = '<lang:print message ="admin.editPeriodical.jsp.button.back"/>'>
        </form>
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