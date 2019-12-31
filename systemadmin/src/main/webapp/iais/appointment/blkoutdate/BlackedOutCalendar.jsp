<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/28/2019
  Time: 2:21 PM
  To change this template use File | Settings | File Templates.
--%><%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%
  String webroot=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
%>

<style>

</style>
<webui:setLayout name="iais-intranet"/>
<%@ include file="../common/mainContent.jsp" %>


<script>
    document.addEventListener('DOMContentLoaded', function () {
        var Calendar = FullCalendar.Calendar;
        var calendarEl = document.getElementById('calendar');
        var calendar = new Calendar(calendarEl, {
            plugins: [ 'interaction', 'dayGrid', 'timeGrid', 'list' ],
            dateClick: function(info) {
                // change the day's background color just for fun
                info.dayEl.style.backgroundColor = '#808080';
            }
        });

        calendar.render();
    });


</script>

