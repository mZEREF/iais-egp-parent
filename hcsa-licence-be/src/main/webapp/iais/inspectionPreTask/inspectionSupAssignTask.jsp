<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
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
  <form method="post" id="mainAssignForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="InspectionSupSearchSwitchType" value="">
    <input type="hidden" name="actionValue" value="">
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
                      <iais:field value="Application Number"/>
                      <iais:value width="7">
                        <label><c:out value="${inspectionTaskPoolListDto.applicationNo}"/></label>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Application Type"/>
                      <iais:value width="7">
                        <label><c:out value="${inspectionTaskPoolListDto.applicationType}"/></label>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Application Status"/>
                      <iais:value width="7">
                        <label><c:out value="${inspectionTaskPoolListDto.applicationStatus}"/></label>
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
                        <%--<iais:datePicker id = "submitDt" name = "submitDt" value="${inspecTaskCreAndAssDto.submitDt}"></iais:datePicker>--%>
                        <label><c:out value="${inspectionTaskPoolListDto.submitDt}"/></label>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Inspection Lead"/>
                      <iais:value width="7">
                        <label><c:out value="${inspectionTaskPoolListDto.inspectorLead}"/></label>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Inspector"/>
                      <iais:value width="10">
                        <c:if test="${inspectionTaskPoolListDto.inspectorCheck == null}">
                          <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                              <input type="checkbox" name="inspector" value="<c:out value="${name.value}"/>"/><label><c:out value="${name.text}"/></label>
                          </c:forEach>
                        </c:if>
                        <c:if test="${inspectionTaskPoolListDto.inspectorCheck != null}">
                          <c:forEach items="${inspectionTaskPoolListDto.inspectorOption}" var="name">
                            <input type="checkbox" name="inspector" value="<c:out value="${name.value}"/>"
                              <c:forEach items="${inspectionTaskPoolListDto.inspectorCheck}" var="checkName">
                                 <c:if test="${name.value eq checkName.value}">checked="checked"</c:if>
                              </c:forEach>
                            /><label><c:out value="${name.text}"/></label>
                          </c:forEach>
                        </c:if>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Inspection Type"/>
                      <iais:value width="7">
                        <label><c:out value="${inspectionTaskPoolListDto.inspectionTypeName}"/></label>
                      </iais:value>
                    </iais:row>
                    <iais:action >
                      <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doBack()">Back</button>
                      <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:doNext()">Next</button>
                    </iais:action>
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
<script type="text/javascript">
    function doBack() {
        $("[name='actionValue']").val('back');
        submit('back');
    }

    function doNext() {
        $("[name='actionValue']").val('confirm');
        submit('confirm');
    }
    function submit(action){
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

