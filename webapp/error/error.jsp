<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>error:400</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <jsp:text>
                error: ${message}
            </jsp:text>
        </p>
        <input type="button" class="btn btn-secondary" onclick="history.back();" value='Back'>
    </body>
</html>