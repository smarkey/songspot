<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Spotkick</title>
</head>
<body>
    <div class="row">
        <g:if test="${flash.message}">
            <div col="col-sm-12">
                <div class="alert alert-danger">
                    ${flash.message}
                </div>
            </div>
        </g:if>
    </div>
    <div class="row">
        <div class="col-sm-12 center">
        <g:form name="addAllConcertArtistsTopTracksToNewPlaylistForm" controller="main" action="addAllConcertArtistsTopTracksToNewPlaylist">
            <div class="row">
                <div col="col-sm-12">
                    <p><g:message code="com.spotkick.list.fetivals.description" /></p>
                </div>
            </div>
            <div class="row">
                <div col="col-sm-12">
                    <select id="artists" name="artists" multiple="true" class="form-control hidden">
                        <g:each in="${festivals}" var="festival">
                            <optgroup label="${festival.key.replaceAll(" 2016", "")} (${festival.value.size()})">
                                <g:each in="${festival.value}" var="artist">
                                    <option value="${artist}">${artist}</option>
                                </g:each>
                            </optgroup>
                        </g:each>
                    </select>
                </div>
            </div>
            <div class="row">
                <div col="col-sm-12">
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
                <div col="col-sm-12">
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
                enableClickableOptGroups: true,
                enableCollapsibleOptGroups: true,
                disableIfEmpty: true,
                disabledText: "No festivals found",
                buttonText: function(options, select) {
                    return options.length+" artists selected";
                },
                onInitialized: function() {
                    var $li = $(".caret-container").closest('li');
                    var $inputs = $li.nextUntil("li.multiselect-group").not('.multiselect-filter-hidden');
                    $inputs.hide().addClass('multiselect-collapsible-hidden');
                    $(".multiselect-item a").click()
                },
                onDropdownShow: function() {
                    var width = $("button.multiselect").width() + $("button.multiselect").css("padding-left").replace("px", "") * 2;
                    console.debug(width);
                    $(".btn-group .multiselect-container").width(width);
                }
            });

            $("#numberOfTracks").slider({
                tooltip: 'show'
            });
        });
    </script>
</body>
</html>
