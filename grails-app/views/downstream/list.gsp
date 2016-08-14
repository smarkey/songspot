<%@ page import="org.joda.time.DateTime" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Spotkick</title>
</head>
<body>
    <p>${flash.message}</p>
    <g:form name="addAllConcertArtistsTopTracksToNewPlaylistForm" controller="main" action="addAllConcertArtistsTopTracksToNewPlaylist">
        <label for="artists" class="control-label">Artists</label>
        <g:select name="artists" multiple="true" from="${artists}" class="form-control"></g:select>
        <br/>
        <button id="selectAll" type="button" class="btn btn-info">Select All</button>
        <button id="unselectAll" type="button" class="btn btn-info">Unselect All</button>
        <br/>
        <br/>
        <label for="numberOfTracks" class="control-label">Number of Tracks per Artist</label>
        <g:select name="numberOfTracks" from="${1..20}" value="5" class="form-control"></g:select>
        <br />
        <label for="name" class="control-label">Name</label>
        <g:textField name="name" value="test${DateTime.now()}"></g:textField>
        <g:submitButton name="addAllConcertArtistsTopTracksToNewPlaylistButton" value="Do it!" class="btn btn-info"></g:submitButton>
    </g:form>

    <script>
        $("#selectAll").on("click", function() {
            $('#artists option').prop('selected', true);
        });
        $("#unselectAll").on("click", function() {
            $('#artists option').prop('selected', false);
        });
    </script>
</html>
