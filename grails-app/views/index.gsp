<%@ page import="org.joda.time.DateTime" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Spotkick</title>
</head>
<body>
    <p>${flash.message}</p>
    <g:form name="getConcertArtistsForm" controller="main" action="getConcertArtists">
        <h2>Get Concert Artists</h2>
        <label for="filterByArea" class="control-label">Filter by Area (default is by user)</label>
        <g:checkBox name="filterByArea"></g:checkBox>
        <br/>
        <div id="areaSection" class="hidden">
            <label for="area" class="control-label">Area</label>
            <g:select name="area" from="${grailsApplication.config.com.spotkick.songkick.areas as List}" disabled="true" class="control-label" />
            <br/>
        </div>
        <label for="startDate" class="control-label">Between</label>
        <g:datePicker name="startDate" value="${DateTime.now().toDate()}" precision="day" relativeYears="[0..1]" class="form-control"/>
        <br/>
        <label for="endDate" class="control-label">And</label>
        <g:datePicker name="endDate" value="${DateTime.now().plusMonths(1).toDate()}" precision="day" relativeYears="[0..1]" class="form-control"/>
        <br/>
        <label for="includeFestivals" class="control-label">Include Festivals</label>
        <g:checkBox name="includeFestivals" />
        <br/>
        <g:submitButton name="getConcertArtistsButton" value="Fetch"  class="btn btn-info"></g:submitButton>
    </g:form>

    <script>
        $(document).on("ready", function() {
            $("#filterByArea").on("change", function() {
                if (this.checked) {
                    $("#areaSection").removeClass("hidden");
                    $("#area").prop("disabled", false);
                } else {
                    $("#areaSection").addClass("hidden");
                    $("#area").prop("disabled", true);
                }
            });
        });
    </script>
</body>
</html>
