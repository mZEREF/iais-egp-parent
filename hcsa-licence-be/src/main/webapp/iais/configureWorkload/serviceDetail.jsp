<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-intranet"/>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm1" method = "post" action=<%=process.runtime.continueURL()%>>
  <div class="main-content">
    <div class="container">
      <div class="form-horizontal">
        <div class="form-group">
          <h1>${stageValue}</h1>

          <div class="row">
            <div class="col-xs-12">
              <div class="table-gp">
                <table class="table">
                  <thead>
                  <tr>
                    <th class="col-xs-1"></th>
                    <th class="col-xs-2">S/N</th>
                    <th class="col-xs-3">Service</th>
                    <th class="col-xs-5">Workload Manhours<br>(For illustration only)</th>
                    <th class="col-xs-1"></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var = "item" items = "${stageList}" varStatus="status" >
                    <tr>
                      <td></td>
                      <td>
                        ${status.index + 1}
                      </td>
                      <td>
                        ${item.serviceName}
                      </td>
                      <td>
                        <input name="${item.serviceName}" value="${item.manhourCount}">
                      </td>
                      <td></td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="col-xs-12 col-sm-6">
            <div class="text-right text-center-mobile"><a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doSubmit();">Submit</a></div>
          </div>

        </div>
      </div>
    </div>
  </div>

</form>
<script>
  function doSubmit() {
    SOP.Crud.cfxSubmit("mainForm1","submit");
  }
</script>