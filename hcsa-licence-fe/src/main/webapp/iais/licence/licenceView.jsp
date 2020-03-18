<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@ include file="../common/dashboard.jsp" %>
<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>


  <div class="container">
    <div class="row">
      <div class="col-xs-12">
        <div class="instruction-content center-content">
          <h3>Licence detail</h3>
          <!--startpoint-->
          <div id = "printDev">
            <p>This is a dummy licence with Licence Number ${licenceViewDto.licenceDto.licenceNo}</p>
            <ul class="info-content">
              <li>
                <p>Name of Licensee:  ${licenceViewDto.licenseeDto.name}</p>
              </li>
              <li>
                <p>Service Licence: ${licenceViewDto.licenceDto.svcName}</p>
              </li>
              <li>
              <p>Licence Start and End Date: ${licenceViewDto.startDate} to ${licenceViewDto.endDate} </p>
              </li>
              <li>
                <p>Licensed Premises: ${licenceViewDto.hciName} </p>
              </li>
            </ul>
            <p>Premises Address: ${licenceViewDto.address}</p>
          </div>
          <!--endpoint-->
          <div class="application-tab-footer">
            <div class="row">
              <div class="col-xs-12 col-sm-6">
                <p></p>
              </div>
              <div class="col-xs-12 col-sm-6">
                <div class="text-right text-center-mobile"><a class="btn btn-primary next" onclick="preview();" href="">Print</a></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
  <%@ include file="/include/validation.jsp" %>
  <input type="hidden" name="pageCon" value="valPremiseList" >
</form>
<script>
    function preview() {
        body = window.document.body.innerHTML;
        startpoint= "<!--startpoint-->";
        endpoint= "<!--endpoint-->";
        printdb= body.substring(body.indexOf(startpoint) + 17);
        printdb = printdb.substring(0, printdb.indexOf(endpoint));
        window.document.body.innerHTML = printdb ;
        window.print();
        window.document.body.innerHTML = body ;
    }

</script>

