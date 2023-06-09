<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<div class="main-content">
  <div class="container">
    <form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="switch_action_type" value="">
      <input type="hidden" name="crud_action_value" value="">
      <input type="hidden" name="crud_action_additional" value="">
      <c:set var="loginContext" value="${iais_Login_User_Info_Attr}"/>
      <br>
          <div class="navigation-gp">
            <%@ include file="../../common/dashboardDropDown.jsp" %>
              <div class="col-xs-12">
                <div class="dashboard-page-title">
                  <h1>New Licence Application</h1>
                </div>
              </div>
      </div>
      <div class="instruction-content center-content">
        <h2>Service Selected</h2>
        <ul class="service-list">
          <c:forEach var="baseItem" items="${baseSvcIdList}" varStatus="status">
            <li><span><iais:service value="${baseItem}"></iais:service></span> <%--(<iais:code code="CDN002"/>)--%></li>
          </c:forEach>
        </ul>
        <h3>Before You Begin</h3>
        <ul class="">
          <li>
            <p>Please refer to <a style="text-decoration:none;" target="_blank" href="<iais:code code="MRUS022"/>"><iais:code code="MRUS022"/></a> for the list of documents required during the licence application.</p>
          </li>
          <li>
            <p>This form will take approximately 30 minutes to complete. You may save your progress at any time and resume your application later. </p>
          </li>
          <li>
            <p>Payment may be made by credit card, debit card, PayNow, GIRO or eNETS.</p>
          </li>
          <li>
            <p>If you have selected more than one licensable service, it has to be provided via the same mode of service delivery.</p>
          </li>
          <li>
            <p>"<span style="color: red">*</span>" denotes mandatory field.</p>
          </li>
        </ul>
        <div class="application-tab-footer">
          <div class="row">
            <div class="col-xs-12 col-sm-6">
              <a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-sm-6">
              <input type="text" style="display: none; " id="selectDraftNo" value="<c:out value="${selectDraftNo}"/>">
              <div class="text-right text-center-mobile"><a class="btn btn-primary next" onclick="doNext()" data-toggle="modal" data-target= "#saveDraft" href="javascript:void(0);">Proceed</a></div>
            </div>
          </div>
        </div>
      </div>
      <%--  <c:if test="${ not empty selectDraftNo }">
            <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
        </c:if>--%>
    </form>
  </div>
</div>

<script>
  function doNext() {
    showWaiting();
    $("input[name='switch_action_type']").val("startApplication");
    $("#mainForm").submit();
  }

  function doBack() {
    showWaiting();
    $("input[name='switch_action_type']").val("doBack");
    $("#mainForm").submit();
  }

  function saveDraft() {

  }

  function cancelSaveDraft() {

  }

  function popUplicensee(url,id,name){
    if(id != 0){
      url = url + "&"+name+"=" +id+ "&name=" + name+"&licenseeCompanyflag=pop";
    }else{
      url = url + "&licenseeCompanyflag=pop";
    }
    var scrWidth=screen.availWidth;
    var scrHeight=screen.availHeight;
    var self = window.open(url,"_blank", "scrollbars=yes,resizable=1,modal=false,alwaysRaised=yes");
    self.moveTo(0,0);
    self.resizeTo(scrWidth*.8,scrHeight*.8);
  }
</script>