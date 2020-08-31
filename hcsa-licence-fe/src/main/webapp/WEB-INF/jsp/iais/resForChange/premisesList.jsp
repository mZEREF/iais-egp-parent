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
    <style>
      .col-xs-12.col-md-8.text-right>.nav{
        margin-right: 20%;
      }
    </style>
<div class="tab-pane" id="tabApp" role="tabpanel">
  <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" id="hiddenIndex" name="hiddenIndex" value="" />
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">

  <div class="tab-search col-xs-11" style="margin-left: 10%;margin-top:1%">
    <div class="form-group">
      <label class="control-label" for="premType">Type</label>
      <div class="col-xs-12 col-md-8 col-lg-9" id="premTypeDiv">
        <iais:select name="premType"  value="${premiseDoSearch}" options="applicationType" firstOption="All"></iais:select>
      </div>
    </div>
    <div class="form-group" style="position: absolute ;right: 1%" >
      <div class="col-xs-12 col-md-4 col-lg-9">
        <a class="btn btn-secondary" id="premiseClear">Clear</a>
        <a class="btn btn-primary" id="premiseSearch">search</a>
      </div>
    </div>

  </div>
  <div class="row col-xs-11 " style="margin-left: 10%">
    <div class="col-xs-12">
      <div class="table-gp">
        <span class="error-msg"><c:out value="${Error_Status}"/></span>
        <table class="table" style="margin-right: 20%">
          <iais:pagination  param="PremisesSearchParam" result="PremisesSearchResult"/>
          <thead>
            <tr>
            <th style="width: 40%">Address</th>
            <th style="width: 20%">Type</th>
            <th style="width: 40%">Licence</th>
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
            <c:if test="${prem.premisesType=='OFFSITE'}"><c:out value="Off-site"/></c:if>
          </td>
          <td><c:out value="${prem.svcId}"/></td>
        </tr>
      </c:forEach>
      <c:if test="${ACK018!=null}">
        <tr><td>${ACK018}</td></tr>
      </c:if>
        </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="row col-xs-11 ">
    <div class="col-xs-12">
      <a class="back" style="margin-left: 10%" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
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
    /*doSubmitForm('prePremisesEdit','', '');*/
      SOP.Crud.cfxSubmit("menuListForm", "prePremisesEdit");
  });
  
  $('#Back').click(function() {
  });

  function jumpToPagechangePage(){
      SOP.Crud.cfxSubmit("menuListForm", "doPage");
  }

  function sortRecords(sortFieldName,sortType){
      SOP.Crud.cfxSubmit("menuListForm","doSort",sortFieldName,sortType);
  }

  $('#premiseSearch').click(function () {
      SOP.Crud.cfxSubmit("menuListForm", "doSearch",$('#premType').val(),"");
  });
  $('#premiseClear').click(function () {

    $('#premType option:first').prop('selected', 'selected');
    $("#premTypeDiv .current").text("All");

  });
</script>