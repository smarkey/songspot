<%@ page import="org.joda.time.DateTime" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to SpotKick</title>
    <asset:javascript src="jquery-2.2.0.min.js"/>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>
<body>
    <p>${flash.message}</p>
    <button id="selectAll" type="button" class="btn btn-info">Select All</button>
    <button id="unselectAll" type="button" class="btn btn-info">Unselect All</button>
    <g:select name="concerts" multiple="true" from="${artists}"></g:select>
    <script>
        $("#selectAll").on("click", function() {
            $('#concerts option').prop('selected', true);
        });
        $("#unselectAll").on("click", function() {
            $('#concerts option').prop('selected', false);
        });
    </script>
</html>
