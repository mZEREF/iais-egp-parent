<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/6
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<webui:setLayout name="iais-internet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%@include file="../common/dashboard.jsp"%>


<br><br>
        <div class="main-content">
            <div class = "container">
                <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
                    <input id="listLength" type="hidden" value="${fn:length(selfAssessmentQueryAttr)}"/>
                    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
                    <input type="hidden" id="selfAssessmentCorrId" name="selfAssessmentCorrId" value="">
                    <input type="hidden" id="assessmentBySvcId" name="assessmentBySvcId" value="">
                    <div class="personal-info-section">
                        &nbsp;<span id="error_noFillUpItemError" name="iaisErrorMsg" class="error-msg"></span>
                        <c:if test="${hasSubmitted eq 'Y'}">
                            <span id="hasSubmitted" name="hasSubmitted" class="error-msg"><c:out value="${hasSubmittedMsg}"></c:out></span>
                        </c:if>

                            <table class="table" border="1">
                                <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Premises</th>
                                    <th>Service</th>
                                    <th>Action</th>
                                </tr>
                                <tbody>
                                <c:forEach var="selfAssessment" items="${selfAssessmentQueryAttr}" varStatus="status">
                                    <tr>
                                            <td>${status.index + 1}</td>
                                            <td><c:out value="${selfAssessment.premises}"></c:out></td>
                                            <td><c:out value="${selfAssessment.svcName}"></c:out></td>
                                            <td>
                                            <c:choose>
                                                <c:when test="${hasSubmitted == 'Y'}">
                                                    <button type="button" id="viewSelfAssessment${status.index}" value="<iais:mask name="selfAssessmentCorrId" value="${selfAssessment.corrId}"/>" class="btn btn-default btn-md" >View</button>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${selfAssessment.canEdit == true}">
                                                        <button type="button" id="editSelfAssessment${status.index}" value="<iais:mask name="selfAssessmentCorrId" value="${selfAssessment.corrId}"/>" class="btn btn-default btn-md" >Fill</button>
                                                    </c:if>

                                                    <c:if test="${selfAssessment.canEdit == false}">
                                                        <button type="button" id="viewSelfAssessment${status.index}" value="<iais:mask name="selfAssessmentCorrId" value="${selfAssessment.corrId}"/>" class="btn btn-default btn-md" >View</button>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        <button type="button" id="printSelfAssessment${status.index}" value="<iais:mask name="selfAssessmentCorrId" value="${selfAssessment.corrId}"/>" class="btn btn-default btn-md" >Print</button></td>
                                    </tr>




                                </c:forEach>
                                </tbody>
                            </table>

                        <iais:action >
                            <p class="print">
                                <c:choose>
                                    <c:when test="${selfDeclAction eq 'new'}">
                                        <a class="back" id="backLastPageId" style="text-decoration:none" ><em class="fa fa-angle-left"> </em> Back</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a  class="back" id="backLink" style="text-decoration:none" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"> </em> Back</a>
                                    </c:otherwise>
                                </c:choose>
                                <a class="btn btn-primary next <c:if test="${fn:length(selfAssessmentQueryAttr) == 0 || hasSubmitted eq 'Y'}"> disabled</c:if>" id="submitAllDataButtonId" style="float:right" href="javascript:void(0);">Submit</a>

                            </p>
                        </iais:action>
                            <br><br>
                    </div>

                </form>
            </div>
        </div>

        <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
        <%@include file="/WEB-INF/jsp/include/utils.jsp"%>
        <script>
                var length = $('#listLength').val();
                for (var i = 0; i < length; i++){
                $("#editSelfAssessment" + i).click(function (){
                     Utils.markSubmit('mainForm','loadSelfAssessment', 'selfAssessmentCorrId', $(this).val())
                  });

                $("#viewSelfAssessment" + i).click(function () {
                    Utils.markSubmit('mainForm','viewSelfAssessment', 'selfAssessmentCorrId', $(this).val())
                });
                $("#printSelfAssessment" + i).click(function () {
                    showPopupWindow('/hcsa-licence-web/eservice/INTERNET/MohSelfAssessmentSubmit/1/viewSelfAssessment?selfAssessmentCorrId='
                        + $(this).val() + '&loadPopupPrint=Y');  //for page "selfassessmentQuestion.jsp"
                });
            }

            submitAllDataButtonId.onclick = (function () {
                Utils.submit("mainForm", "submitAllSelfAssessment")
            });
        </script>
