<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="panel panel-default">
    <div class="panel-heading" id="headingPremise" role="tab">
        <h4 class="panel-title"><a role="button" class="collapsed" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Mode of Service Delivery</a></h4>
    </div>
    <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}"
                       varStatus="status">
                <iais:row>
                    <div class="app-title">Mode of Service Delivery</div>
                </iais:row>
                <div class="panel-main-content form-horizontal min-row">
                    <%@include file="/WEB-INF/jsp/iais/view/premises/viewPremisesContent.jsp"%>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
