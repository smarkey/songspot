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
                    <p><g:message code="com.spotkick.list.concerts.description"/></p>
                </div>
            </div>
            <div class="row">
                <div col="col-sm-12 col-lg-12">
                    <g:select name="artists" multiple="true" from="${artists}" class="form-control hidden"></g:select>
                </div>
            </div>
            <div class="row">
                <div col="col-sm-12 col-lg-12">
                    <input id="numberOfTracks" name="numberOfTracks" type="text" data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="1"/>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group">
                        <g:textField name="name" value="Spotkick Playlist" class="form-control"></g:textField>
                    </div>
                </div>
            </div>
            <div class="row">
                <div col="col-sm-12 col-lg-12">
                    <g:submitButton name="addAllConcertArtistsTopTracksToNewPlaylistButton" value="Create Playlist" class="btn btn-success"></g:submitButton>
                </div>
            </div>
        </g:form>
        </div>
    </div>

    <script>
        $(function(){
            $("#artists").multiselect({
                maxHeight: 400,
                enableFiltering: true,
                includeSelectAllOption: true,
                buttonText: function(options, select) {
                    return options.length+" artists selected";
                },
                onDropdownShow: function() {
                    var width = $("button.multiselect").width() + $("button.multiselect").css("padding-left").replace("px", "") * 2;
                    console.debug(width);
                    $(".btn-group .multiselect-container").width(width);
                }
            });
            $("#numberOfTracks").slider();
        });
    </script>
</body>
</html>
