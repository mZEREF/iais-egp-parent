<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/9/16
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
  <form class="form-horizontal" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <br/><br/><br/><br/>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <div class="container">
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="tab-content">
            <h2 class="component-title">Import Users</h2>
            <span class="error-msg" name="iaisErrorMsg" id="error_userRoleUploadError"></span>
            <div class="table-gp">
              <table aria-describedby="" class="table application-group">
                <thead>
                  <tr>
                    <th scope="col" style="width:10%;">No.</th>
                    <th scope="col" style="width:15%;">User ID</th>
                    <th scope="col" style="width:15%;">Role ID</th>
                    <th scope="col" style="width:50%;">Group ID</th>
                    <th scope="col" style="width:10%;">Status</th>
                  </tr>
                </thead>
                <tbody>
                  <c:choose>
                    <c:when test="${'FAIL' eq ackSuccessFlag}">
                      <c:forEach var="user" items="${egpUserRoleDtos}" varStatus="status">
                        <tr>
                          <td>
                            <p><c:out value="${status.count}"></c:out></p>
                          </td>
                          <td>
                            <p><c:out value="${user.userId}"></c:out></p>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_userRoleUploadUserId${status.count}"></span>
                          </td>
                          <td>
                            <p><c:out value="${user.roleId}"></c:out></p>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_userRoleUploadRole${status.count}"></span>
                          </td>
                          <td>
                            <p><c:out value="${user.workGroupId}"></c:out></p>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_userRoleUploadWorkGroupId${status.count}"></span>
                          </td>
                          <td>
                            <p><c:out value="FAIL"></c:out></p>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:when>
                    <c:when test="${'SUCCESS' eq ackSuccessFlag}">
                      <c:forEach var="user" items="${egpUserRoleDtos}" varStatus="status">
                        <tr>
                          <td>
                            <p><c:out value="${status.count}"></c:out></p>
                          </td>
                          <td>
                            <p><c:out value="${user.userId}"></c:out></p>
                          </td>
                          <td>
                            <p><c:out value="${user.roleId}"></c:out></p>
                          </td>
                          <td>
                            <p><c:out value="${user.workGroupId}"></c:out></p>
                          </td>
                          <td>
                            <p><c:out value="SUCCESS"></c:out></p>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                      <tr>
                        <td>
                          <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                          <!--No Record!!-->
                        </td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <iais:action>
      <a style="margin-left: 16%" class="back" href="#" onclick="submit()"><em class="fa fa-angle-left"></em> Back</a>
    </iais:action>
  </form>
</div>

<style>
  .form-horizontal p {
    line-height: 10px;
  }
</style>



<script type="text/javascript">
  function submit(action) {
    $("#mainForm").submit();
  }
</script>