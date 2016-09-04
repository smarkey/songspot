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
        $(function(){
            var scrollList = $("#artists").bootstrapDualListbox();
            var page = 1;

            $("#bootstrap-duallistbox-nonselected-list_artists").on("scroll", function(e) {
                var scrollHeight = $(this).prop('scrollHeight');
                var divHeight = $(this).height();
                var scrollerEndPoint = scrollHeight - divHeight - 14;
                var divScrollerTop =  $(this).scrollTop();

                console.debug(divScrollerTop, scrollerEndPoint);
                if(divScrollerTop >= scrollerEndPoint) {
                    page += 1;

                    $.ajax({
                        url: "/main/ajaxGetConcerts",
                        data: {
                            page: page,
                            <g:each in="${getConcertArtistsParams}" var="param">
                            ${param.key}:
                            "${param.value}",
                            </g:each>
                        }
                    })
                    .done(function (resp) {
                        console.debug(resp.artistNames.join("\n"));
                        scrollList.append(resp.artistNames.join("\n"));
                        scrollList.bootstrapDualListbox('refresh');
                    });
                }
            });
        });
    </script>
</body>
</html>
