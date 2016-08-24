<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Spotkick</title>
</head>
<body>
    <div class="row">
        <div col="col-sm-12 col-lg-3">
            <div class="form-group">
                <g:link controller="main" action="index" params="$getConcertArtistsParams"><< Back</g:link>
                <p>${flash.message}</p>
            </div>
        </div>
    </div>
    <div class="row">
        <g:form name="addAllConcertArtistsTopTracksToNewPlaylistForm" controller="main" action="addAllConcertArtistsTopTracksToNewPlaylist">
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <label for="artists" class="control-label">Artists</label>
                    <g:select name="artists" multiple="true" from="${artists}" class="form-control"></g:select>
                    <br/>
                    <ul class="pagination pagination-sm">
                        <li><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                    </ul>
                    <button id="selectAll" type="button" class="btn btn-info">Select All</button>
                    <button id="unselectAll" type="button" class="btn btn-info">Unselect All</button>
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <label for="numberOfTracks" class="control-label">Number of Tracks per Artist</label>
                    <g:select name="numberOfTracks" from="${1..20}" value="5" class="form-control"></g:select>
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <label for="name" class="control-label">Name</label>
                    <g:textField name="name" value="${playlistName}" class="form-control"></g:textField>
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <g:submitButton name="addAllConcertArtistsTopTracksToNewPlaylistButton" value="Do it!" class="btn btn-info"></g:submitButton>
                </div>
            </div>
        </g:form>
    </div>

    <script>
        $("#selectAll").on("click", function() {
            $('#artists option').prop('selected', true);
        });
        $("#unselectAll").on("click", function() {
            $('#artists option').prop('selected', false);
        });

        var selectedArtists = [];

        $(".pagination a").on("click", function(e) {
            $.ajax({
                url: "/main/ajaxGetConcerts",
                data: {
                    page: $(this).text(),
                    <g:each in="${getConcertArtistsParams}" var="param">
                        ${param.key}: "${param.value}",
                    </g:each>
                }
            }).done(function(resp) {
                console.debug(resp);
            });
        });
    </script>
</body>
</html>
