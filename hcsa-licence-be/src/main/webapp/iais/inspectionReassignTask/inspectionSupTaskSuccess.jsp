<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainSuccessForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Assign Task</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <c:if test = "${not empty errorMap}">
                      <iais:error>
                        <div class="error">
                          <c:forEach items="${errorMap}" var="map">
                            ${map.key}  ${map.value} <br/>
                          </c:forEach>
                        </div>
                      </iais:error>
                    </c:if>
                    <iais:section title="" id = "assign_Task">
                      <iais:row>
                        <iais:field value="Application No"/>
                        <iais:value width="7">
                          <label><c:out value="${inspectionTaskPoolListDto.applicationNo}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Application Type"/>
                        <iais:value width="7">
                          <label><iais:code code="${inspectionTaskPoolListDto.applicationType}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Application Status"/>
                        <iais:value width="7">
                          <label><iais:code code="${inspectionTaskPoolListDto.applicationStatus}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="HCI Code"/>
                        <iais:value width="7">
                          <label><c:out value="${inspectionTaskPoolListDto.hciCode}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="HCI Name / Address"/>
                        <iais:value width="7">
                          <label><c:out value="${inspectionTaskPoolListDto.hciName}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Service Name"/>
                        <iais:value width="7">
                          <label><c:out value="${inspectionTaskPoolListDto.serviceName}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Submission Date"/>
                        <iais:value width="7">
                          <label><fmt:formatDate value='${inspectionTaskPoolListDto.submitDt}' pattern='dd/MM/yyyy' /></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="${lead}"/>
                        <iais:value width="7">
                          <label><c:out value="${inspectionTaskPoolListDto.inspectorLeads}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="${user}"/>
                        <iais:value width="10">
                          <c:forEach items="${inspectionReassignTaskDto.inspectorCheck}" var="name">
                            <label><c:out value="${name.text}"/></label>
                          </c:forEach>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Reassign Reason"/>
                        <iais:value width="10">
                          <label><c:out value="${inspectionReassignTaskDto.reassignRemarks}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Inspection Type"/>
                        <iais:value width="7">
                          <label><c:out value="${inspectionTaskPoolListDto.inspectionTypeName}"/></label>
                        </iais:value>
                      </iais:row>
                    </iais:section>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </iais:body>
  </form>
</div>


