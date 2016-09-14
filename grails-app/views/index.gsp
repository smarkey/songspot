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
        <div col="col-sm-12 col-lg-3">
            <div class="form-group">
                <p>${flash.message}</p>
            </div>
        </div>
        </g:if>
    </div>
    <div class="row">
        <div class="col-sm-12 col-lg-3 center">
        <g:form name="getConcertArtistsForm" controller="main" action="getConcertArtists">
            <div class="row">
            <div col="col-sm-12 col-lg-3">
                <p>Select a Date Range, filter concert results by User or Location and decide if you want to include Festivals.</p>
            </div>
            </div>
            <div class="row">
                <div col="col-sm-12 col-lg-3">
                    <div id="dateRange" class="pull-right">
                        <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
                        <span></span> <b class="caret pull-right"></b>
                    </div>
                </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <g:checkBox name="filterByArea" class="form-control switch"></g:checkBox>
                </div>
                <div class="form-group hidden" id="areaSection">
                    <g:select name="area" from="${grailsApplication.config.com.spotkick.songkick.areas as List}" disabled="true" class="form-control" />
                </div>
            </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-3">
                <div class="form-group">
                    <g:checkBox name="includeFestivals" class="form-control switch" />
                </div>
            </div>
            </div>
            <div class="row">
            <div col="col-sm-12 col-lg-3">
                <input id="startDate" name="startDate" class="form-control" value="${now.toString(dateFormat)}" type="hidden">
                <input id="endDate" name="endDate" class="form-control" value="${now.plusMonths(1).toString(dateFormat)}" type="hidden">
                <g:submitButton name="getConcertArtistsButton" value="Get Gigging Artists"  class="btn btn-success"></g:submitButton>
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
