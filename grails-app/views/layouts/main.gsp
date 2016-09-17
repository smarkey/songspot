<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <asset:stylesheet src="bootstrap-switch.min.css"/>
    <asset:stylesheet src="bootstrap-multiselect.css"/>
    <asset:stylesheet src="daterangepicker.css"/>
    <asset:stylesheet src="bootstrap-slider.css"/>
    <asset:stylesheet src="application.css"/>
    <asset:stylesheet src="custom.css"/>

    <script src="https://code.jquery.com/jquery-3.1.0.min.js" integrity="sha256-cCueBR6CsyA4/9szpPfrX3s49M9vUU5BgtiJj06wt/s=" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.14.1/moment.min.js"></script>
    <asset:javascript src="bootstrap-switch.min.js"/>
    <asset:javascript src="bootstrap-multiselect.js"/>
    <asset:javascript src="daterangepicker.js"/>
    <asset:javascript src="bootstrap-slider.js"/>
    <g:layoutHead/>
</head>
<body>

    <div class="navbar navbar-default navbar-static-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                %{--<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">--}%
                    %{--<span class="sr-only">Toggle navigation</span>--}%
                    %{--<span class="icon-bar"></span>--}%
                    %{--<span class="icon-bar"></span>--}%
                    %{--<span class="icon-bar"></span>--}%
                %{--</button>--}%
                <h1>Spotkick</h1>
            </div>
            <div class="navbar-collapse collapse" aria-expanded="false" style="height: 0.8px;">
                <ul class="nav navbar-nav navbar-right">
                    <g:pageProperty name="page.nav" />
                    <li><g:link controller="main" action="index">Configure</g:link></li>
                </ul>
            </div>
        </div>
    </div>

    <g:layoutBody/>

    <div class="footer" role="contentinfo">
        <g:link controller="main" action="index" params="$params">Start again?</g:link>
    </div>

    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

    <asset:javascript src="application.js"/>

</body>
</html>
