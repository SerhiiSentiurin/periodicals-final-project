<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Account page</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <lang:print message = "account.manageAccount.jsp.welcome"/> ${sessionScope.user.login}, <lang:print message = "account.manageAccount.jsp.here_you_can_manage_your_account"/>
        </p>
        <br><br>

            <table id="manageAccount" vertical-align = "center">
                <caption><lang:print message = "account.manageAccount.jsp.table.caption.account_balance"/>: ${amountOfMoney} $</caption>
                    <tr>
                        <th><lang:print message = "account.manageAccount.jsp.table.top_up_account"/></th>
                        <th><lang:print message = "account.manageAccount.jsp.table.choose_periodical_to_subscribe"/></th>
                    </tr>
                    <tr>
                        <td align="center">
                            <form action ="/app/periodicals/account/topUpAccountAmount" method = "POST">
                                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                                <input type="number" min = "0" step = 0.01 name="amountOfMoney" required><br><br>
                                <input type = "submit" value ='<lang:print message = "account.manageAccount.jsp.table.button.top_up"/>'>
                            </form>
                        </td>
                        <td align="center">
                            <form action = "/app/periodicals/periodical/periodicalsForSubscribing" method = "GET">
                                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                                <input type = "submit" value ='<lang:print message = "account.manageAccount.jsp.table.button.get_subscription"/>'>
                            </form>
                        </td>
                    </tr>
            </table><br>
        <button onclick="location.href='/app/reader/readerHome.jsp'"><lang:print message = "account.manageAccount.jsp.button.back_home"/></button>

        <style>
            caption {
                font-family: annabelle;
                font-weight: bold;
                font-size: 2em;
                padding: 10px;
                border: 1px solid #A9E2CC;
            }
            th {
                padding: 10px;
                border: 1px solid #A9E2CC;
            }
            td {
                font-size: 1.5em;
                padding: 5px 10px;
                border: 1px solid #A9E2CC;
            }
        </style>
    </body>
</html>