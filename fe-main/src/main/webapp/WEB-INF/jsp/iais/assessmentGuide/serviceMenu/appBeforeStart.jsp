<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
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
        <h2>SERVICES SELECTED</h2>
        <ul class="service-list">
          <c:forEach var="baseItem" items="${baseSvcIdList}" varStatus="status">
            <li><span><iais:service value="${baseItem}"></iais:service></span> (<iais:code code="CDN002"/>)</li>
          </c:forEach>
          <c:forEach var="specifiedItem" items="${speSvcIdList}" varStatus="status">
            <li><span><iais:service value="${specifiedItem}"></iais:service></span> (<iais:code code="CDN004"/>)</li>
          </c:forEach>
        </ul>
       <%--
       <div class="gray-content-box">
          <div class="h3-with-desc">
            <h3>Licensee and Key Personnel</h3>
            <p>The following details are common to all services in your healthcare organisation. To make any changes, please contact your company administrator.</p>
          </div>
          <div class="license-info-gp">
            <div class="license-info-row">
              <div class="licnese-info">
                <p>Licensee: <strong>${licensee.name}</strong> </p>
              </div>
              <div class="license-edit">
                <p><a class="license-view" href="javascript:void(0);" onclick="popUplicensee('${licenseeurl}',0)">View</a></p>
              </div>
            </div>
            <c:if test="${!empty feUserDtos && 'LICT001'.equals(licensee.licenseeType)}">
              <c:forEach var="item" items="${feUserDtos}" varStatus="status">
                <div class="license-info-row">
                  <div class="licnese-info">
                    <p>Authorised Person ${(status.index + 1)}: <strong>${item.displayName}</strong> </p>
                  </div>
                  <div class="license-edit">
                    <p><a class="authorise-view" href="javascript:void(0);" onclick="popUplicensee('${authorisedUrl}','<iais:mask name="authorisedId${status.index}" value="${item.id}"/>','authorisedId${status.index}')">View</a></p>
                  </div>
                </div>
              </c:forEach>
            </c:if>

          </div>
        </div>
        --%>
        <h3>Before You Begin</h3>
        <ul class="">
          <li>
            <p>Please refer to <a style="text-decoration:none;" target="_blank" href="<iais:code code="MRUS022"/>"><iais:code code="MRUS022"/></a> for the list of documents needed during the licence application.</p>
          </li>
          <li>
            <p>This form will take approximately 30 minutes to complete. You may save your progress at any time and resume your application later. </p>
          </li>
          <li>
            <p>Payment may be made by credit card, debit card, PayNow, GIRO or eNETS.</p>
          </li>
          <li>
            <p>* denotes mandatory field.</p>
          </li>
        </ul>
        <div class="application-tab-footer">
          <div class="row">
            <div class="col-xs-12 col-sm-6">
              <a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-sm-6">
              <input type="text" style="display: none; " id="selectDraftNo" value="${selectDraftNo}">
              <div class="text-right text-center-mobile"><a class="btn btn-primary next" onclick="doNext()" data-toggle="modal" data-target= "#saveDraft" href="javascript:void(0);">Proceed</a></div>
            </div>
          </div>
        </div>
      </div>
    </form>
</div>
<script>
  function doNext() {
      $("input[name='switch_action_type']").val("startApplication");
      $("#mainForm").submit();
  }

  function doBack() {
      $("input[name='switch_action_type']").val("doBack");
      $("#mainForm").submit();
  }

  // $(".license-view").click(function () {
  //     $("input[name='switch_action_type']").val("showlicense");
  //     $("input[name='crud_action_additional']").val("Licensee");
  //     $("#mainForm").submit();
  // });
  //
  // $(".authorise-view").click(function () {
  //     $("input[name='switch_action_type']").val("showlicense");
  //     $("input[name='crud_action_additional']").val("Authorised");
  //     $("#mainForm").submit();
  // });
  //
  // $(".medAlert-view").click(function () {
  //     $("input[name='switch_action_type']").val("showlicense");
  //     $("input[name='crud_action_additional']").val("MedAlert");
  //     $("#mainForm").submit();
  // });

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