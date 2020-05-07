    <%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
        <%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
        <%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
    <%@include file="dashboard.jsp"%>
    <%@include file="../common/dashboard.jsp" %>
<div class="tab-pane" id="tabApp" role="tabpanel">
  <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" id="hiddenIndex" name="hiddenIndex" value="" />

  <div class="row col-xs-11">
    <div class="col-xs-12" style="margin-left: 3%">
      <label>4 outof 4</label>
    </div>
  </div>
  <div class="tab-search col-xs-11" style="margin-left: 3%">
    <div class="form-group">
      <label class="control-label" for="premType">Type</label>
      <div class="col-xs-12 col-md-8 col-lg-9">
        <iais:select name="" id="premType" options="applicationType" firstOption="All"></iais:select>
      </div>
    </div>
    <div class="form-group" style="position: absolute ;right: 1%" >
      <div class="col-xs-12 col-md-4 col-lg-9">
        <a class="btn btn-secondary">Clear</a>
        <a class="btn btn-primary">search</a>
      </div>
    </div>

  </div>
  <div class="row col-xs-11 " style="margin-left: 3%">
    <div class="col-xs-12">
      <div class="table-gp">
        <span class="error-msg"><c:out value="${Error_Status}"/></span>
        <table class="table">
          <thead>
            <tr>
            <th>Address</th>
            <th>Type</th>
            <th>Licence</th>
            </tr>
          </thead>
        <tbody>
      <c:forEach var="prem" items="${PremisesListDtos}" varStatus="status">
        <tr class="premTr">
          <td>
            <a  class="premAddr"><c:out value="${prem.address}"/></a>
            <label ><em class="fa fa-pencil-square-o"></em></label>
            <input type="hidden" class="statusIndex" name="statusIndex" value="${status.index}" />
            <input type="hidden" class="licId" name="licId${status.index}" value="<iais:mask name="licId${status.index}" value="${prem.licenceId}" />" />
            <input type="hidden" class="premisesId" name="premisesId${status.index}" value="<iais:mask name="premisesId${status.index}" value="${prem.premisesId}"/>" />
          </td>
          <td>
            <c:if test="${prem.premisesType=='ONSITE'}"><c:out value="On-site"/></c:if>
            <c:if test="${prem.premisesType=='CONVEYANCE'}"><c:out value="Conveyance"/></c:if>
            <c:if test="${prem.premisesType=='OFFSIET'}"><c:out value="Off-site"/></c:if>
          </td>
          <td><c:out value="${prem.svcId}"/></td>
        </tr>
      </c:forEach>
        </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="row col-xs-11 ">
    <div class="col-xs-12">
      <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
    </div>
  </div>

  <div class="row">
  </div>
  </form>
</div>
<script>
  $(document).ready(function () {
  
      
  
  });

  $('.premAddr').click(function () {
    $premAddrEle = $(this).closest('tr.premTr');
    
    var index =  $premAddrEle.find('.statusIndex').val();
    $('#hiddenIndex').val(index);
    doSubmitForm('prePremisesEdit','', '');
  
  });
  
  $('#Back').click(function() {
  });
</script>