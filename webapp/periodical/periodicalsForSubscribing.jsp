<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tag/language.tld" prefix="lang" %>
<html>
    <head>
        <title>Reader page</title>
        <meta charset="UTF-8">
    </head>
    <body>

        <c:if test="${periodicals.size() == 0}" >
            <p>
                <lang:print message = "periodical.periodicalForSubscribing.jsp.you_have_subscribed_all_periodicals"/>
            </p>
        </c:if>
        <c:if test="${periodicals.size() > 0}" >

        <form action = "/app/periodicals/periodical/getByTopicPeriodicalsForSubscribing" method = "GET">
            <label for="topic"><lang:print message = "periodical.periodicalForSubscribing.jsp.label.find_periodical_by_topic"/>:</label>
            <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                <select name="topic">
                    <option value="business"><lang:print message = "periodical.periodicalForSubscribing.jsp.option.business"/></option>
                    <option value="sport" selected><lang:print message = "periodical.periodicalForSubscribing.jsp.option.sport"/></option>
                    <option value="technology"><lang:print message = "periodical.periodicalForSubscribing.jsp.option.technology"/></option>
                </select>
            <button type ="submit"><lang:print message = "periodical.periodicalForSubscribing.jsp.button.search"/></button>
        </form>
        <br>

        <form action = "/app/periodicals/periodical/findByNamePeriodicalsForSubscribing" method ="GET">
            <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
            <label for="name"><lang:print message = "periodical.periodicalForSubscribing.jsp.label.find_periodical_by_name"/>:</label>
            <input type="text" name="name" required>
            <input type="submit" value='<lang:print message = "periodical.periodicalForSubscribing.jsp.button.search"/>'>
        </form>
        <br><br>

        <button onclick="sortName(false)"><lang:print message = "periodical.periodicalForSubscribing.jsp.button.sort_by_name"/></button>
        <button onclick="sortName(true)"><lang:print message = "periodical.periodicalForSubscribing.jsp.button.reversed_sort_by_name"/></button>
        <br><br>
        <button onclick="sortPrice(false)"><lang:print message = "periodical.periodicalForSubscribing.jsp.button.sort_by_cost"/></button>
        <button onclick="sortPrice(true)"><lang:print message = "periodical.periodicalForSubscribing.jsp.button.reversed_sort_by_cost"/></button>
        <br><br>

        <form action = "/app/periodicals/periodical/periodicalsForSubscribing" method = "GET">
            <label for="name"><lang:print message = "periodical.periodicalForSubscribing.jsp.label.click_to_reset_sorting"/></label><br>
                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                <input type = "submit" value ='<lang:print message = "periodical.periodicalForSubscribing.jsp.button.reset"/>'>
        </form>

            <table id="periodicalsForSubscribing">
                <caption><lang:print message = "periodical.periodicalForSubscribing.jsp.table.caption.periodicals_you_have_not_subscribed_yet"/></caption>
                    <tr>
                        <th><lang:print message = "periodical.periodicalForSubscribing.jsp.table.name"/></th>
                        <th><lang:print message = "periodical.periodicalForSubscribing.jsp.table.topic"/></th>
                        <th><lang:print message = "periodical.periodicalForSubscribing.jsp.table.cost"/></th>
                        <th><lang:print message = "periodical.periodicalForSubscribing.jsp.table.description"/></th>
                        <c:if test="${reader.lock == 'false'}" >
                        <th><lang:print message = "periodical.periodicalForSubscribing.jsp.table.choose_subscribe_period"/></th>
                        </c:if>
                    </tr>
                <c:forEach items="${periodicals}" var="periodical">
                    <tr>
                        <td>${periodical.name}</td>
                        <td>${periodical.topic}</td>
                        <td>${periodical.cost}</td>
                        <td>${periodical.description}</td>
                        <c:if test="${reader.lock == 'false'}" >
                        <td align="center">
                            <form action ="/app/periodicals/prepayment/addSubscription" method = "POST" onsubmit = "return confirm('You are subscribing periodical ${periodical.name}, are you sure?');">
                                <input type = "hidden" name="readerId" value = "${sessionScope.user.id}"/>
                                <input type = "hidden" name = "periodicalId" value = "${periodical.id}">
                                    <select name="durationOfSubscription">
                                        <option value="30"><lang:print message = "periodical.periodicalForSubscribing.jsp.table.option.subscription_for_30_days"/></option>
                                        <option value="365" selected><lang:print message = "periodical.periodicalForSubscribing.jsp.table.option.subscription_for_365_days"/></option>
                                    </select>
                                <input type = "submit" value ='<lang:print message = "periodical.periodicalForSubscribing.jsp.button.subscribe"/>'>
                            </form>
                        </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:if>


    <button onclick="location.href='/app/reader/readerHome.jsp'"><lang:print message = "periodical.periodicalForSubscribing.jsp.button.back_home"/></button>

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

    <script>
        function sortName(inverse) {
          var table, rows, switching, i, x, y, shouldSwitch;
          table = document.getElementById("periodicalsForSubscribing");
          switching = true;
          /*Make a loop that will continue until
          no switching has been done:*/
          while (switching) {
            //start by saying: no switching is done:
            switching = false;
            rows = table.rows;
            /*Loop through all table rows (except the
            first, which contains table headers):*/
            for (i = 1; i < (rows.length - 1); i++) {
              //start by saying there should be no switching:
              shouldSwitch = false;
              /*Get the two elements you want to compare,
              one from current row and one from the next:*/
              x = rows[i].getElementsByTagName('TD')[0];
              y = rows[i + 1].getElementsByTagName('TD')[0];
              //check if the two rows should switch place:
              if ((x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) ^ inverse) {
                //if so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
              }
            }
            if (shouldSwitch) {
              /*If a switch has been marked, make the switch
              and mark that a switch has been done:*/
              rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
              switching = true;
            }
          }
        }
        function sortPrice(inverse) {
          var table, rows, switching, i, x, y, shouldSwitch;
          table = document.getElementById("periodicalsForSubscribing");
          switching = true;
          /*Make a loop that will continue until
          no switching has been done:*/
          while (switching) {
            //start by saying: no switching is done:
            switching = false;
            rows = table.rows;
            /*Loop through all table rows (except the
            first, which contains table headers):*/
            for (i = 1; i < (rows.length - 1); i++) {
              //start by saying there should be no switching:
              shouldSwitch = false;
              /*Get the two elements you want to compare,
              one from current row and one from the next:*/
              x = rows[i].getElementsByTagName('TD')[2];
              y = rows[i + 1].getElementsByTagName('TD')[2];
              //check if the two rows should switch place:
              if ((parseInt(x.innerHTML, 10) > parseInt(y.innerHTML, 10)) ^ inverse) {
                //if so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
              }
            }
            if (shouldSwitch) {
              /*If a switch has been marked, make the switch
              and mark that a switch has been done:*/
              rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
              switching = true;
            }
          }
        }
    </script>

</html>