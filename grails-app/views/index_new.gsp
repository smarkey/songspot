<%@ page import="org.joda.time.DateTime" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to SpotKick</title>
</head>
<body>
    <g:form name="getConcertsForm" controller="main" action="getConcerts">
        <h2>Get Concerts</h2>
        <g:submitButton name="getConcertsButton" value="Fetch"></g:submitButton>
    </g:form>
    <g:form name="getConcertsByDateRangeForm" controller="main" action="getConcertsByDateRange">
        <h2>Get Concerts By Date Range</h2>
        <label for="startDate">Start Date</label>
        <g:datePicker name="startDate" value="${new Date()}" precision="day" relativeYears="[0..1]" />
        <br />
        <label for="endDate">End Date</label>
        <g:datePicker name="endDate" value="${new Date()}" precision="day" relativeYears="[0..1]" />
        <br />
        <label for="includeFestivals">Include Festivals</label>
        <g:checkBox name="includeFestivals" checked="" />
        <br />
        <g:submitButton name="getConcertsByDateRangeButton" value="Fetch"></g:submitButton>
    </g:form>
    <g:form name="createPlaylistForm" controller="spotify" action="createPlaylist">
        <h2>Create Playlist</h2>
        <label for="name">Name</label>
        <g:textField name="name"></g:textField>
        <g:submitButton name="createPlaylistButton" value="Create"></g:submitButton>
    </g:form>
    <g:form name="findArtistByNameForm" controller="spotify" action="findArtistByName">
        <h2>Find Artist By Name</h2>
        <label for="name">Name</label>
        <g:textField name="name"></g:textField>
        <g:submitButton name="findArtistByNameButton" value="Find"></g:submitButton>
    </g:form>
    <g:form name="getArtistsTopTracksForm" controller="spotify" action="getArtistsTopTracks">
        <h2>Get Artists Top Tracks</h2>
        <label for="name">Name</label>
        <g:textField name="name"></g:textField>
        <g:submitButton name="getArtistsTopTracksButton" value="Find"></g:submitButton>
    </g:form>
    <g:form name="addAllConcertArtistsTopTracksToNewPlaylistForm" controller="main" action="addAllConcertArtistsTopTracksToNewPlaylist">
        <h2>Add All Concert Artists Top Tracks To New Playlist</h2>
        <label for="name">Name</label>
        <g:textField name="name"></g:textField>
        <g:submitButton name="addAllConcertArtistsTopTracksToNewPlaylistButton" value="Do it!"></g:submitButton>
    </g:form>
</body>
</html>
