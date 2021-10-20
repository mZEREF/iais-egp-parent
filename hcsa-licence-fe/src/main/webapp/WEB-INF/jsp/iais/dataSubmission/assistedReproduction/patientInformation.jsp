<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<c:set var="title" value="Joelle Lim" />
<c:set var="smallTitle" value="Joelle Lim" />

<%@ include file="arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please key in patient information</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/patientDetailSection.jsp" %>
                </div>
                <%@include file="arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
<script>
    $(document).ready(function() {
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        function cancel() {
            $('#saveDraft').modal('hide');
        }

        function jumpPage() {
            submit('premises','saveDraft','jumpPage');
        }

        $('#backBtn').click(function () {
            showWaiting();
            submit('jump','back',null);
        });

        $('#nextBtn').click(function () {
            showWaiting();
            submit('preview','next','');
        });

    });
</script>