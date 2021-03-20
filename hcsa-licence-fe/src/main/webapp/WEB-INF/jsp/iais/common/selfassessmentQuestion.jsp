<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/6
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:choose>
    <c:when test="${empty selfAssessmentDetail}">
        <br><br><br><br>
        <div id="intro">
            <p style="padding-left: 600px;"><strong margin-left: 250px;><c:out value="you have encountered some problems, please contact the administrator !!!"></c:out></strong></p>
            <br><br><br><br><br><br><br>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container">
            <br>
            &nbsp;<span id="error_noFillUpItemError" name="iaisErrorMsg" class="error-msg"></span>
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                            <c:forEach  var="selfAssessmentConfig" items="${selfAssessmentDetail.selfAssessmentConfig}" varStatus="status">
                                <c:choose>
                                    <c:when test="${selfAssessmentConfig.common}">
                                        <div class="panel panel-default">
                                            <div class="panel-heading" id="headingPremise${status.index}" role="tab">
                                                <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise${status.index}" aria-expanded="true"
                                                                           aria-controls="collapsePremise" class="">General Regulation</a></h4>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="panel panel-default">
                                            <div class="panel-heading" id="headingPremise${status.index}" role="tab">
                                                <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise${status.index}" aria-expanded="true"
                                                                           aria-controls="collapsePremise" class="">${selfAssessmentConfig.svcName}</a></h4>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <%@include file="/WEB-INF/jsp/iais/common/checklistAnswer.jsp"%>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>