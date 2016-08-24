<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Playlist</title>
</head>
<body>
    <div class="row">
        <div col="col-sm-12 col-lg-3">
            <div class="form-group">
                <g:link controller="main" action="getConcertArtists" params="$addAllConcertArtistsTopTracksToNewPlaylistParams"><< Back</g:link>
                <br/>
                <g:link controller="main" action="index" params="$params"><< Start Again</g:link>
                <p>${flash.message}</p>
                <g:if test="${missingArtists.size() > 0}">
                    <p>The following artists are not on Spotify:</p>
                    <ul>
                    <g:each in="${missingArtists}" var="artist">
                        <li>${artist}</li>
                    </g:each>
                    </ul>
                </g:if>
            </div>
        </div>
    </div>
    <div class="row">
        <div col="col-sm-12 col-lg-3">
            <iframe src="https://embed.spotify.com/?uri=${spotifyPlaylistUri}&theme=white&view=coverart"
                    frameborder="0"
                    allowtransparency="true">
            </iframe>
        </div>
    </div>
</body>
</html>
