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
        <br><br><br>
        <div class="row">
            <div class="col-xs-12">
                &nbsp;<span id="error_noFillUpItemError" name="iaisErrorMsg" class="error-msg"></span>
                <br><br>
                <div class="dashboard-gp">

      
                    <c:forEach  var="declItem" items="${selfAssessmentDetail.selfAssessmentConfig}" varStatus="status">
                        <c:choose>
                            <c:when test="${declItem.common eq true}">
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile" id="myBody">
                                        <a data-tab="#tabInbox" href="javascript:switchNextStep('<iais:mask name="tabIndex" value="${declItem.configId}"/>');">
                                            <p class="dashboard-txt">General Regulation</p>
                                        </a>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#tabInbox" href="javascript:switchNextStep('<iais:mask name="tabIndex" value="${declItem.configId}"/>');">
                                            <p class="dashboard-txt">${declItem.svcName}</p>
                                        </a>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="tab-content">
            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="table-gp">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>No.</th>
                                    <th>Regulation Clause Number</th>
                                    <th>Item</th>
                                    <th>Yes</th>
                                    <th>No</th>
                                    <th>N/A</th>
                                </tr>
                                </thead>
                                <tbody id="general">
                                <%@include file="/WEB-INF/jsp/iais/common/checklistAnswer.jsp"%>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <br>
    </c:otherwise>
</c:choose>
