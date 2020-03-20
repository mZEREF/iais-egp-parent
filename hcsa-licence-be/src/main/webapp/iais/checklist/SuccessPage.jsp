<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/13/2019
  Time: 12:52 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <input type="hidden" name="currentValidateId" value="">

  <br><br><br>

  <div class="main-content">
    <div class="container">

      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">
          <div class="tab-content">
            <h2 class="component-title">Upload Result</h2>
            <table class="table">
              <thead>
              <tr>
                <th>No.</th>
                <th>Upload Item</th>
                <th>Upload Result</th>
              </tr>
              </thead>

              <tbody>
              <c:forEach var="result" items="${messageContent}" varStatus="status">
                <tr>
                  <td>
                    <p>${status.index + 1}</p>
                  </td>
                  <td>
                    <p style="width: 300px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">${result.subject}</p>
                  </td>
                  <td>
                    <p>${result.result}</p>
                  </td>
                </tr>
              </c:forEach>
              </tbody>

            </table>
          </div>

        </div>


      </div>

    </div>

    <div class="application-tab-footer">
      <div class="row">
        <div class="col-xs-12 col-sm-6">
          <p></p>
        </div>
        <div class="col-xs-12 col-sm-6">
          <div class="button-group"><a id="docBack" class="btn btn-primary next" >Done</a></div>
        </div>
      </div>
    </div>



  </div>

</form>

<script>
    $('#docBack').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    });

</script>