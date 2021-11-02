<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2021/10/29
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
  String continueURL = "";
  if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
    continueURL = process.runtime.continueURL();
  }
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/patientInformation.js"></script>

<form method="post" id="mainForm" action=<%=continueURL%>>
  <div class="main-content">
    <div class="container center-content">
      <div class="col-xs-12">
        <h3>Preview & Submit</h3>
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <strong>
                  Freezing
                </strong>
              </h4>
            </div>
            <div id="patientDetails" class="panel-collapse collapse in">
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <iais:row>
                    <iais:field value="What was cryopreserved?" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <c:forEach items="${freeCryoOptions}" var="freeCryo">
                        <span style="font-size: 16px"><iais:code code="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedType}"/></span>
                      </c:forEach>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. Cryopreserved" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <span style="font-size: 16px"><c:out value="${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedNum}"/></span>
                      <br>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Cryopreservation Date" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <fmt:formatDate value='${arSuperDataSubmissionDto.arSubFreezingStageDto.cryopreservedDate}' pattern='dd/MM/yyyy' />
                    </iais:value>
                  </iais:row>
                </div>
                <%@include file="common/arFooter.jsp" %>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>