<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-cc"/>

<iais:error>
  <c:if test = "${not empty errorMap}">
    <div class="error">
      <c:forEach items="${errorMap}" var="map">
        ${map.key}  ${map.value} <br/>
      </c:forEach>
    </div>
  </c:if>
</iais:error>

<%@ include file="../dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <div class="tab-content">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <div class="row">
                  <div class="col-xs-12">
                    <div class="new-premise-form-conveyance">
                      <div class="form-horizontal">
                        <iais:row>
                          <iais:field value="UEN:" width="11"/>
                          <iais:value width="11">
                            <iais:input type="text" name="uenNo" id="uenNo" value="${orgUserDto.uenNo}"/>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="UIN:" width="11"/>
                          <iais:value width="11">
                            <iais:input type="text" name="idNumber" id="idNumber" value="${orgUserDto.idNumber}"/>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Salutation:" width="11"/>
                          <iais:value width="11">
                            <iais:select name="salutation" id="salutation" value="${orgUserDto.salutation}" codeCategory="CATE_ID_SALUTATION" firstOption="Please Select"/>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="First Name:" width="11"/>
                          <iais:value width="11">
                            <iais:input type="text" name="firstName" id="firstName" value="${orgUserDto.firstName}"/>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Last Name:" width="11"/>
                          <iais:value width="11">
                            <iais:input type="text" name="lastName" id="lastName" value="${orgUserDto.lastName}"/>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Email:" width="11"/>
                          <iais:value width="11">
                            <iais:input type="text" maxLength="320" name="emailAddr" id="emailAddr" value="${orgUserDto.emailAddr}"/>
                          </iais:value>
                        </iais:row>
                        <iais:action>
                          <button type="button" class="search btn" onclick="javascript:validation();">Submit</button>
                        </iais:action>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>

<script type="text/javascript">
  function validation(){
    SOP.Crud.cfxSubmit("mainForm", "validation");
  }

</script>