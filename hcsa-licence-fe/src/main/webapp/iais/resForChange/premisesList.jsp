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
  <h1>Premises List</h1>
<div class="tab-pane" id="tabApp" role="tabpanel">
  <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" id="hiddenIndex" name="hiddenIndex" value="" />

  <div class="row col-xs-11">
    <div class="col-xs-12">
      <label>4 outof 4</label>
    </div>
  </div>
  <div class="tab-search col-xs-11">
    <div class="form-group">
      <label class="control-label" for="premType">Type</label>
      <div class="col-xs-12 col-md-8 col-lg-9">
        <iais:select name="" id="premType" options="" firstOption="All"></iais:select>
      </div>
    </div>
  </div>
  <div class="row col-xs-11 ">
    <div class="col-xs-12">
      <div class="table-gp">
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
            <label ><i class="fa fa-pencil-square-o"></i></label>
            <input type="hidden" class="statusIndex" name="statusIndex" value="${status.index}" />
            <input type="hidden" class="licId" name="licId${status.index}" value="<iais:mask name="licId${status.index}" value="${prem.licenceId}" />" />
            <input type="hidden" class="premisesId" name="premisesId${status.index}" value="<iais:mask name="premisesId${status.index}" value="${prem.premisesId}"/>" />
          </td>
          <td><c:out value="${prem.premisesType}"/></td>
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
    <a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a>
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