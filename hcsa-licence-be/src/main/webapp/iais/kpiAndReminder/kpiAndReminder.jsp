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
<%--  <input type="hidden" name="crud_action_type" value="">--%>
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <div class="col-lg-12 col-xs-12">
    <div class="center-content">
      <div class="intranet-content">
        <div class="bg-title">
                <h2>Add KPI and Reminder Threshold</h2>
        </div>

      <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label">Module:</label>
         <div class="col-xs-8 col-sm-6 col-md-5">
            <select  name="module"  id="module" >
               <option  value="">Select one</option>

               <option   value="APTY002" >New</option>
               <option   value="APTY004"  >Renewal</option>
               <option   value="APTY005">Request for Change</option>
               <option   value="APTY001">Appeal</option>
               <option   value="APTY006" >Withdrawal</option>
               <option   value="APTY003">Revocation</option>
            </select>

           <span name="iaisErrorMsg" id="error_module" class="error-msg"></span>
         </div>
      </div>

           <div class="form-group">
               <label class="col-xs-12 col-md-4 control-label">Service</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                  <select name="service"  id="service">
                    <option value="" >Select one</option>
                    <c:forEach items="${hcsaServiceDtos}" var="hcsaServiceDto">

                      <option value="${hcsaServiceDto.svcCode}"   <c:if test="${service==hcsaServiceDto.svcCode}">selected="selected" </c:if>>${hcsaServiceDto.svcDesc}</option>
                    </c:forEach>
                  </select>
                  <span name="iaisErrorMsg" id="error_service" class="error-msg"></span>
                </div>
           </div>

  <div class="table-gp">
    <table class="table" border="1px">

      <tr>
        <td rowspan="9" width="20%" style="text-align: center;vert-align: middle" >Processing Time for different stages</td>
        <td style="text-align: center;vert-align: middle">Reminder Threshold:</td>
        <td style="text-align: center;vert-align: middle">
          <input name="reminderThreshold" type="text" value="${reminderThreshold}" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_reminderThreshold" class="error-msg"></span>
         </td>

      </tr>

  <c:forEach items="${hcsaSvcRoutingStageDtos}" var="hcsaSvcRoutingStageDto">

      <tr>

        <td style="text-align: center;vert-align: middle">${hcsaSvcRoutingStageDto.stageName}</td>
        <td style="text-align: center;vert-align: middle">
          <input name="${hcsaSvcRoutingStageDto.stageCode}" type="text"  placeholder="Enter required man-days" value="" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_${hcsaSvcRoutingStageDto.stageCode}" class="error-msg"></span>
        </td>

      </tr>
  </c:forEach>
    <%--  <tr>

        <td style="text-align: center;vert-align: middle">Professional Screening:</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" name="professionalScreening" value="${professionalScreening}" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_professionalScreening" class="error-msg"></span>
        </td>

      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Pre-Inspection :</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days" value="${preInspection}"  name="preInspection" value="" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_preInspection" class="error-msg"></span>
        </td>

      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Inspection</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days"   value="${inspection}"  name="inspection" value="" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_inspection" class="error-msg"></span>
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Post-Inspection </td>
        <td style="text-align: center;vert-align: middle">
          <input type="text"  placeholder="Enter required man-days"  value="${postInspection}" name="postInspection" value="" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_postInspection" class="error-msg"></span>
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Approval Officer Level 1:</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days"  value="${levelOne}" maxlength="5" value="" name="levelOne" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_levelOne" class="error-msg"></span>
        </td>
      </tr>
      <tr>
        <td style="text-align: center;vert-align: middle">Approval Officer Level 2:</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days" value="${levelTwo}" maxlength="5" name="levelTwo" value="" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_levelTwo" class="error-msg"></span>
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Approval Officer Level 3:</td>
        <td style="text-align: center;vert-align: middle"><input type="text" name="levelThree"  value="${levelThree}" placeholder="Enter required man-days" value="" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
          <span name="iaisErrorMsg" id="error_levelThree" class="error-msg"></span>
        </td>

      </tr>--%>

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
           <div class="col-xs-12 col-sm-12">
             <div class="text-right text-center-mobile">
                  <a class="btn btn-primary" href="#" id="createRole">Submit</a>
                  <a class="btn btn-primary" href="#" id="cancel">Cancel</a>
             </div>
           </div>
           </div>
         </div>
       </div>
      </div>
    </div>
  <%@ include file="/include/validation.jsp" %>
  </form>

  </div>
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
    $("#service").val("");
    $("#module").val("");

    $("input[name='reminderThreshold']").val("");
    $("input[name='adminScreening']").val("");
    $("input[name='professionalScreening']").val("");
    $("input[name='preInspection']").val("");
    $("input[name='inspection']").val("");
    $("input[name='levelOne']").val("");
    $("input[name='levelTwo']").val("");
    $("input[name='levelThree']").val("");
    $("input[name='postInspection']").val("");
    location.href="https://egp.sit.intra.iais.com/egov/process/EGPCLOUD/Home";
   /* SOP.Crud.cfxSubmit("mainForm", "cancel");*/
});



</script>

</>

