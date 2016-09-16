<!doctype html>
<html>
    <head>
        <title><g:if env="development">Redirecting...</g:if><g:else>Error</g:else></title>
    </head>
<body>
    <h1>Redirecting...</h1>
    <g:javascript>
        window.location.href = "${g.createLink(uri: '/')}";
    </g:javascript>
</body>
</html>