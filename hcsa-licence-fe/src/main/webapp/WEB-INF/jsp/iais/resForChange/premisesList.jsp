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
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<style>
    .col-xs-12.col-md-8.text-right>.nav{
        margin-right: 20%;
    }

    .premTypeDiv li {
        padding-top: 10px;
        padding-bottom: 10px;
    }
</style>
<div class="tab-pane" id="tabApp" role="tabpanel">
  <form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" id="crud_action_type_form_value" value="">
  <input type="hidden" id="hiddenIndex" name="hiddenIndex" value="" />
    <input type="hidden" name="crud_action_type" id="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" id="crud_action_value" value="">

  <div class="container form-horizontal" style="margin-top:1%">
    <iais:row cssClass="col-md-4">
      <label class="control-label float-left" for="premType">Type</label>
      <div class="col-xs-10 col-md-9 col-lg-8 premTypeDiv" id="premTypeDiv">
        <iais:select name="premType"  value="${premiseDoSearch}" options="applicationType" firstOption="All"></iais:select>
      </div>
    </iais:row>
    <iais:row cssClass="col-md-8">
      <div class="text-right">
        <a class="btn btn-secondary" id="premiseClear" href="javascript:void(0);">Clear</a>
        <a class="btn btn-primary" id="premiseSearch" href="javascript:void(0);">search</a>
      </div>
    </iais:row>
  </div>
  <div class="container" >
    <iais:pagination  param="PremisesSearchParam" result="PremisesSearchResult"/>
    <div class="col-xs-12">
        <span class="error-msg"><c:out value="${Error_Status}"/></span>
        <div class="table-responsive">
          <table aria-describedby="" class="table " style="margin-right: 20%">
            <thead style="align-content: center">
            <tr>
              <th scope="col" style="width: 40%">Address</th>
              <th scope="col" style="width: 20%">Type</th>
              <th scope="col" style="width: 40%">Licence</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="prem" items="${PremisesListDtos}" varStatus="status">
              <tr class="premTr">
                <td>
                  <a class="premAddr"><c:out value="${prem.address}"/></a>
                  <span class="premAddr"><em class="fa fa-pencil-square-o"></em></span>
                  <input type="hidden" class="statusIndex" name="statusIndex" value="${status.index}" />
                  <input type="hidden" class="licId" name="licId${status.index}" value="<iais:mask name="licId${status.index}" value="${prem.licenceId}" />" />
                  <input type="hidden" class="premisesId" name="premisesId${status.index}" value="<iais:mask name="premisesId${status.index}" value="${prem.premisesId}"/>" />
                </td>
                <td>
                  <c:if test="${prem.premisesType=='ONSITE'}"><c:out value="Premises"/></c:if>
                  <c:if test="${prem.premisesType=='CONVEYANCE'}"><c:out value="Conveyance"/></c:if>
                  <c:if test="${prem.premisesType=='OFFSITE'}"><c:out value="Off-site"/></c:if>
                  <c:if test="${prem.premisesType=='EASMTS'}"><c:out value="Conveyance (in a mobile clinic / ambulance)"></c:out></c:if>
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
    <div class="row col-xs-11 ">
      <div class="col-xs-12">
        <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
      </div>
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
      Utils.submit('menuListForm','prePremisesEdit','')
     /* SOP.Crud.cfxSubmit("menuListForm", "prePremisesEdit");*/
  });
  
  $('#Back').click(function() {
  });

  function jumpToPagechangePage(){
      Utils.submit('menuListForm','doPage','');
    /*  SOP.Crud.cfxSubmit("menuListForm", "doPage");*/
  }

  function sortRecords(sortFieldName,sortType){
      Utils.submit('menuListForm','doPage',sortFieldName,sortType,'');
    /*  SOP.Crud.cfxSubmit("menuListForm","doSort",sortFieldName,sortType);*/
  }

  $('#premiseSearch').click(function () {
      Utils.submit('menuListForm','doSearch',$('#premType').val(),'','');
     /* SOP.Crud.cfxSubmit("menuListForm", "doSearch",$('#premType').val(),"");*/
  });
  $('#premiseClear').click(function () {

    $('#premType option:first').prop('selected', 'selected');
    $("#premTypeDiv .current").text("All");

  });
</script>