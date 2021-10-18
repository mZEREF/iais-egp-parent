<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<c:if test="${appeal=='appeal'}">
  <webui:setLayout name="iais-blank"/>
  <%@ include file="../common/dashboard.jsp" %>
</c:if>
<c:if test="${appeal!='appeal'}">
  <webui:setLayout name="iais-internet"/>
  <%@ include file="../common/dashboard.jsp" %>
</c:if>


<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>


  <div class="container">
    <div class="row">
      <div class="col-xs-12">

        <div class="tab-gp steps-tab">
          <div class="tab-content">
            <div class="tab-pane active" id="previewTab" role="tabpanel">
              <div class="preview-gp">
                <c:if test="${appeal!='appeal'}">
                  <div style="font-size: 16px">
                    <p class="print" style="margin-left: 90%">
                      <a onclick="printRLPDF()"><em class="fa fa-print"></em>Print</a>
                    </p>
                  </div>
                </c:if>
                <div class="row">
                  <div class="col-xs-12">
                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                      <%@include file="../common/previewLicensee.jsp"%>
                      <%@include file="../common/previewPremises.jsp"%>
                      <%@include file="../common/previewPrimary.jsp"%>
                      <div class="panel panel-default svc-content">
                        <div class="panel-heading"  id="headingServiceInfo" role="tab">
                          <h4 class="panel-title"><a class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${status.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information </a></h4>
                        </div>

                        <div class=" panel-collapse collapse" id="collapseServiceInfo" role="tabpanel" aria-labelledby="headingServiceInfo">
                          <div class="panel-body">
                            <p class="mb-0">
                            <div class="text-right app-font-size-16">
                              <%--<a href="#" id="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a>--%>
                            </div>
                            </p>
                            <div class="panel-main-content">
                              <%@include file="../common/previewSvcInfo.jsp"%>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
              <div class="application-tab-footer">
                <div class="row">
                  <div class="col-xs-12 col-sm-3">
                      <c:if test="${appeal!='appeal'}">
                        <a id = "Back" class="back" href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initLic" ><em class="fa fa-angle-left"></em> Back</a>
                      </c:if>
                  </div>
                  <div class="col-xs-12 col-sm-3">
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
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>
<script>
    function printRLPDF() {
        var url ='${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFePrintView",request)%>';
        if (url.indexOf('?') > 0 ) {
            url += '&';
        } else {
            url += '?';
        }
        url += 'licenceView=licenceView';
        window.open(url,'_blank');
    }
</script>

