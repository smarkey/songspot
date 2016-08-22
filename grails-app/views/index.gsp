<%@ page import="org.joda.time.format.DateTimeFormat; org.joda.time.DateTime" %>
<g:set var="now" value="${DateTime.now()}"/>
<g:set var="dateFormat" value="${DateTimeFormat.forPattern('dd/MM/yyyy')}" />
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Spotkick</title>
</head>
<body>
    <div class="row">
        <div col="col-sm-12 col-lg-3">
            <div class="form-group">
                <p>${flash.message}</p>
            </div>
        </div>
    </div>
    <div class="row">
        <g:form name="getConcertArtistsForm" controller="main" action="getConcertArtists">
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <label for="startDate" class="control-label">Between</label>
                    <div class="input-group date">
                        <input id="startDate" name="startDate" type="text" class="form-control" value="${now.toString(dateFormat)}">
                        <div class="input-group-addon">
                            <span class="glyphicon glyphicon-th"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <label for="endDate" class="control-label">And</label>
                    <div class="input-group date">
                        <input id="endDate" name="endDate" type="text" class="form-control" value="${now.plusMonths(1).toString(dateFormat)}">
                        <div class="input-group-addon">
                            <span class="glyphicon glyphicon-th"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <g:checkBox name="filterByArea" class="form-control switch"></g:checkBox>
                </div>
                <div class="form-group hidden" id="areaSection">
                    <label for="area" class="control-label">Area</label>
                    <g:select name="area" from="${grailsApplication.config.com.spotkick.songkick.areas as List}" disabled="true" class="form-control" />
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <g:checkBox name="includeFestivals" class="form-control switch" />
                </div>
            </div>
            <div col="col-sm-12 col-lg-3">
                <g:submitButton name="getConcertArtistsButton" value="Fetch"  class="btn btn-info"></g:submitButton>
            </div>
        </g:form>
    </div>
    <script>
        $(function () {
            $('.date').datepicker({
                format: 'dd/mm/yyyy'
            });

            $("#filterByArea").bootstrapSwitch({
                size: "small",
                onText: "Filtering by Area",
                offText: "Filtering by User",
                labelWidth: 0,
                offColor: "info",
                onSwitchChange: function(event, state) {
                    console.debug(state);
                    if(state == true) {
                        $("#areaSection").removeClass("hidden");
                        $("#area").prop("disabled", false);
                    } else {
                        $("#areaSection").addClass("hidden");
                        $("#area").prop("disabled", true);
                    }
                }
            });

            $("#includeFestivals").bootstrapSwitch({
                size: "small",
                onText: "Including festivals",
                offText: "Not including festivals",
                labelWidth: 0,
                offColor: "info"
            });
        });
    </script>
</body>
</html>
