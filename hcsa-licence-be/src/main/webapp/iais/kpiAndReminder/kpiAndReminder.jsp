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
  <input type="hidden" name="crud_action_type" value="">
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
            <select  name="module">
               <option  value="new">New</option>
               <option  value="renewal">Renewal</option>
               <option  value="reqForChange">Request for Change</option>
               <option  value="appeal">Appeal</option>
              <option >Withdrawal</option>
              <option>Revocation</option>
            </select>
         </div>
      </div>

           <div class="form-group">
               <label class="col-xs-12 col-md-4 control-label">Service</label>
                <div class="col-xs-8 col-sm-6 col-md-5">
                  <select name="service" >
                    <option >Select one</option>
                    <c:forEach items="${hcsaServiceDtos}" var="hcsaServiceDto">
                      <option >${hcsaServiceDto.svcDesc}</option>
                    </c:forEach>
                  </select>
                </div>
           </div>

  <div class="table-gp">
    <table class="table" border="1px">
      <tr>
        <td rowspan="9" width="20%" style="text-align: center;vert-align: middle" >Processing Time for different stages</td>
        <td style="text-align: center;vert-align: middle">Reminder Threshold:</td>
        <td style="text-align: center;vert-align: middle">
          <input name="reminderThreshold" type="text" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Admin Screening :</td>
        <td style="text-align: center;vert-align: middle">
          <input name="adminScreening" type="text" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Professional Screening:</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" name="professionalScreening" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Pre-Inspection :</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days" name="preInspection" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Inspection</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days" name="inspection" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Post-Inspection </td>
        <td style="text-align: center;vert-align: middle">
          <input type="text"  placeholder="Enter required man-days" name="postInspection" maxlength="5" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Approval Officer Level 1:</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days" maxlength="5" name="levelOne" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>
        <td style="text-align: center;vert-align: middle">Approval Officer Level 2:</td>
        <td style="text-align: center;vert-align: middle">
          <input type="text" placeholder="Enter required man-days" maxlength="5" name="levelTwo" style="width: 50% ;height: 10%"/>
          man-days
        </td>
      </tr>
      <tr>

        <td style="text-align: center;vert-align: middle">Approval Officer Level 3:</td>
        <td style="text-align: center;vert-align: middle"><input type="text" name="levelThree" placeholder="Enter required man-days" maxlength="5" style="width: 50% ;height: 10%"/>  man-days</td>
      </tr>

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
            <input style="display: none" value="Charlie Tan" name="createBy">
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

  </form>

  </div>
<script type="text/javascript">

$(document).ready(function () {
    $("input[name='createDate'] +p ").html($("input[name='createDate']").val());


});


$('#createRole').click(function () {
    SOP.Crud.cfxSubmit("mainForm", "submit");
});

$('#cancel').click(function () {
    SOP.Crud.cfxSubmit("mainForm", "cancle");
});



</script>

</>

