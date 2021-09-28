<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>


<div class="main-content">
<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">

  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <div class="col-lg-12 col-xs-12">
    <div class="center-content">
      <div class="intranet-content">
        <div class="bg-title">
                <h2>Add KPI And Reminder Threshold</h2>
        </div>

      <div class="form-group">
        <div>
          <label class="col-xs-12 col-md-4 control-label">Module</label>
          <div class="col-xs-8 col-sm-6 col-md-5" style="margin-bottom: 1%">
            <select  name="module"  id="module" >
              <option  value="">Please Select</option>
              <c:forEach items="${selectOptionList}" var="selectOption">
                <option <c:if test="${module==selectOption.value}">selected="selected"</c:if>  value="${selectOption.value}" >${selectOption.text}</option>
              </c:forEach>

            <%--  <option   <c:if test="${module=='APTY007'}">selected="selected"</c:if> value="APTY007">Suspension</option>--%>
            </select>
            <span name="iaisErrorMsg" id="error_module" class="error-msg"></span>
          </div>
        </div>

      </div>

           <div class="form-group">
             <div style="margin-top: 1%;margin-bottom: 1%" >
               <label class="col-xs-12 col-md-4 control-label">Service</label>
               <div class="col-xs-8 col-sm-6 col-md-5" style="margin-top: 1%;margin-bottom: 1%">
                 <select name="service"  id="service">
                   <option value="" >Please Select</option>
                   <c:forEach items="${hcsaServiceDtos}" var="hcsaServiceDto">
                     <option value="${hcsaServiceDto.svcCode}"   <c:if test="${service==hcsaServiceDto.svcCode}">selected="selected" </c:if>>${hcsaServiceDto.svcDesc}</option>
                   </c:forEach>
                 </select>
                 <span name="iaisErrorMsg" id="error_service" class="error-msg"></span>
               </div>
             </div>

           </div>

  <div >
    <table aria-describedby="" class="table"  border="1px" style="border-collapse: collapse;">

        <tr>
            <th scope="col" rowspan="9" width="20%" style="text-align:center;vertical-align:middle;font-weight:normal;" >Processing Time for different stages</th>
            <th scope="col" style="text-align:center;vertical-align:middle;font-weight:normal;">Reminder Threshold</th>
            <th scope="col" style="vertical-align:middle;font-weight:normal;">
                <input name="reminderThreshold" style="margin-left: 20%;width: 50%;" type="text" value="${reminderThreshold}" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%;"/>
                man-days<br>
                <span style="margin-left: 20%;width: 50%" name="iaisErrorMsg" id="error_reminderThreshold" class="error-msg"></span>
            </th>

        </tr>

  <c:forEach items="${hcsaSvcRoutingStageDtos}" var="hcsaSvcRoutingStageDto">
      <tr>
        <td style="text-align:center;vertical-align:middle;">${hcsaSvcRoutingStageDto.stageName}</td>
        <td style="vertical-align:middle;">

            <input style="margin-left: 20%;width: 50%" name="${hcsaSvcRoutingStageDto.stageCode}" type="text"  placeholder="Enter required man-days"
                    <c:choose>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='PSO'}"> value="${PSO}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='ASO'}">value="${ASO}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='AO1'}">value="${AO1}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='AO2'}">value="${AO2}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='AO3'}">value="${AO3}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='PRE'}">value="${PRE}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='INP'}">value="${INP}" </c:when>
                      <c:when test="${hcsaSvcRoutingStageDto.stageCode=='POT'}">value="${POT}" </c:when>
                    </c:choose>
                   maxlength="5" style="width: 50% ;height: 10%"/>
            man-days<br>
            <span style="margin-left: 20%;width: 50%" name="iaisErrorMsg" id="error_${hcsaSvcRoutingStageDto.stageCode}" class="error-msg"></span>

        </td>
      </tr>

  </c:forEach>

    </table>
  </div>
        <div class="form-group">
          <label class="col-xs-12 col-md-4 control-label">Created Date:</label>
          <div class="col-xs-8 col-sm-6 col-md-5">
            <input name="createDate" value="${date}" style="display: none">
            <p></p>
          </div>
        </div>
        <div class="form-group">
          <label class="col-xs-12  col-sm-1  col-md-4 control-label">Created By:</label>
          <div class="col-xs-8 col-sm-6 col-md-5">
            <input style="display: none" value="${entity}" name="createBy" >
            <p>Charlie Tan</p>

          </div>
        </div>
        <br>
        <br>
        <br>

       <div class="application-tab-footer">
         <div class="row">
           <div class="col-xs-12 col-sm-10">
             <div class="text-right text-center-mobile">

                  <a class="btn btn-secondary" href="#" id="cancel">Cancel</a>
                  <a class="btn btn-primary" href="#" id="createRole">Submit</a>

             </div>
           </div>
           </div>
         </div>
       </div>
      </div>
    </div>
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  </form>

  </div>
<style>
  .table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
    padding: 8px;
    line-height: 1.42857143;
    vertical-align: top;
    border-top: 1px solid #000000;
  }
  input[type=text], input[type=email], input[type=number] {
    margin-bottom: 0px;
  }
</style>
<script type="text/javascript">

$(document).ready(function () {
    $("input[name='createDate'] +p ").html($("input[name='createDate']").val());
    $("input[name='createBy'] +p ").html($("input[name='createBy']").val());
});


  $("#module").change(function () {
      let val = $("#module").val();
      let select = $("#service").val();

      if(val!=""&&select!=""){
      $.getJSON("${pageContext.request.contextPath}/kpi-reminder-result",{"service":val,"module": select},function (data) {
          var remThreshold= data.remThreshold;
          var aso=  data.ASO;

          var pso=  data.PSO;
          var ao1= data.AO1;

          var ao2= data.AO2;

          var ao3= data.AO3;

          var pre= data.PRE;
          var inp= data.INP;
          var pot= data.POT;
          var date=data.remThr;
          var entity= data.entity;
          $("input[name='reminderThreshold']").val(remThreshold);
          $("input[name='ASO']").val(aso);
          $("input[name='PSO']").val(pso);
          $("input[name='PRE']").val(pre);
          $("input[name='INP']").val(inp);
          $("input[name='AO1']").val(ao1);
          $("input[name='AO2']").val(ao2);
          $("input[name='AO3']").val(ao3);
          $("input[name='POT']").val(pot);
          $("input[name='createDate']").val(date);
          $("input[name='createDate'] +p ").html($("input[name='createDate']").val());
          $("input[name='createBy']").val(entity);
          $("input[name='createBy'] +p ").html($("input[name='createBy']").val());
      });
      }

  });
  $("#service").change(function () {
    let val = $("#module").val();
    let select = $("#service").val();

    if(val!=""&&select!=""){
        $.getJSON("${pageContext.request.contextPath}/kpi-reminder-result",{"service":val,"module": select},function (data) {
            var remThreshold= data.remThreshold;
            var aso= data.ASO;
            var pso=  data.PSO;
            var ao1=data.AO1;
            var ao2= data.AO2;
            var ao3= data.AO3;
            var pre= data.PRE;
            var inp=data.INP;
            var pot= data.POT;
            var date=data.remThr;
            var entity= data.entity;
            $("input[name='reminderThreshold']").val(remThreshold);
            $("input[name='ASO']").val(aso);
            $("input[name='PSO']").val(pso);
            $("input[name='PRE']").val(pre);
            $("input[name='INP']").val(inp);
            $("input[name='AO1']").val(ao1);
            $("input[name='AO2']").val(ao2);
            $("input[name='AO3']").val(ao3);
            $("input[name='POT']").val(pot);
            $("input[name='createDate']").val(date);
            $("input[name='createDate'] +p ").html($("input[name='createDate']").val());
            $("input[name='createBy']").val(entity);
            $("input[name='createBy'] +p ").html($("input[name='createBy']").val());
        });
    }
});
  $('#createRole').click(function () {

    SOP.Crud.cfxSubmit("mainForm", "submit");

});

$('#cancel').click(function () {


    SOP.Crud.cfxSubmit("mainForm", "cancel","cancel","");
});



</script>

</>

