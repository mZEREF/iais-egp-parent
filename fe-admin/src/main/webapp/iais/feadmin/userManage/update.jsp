<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-cc"/>
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
                            <iais:input type="text" name="uenNo" id="uenNo" value=""/>
                          </iais:value>
                        </iais:row>
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