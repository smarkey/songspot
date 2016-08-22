<%@ page import="org.joda.time.DateTime" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Playlist</title>
</head>
<body>
    <p>${flash.message}</p>

    <iframe src="https://embed.spotify.com/?uri=${spotifyPlaylistUri}&theme=white&view=coverart"
            frameborder="0"
            allowtransparency="true">
    </iframe>

    <script>

    </script>
</html>
