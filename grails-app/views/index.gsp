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
        <g:if test="${flash.message}">
            <div class="col-sm-12">
                <div class="alert alert-danger">
                    ${flash.message}
                </div>
            </div>
        </g:if>
    </div>
    <div class="row">
        <div class="col-sm-12 center">
            <g:form name="getConcertArtistsOrFestivalsForm" controller="main" action="getConcertArtistsOrFestivals">
                <div class="row">
                    <div class="col-sm-12">
                        <p><g:message code="com.spotkick.index.description"/></p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div id="dateRange" class="pull-right">
                            <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
                            <span></span> <b class="caret pull-right"></b>
                        </div>
                        <input id="startDate" name="startDate" class="form-control" value="${now.toString(dateFormat)}" type="hidden">
                        <input id="endDate" name="endDate" class="form-control" value="${now.plusMonths(1).toString(dateFormat)}" type="hidden">
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="input-group margin-bottom">
                            <span class="input-group-addon no-padding" id="basic-addon1">
                                <g:checkBox name="filterBy" class="form-control switch no-border"></g:checkBox>
                            </span>
                            <g:select name="location" from="${grailsApplication.config.com.spotkick.songkick.areas as List}" disabled="false" class="form-control" aria-describedby="basic-addon1"/>
                            <g:textField name="user" value="steven-markey-1" disabled="true" class="form-control hidden" aria-describedby="basic-addon1"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12 center">
                        <g:submitButton name="getArtistsButton" value="Get Performing Artists"  class="btn btn-success margin-bottom"></g:submitButton>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12 center">
                        <g:submitButton name="getFestivalsButton" value="Get Festivals"  class="btn btn-success margin-bottom"></g:submitButton>
                    </div>
                </div>
            </g:form>
        </div>
    </div>

    <script>
        $(function () {
            var start = moment();
            var end = moment().add(1, 'month');

            function cb(start, end) {
                $("#startDate").val(start.format('DD/MM/YYYY'));
                $("#endDate").val(end.format('DD/MM/YYYY'));
                $("#dateRange span").html(start.format('DD/MM/YYYY') + ' - ' + end.format('DD/MM/YYYY'));
            }

            $('#dateRange').daterangepicker({
                startDate: start,
                endDate: end,
                ranges: {
                    'Today': [moment(), moment()],
                    'Tomorrow': [moment().add(1, 'days'), moment().add(1, 'days')],
                    'This Week': [moment(), moment().add(1, 'weeks')],
                    'This Month': [moment(), moment().add(1, 'month')],
                    'Next Month': [moment().add(1, 'months').startOf('month'), moment().add(1, 'months').endOf('month')],
                    'Next 6 Months': [moment(), moment().add(6, 'month').endOf('month')]
                }
            }, cb);

            cb(start, end);

            $("#filterBy").bootstrapSwitch({
                size: "small",
                onText: "User",
                offText: "Location",
                labelWidth: 0,
                offColor: "info",
                onSwitchChange: function(event, state) {
                    if(state == true) {
                        $("#location").addClass("hidden").prop("disabled", true);
                        $("#user").removeClass("hidden").prop("disabled", false);
                    } else {
                        $("#location").removeClass("hidden").prop("disabled", false);
                        $("#user").addClass("hidden").prop("disabled", true);
                    }
                }
            });
        });
    </script>
</body>
</html>
