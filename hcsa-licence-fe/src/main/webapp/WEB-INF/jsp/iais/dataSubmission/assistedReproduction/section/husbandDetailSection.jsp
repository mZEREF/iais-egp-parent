<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Details of Husband
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="suffix" value="Hbd" />
                <c:set var="person" value="${husband}" />
                <%@include file="personSection.jsp" %>
            </div>
        </div>
    </div>
</div>