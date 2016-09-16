<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Spotkick</title>
</head>
<body>
    <div class="row">
        <g:if test="${flash.message}">
            <div col="col-sm-12 col-lg-12">
                <div class="alert alert-danger">
                    ${flash.message}
                </div>
            </div>
        </g:if>
    </div>
    <div class="row">
        <div class="col-sm-12 col-lg-12 center">
        <g:form name="addAllConcertArtistsTopTracksToNewPlaylistForm" controller="main" action="addAllConcertArtistsTopTracksToNewPlaylist">
            <div class="row">
            <div col="col-sm-12 col-lg-12">
                <p>Select Artists and the number of tracks by each artist to add to the playlist. Change the name of the Spotify playlist to create.</p>
            </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-12">
                <div class="form-group">
                    <g:select name="artists" multiple="true" from="${artists}" class="form-control hidden"></g:select>
                </div>
            </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-12">
                <div class="form-group">
                    <input id="numberOfTracks" name="numberOfTracks" type="text" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="1"/>
                </div>
            </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-12">
                <div class="form-group">
                    <g:textField name="name" value="${playlistName}" class="form-control"></g:textField>
                </div>
            </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-12">
                <div class="form-group">
                    <g:submitButton name="addAllConcertArtistsTopTracksToNewPlaylistButton" value="Do it!" class="btn btn-success"></g:submitButton>
                </div>
            </div>
            </div>
        </g:form>
        </div>
    </div>

    <script>
        $(function(){
            $("#artists").multiselect({
                maxHeight: 300,
                includeSelectAllOption: true,
                //enableFiltering: true,
                //enableClickableOptGroups: true,
                buttonText: function(options, select) {
                    return options.length+" artists selected";
                }
            });
            $("#numberOfTracks").slider();
        });
    </script>
</body>
</html>
