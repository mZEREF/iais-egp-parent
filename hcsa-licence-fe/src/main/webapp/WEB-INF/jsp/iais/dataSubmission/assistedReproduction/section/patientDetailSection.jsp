<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Details of Patient
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="suffix" value="" />
                <c:set var="person" value="${patient}" />
                <%@include file="personSection.jsp" %>
                <c:if test="${showPrevious}">
                    <c:set var="person" value="${previous}" />
                    <%@include file="previewPatientPreviousSection.jsp" %>
                </c:if>
                <c:if test="${isNew}">
                    <%@include file="patientPreviousSection.jsp" %>
                </c:if>
            </div>
        </div>
    </div>
</div>
<iais:confirm msg="DS_MSG006" callBack="$('#noPatientDiv').modal('hide');" popupOrder="noPatientDiv" needCancel="false"
              needFungDuoJi="false"/>
<iais:confirm msg="${ageMsg}" callBack="$('#ageMsgDiv').modal('hide');" popupOrder="ageMsgDiv" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />

