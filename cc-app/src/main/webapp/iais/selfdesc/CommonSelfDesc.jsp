<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/20/2019
  Time: 6:06 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
  .form-check-gp{
    width: 50%;
    float:left;
  }

  .form-inline .form-group {
    width: 30%;
    margin-bottom: 25px;
    display: inline-block;
    vertical-align: middle;
  }



</style>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <input type="hidden" name="pageIndex" value="">

<c:choose>
  <c:when test="${empty tabResultAttr.rows}">
    <tr>
      <td colspan="6">
        No Record!!
      </td>
    </tr>
  </c:when>
  <c:otherwise>
    <div class = "container">
      <div class="row">
        <div class="col-xs-12">
          <div class="dashboard-gp">

            <c:forEach var="item" items="${tabResultAttr.rows}"  varStatus="status">
              <c:choose>
                <c:when test="${item.common == true && status.index == 0}">
                  <div class="dashboard-tile-item">
                    <div class="dashboard-tile" id="myBody">
                      <a data-tab="#tabInbox" href="javascript:switchNextStep('${item.configId}');">
                        <p class="dashboard-txt">General Regulation</p>
                      </a>
                    </div>
                  </div>
                </c:when>
                <c:when test="${item.common == false}">
                  <div class="dashboard-tile-item">
                    <div class="dashboard-tile">
                      <a data-tab="#tabInbox" href="javascript:switchNextStep('${item.configId}');">
                        <p class="dashboard-txt">${item.svcName}</p>
                      </a>
                    </div>
                  </div>
                </c:when>
              </c:choose>


            </c:forEach>


          </div>
        </div>
      </div>

      <div class="tab-content">
        <div class="tab-pane active" id="tabInbox" role="tabpanel">

          <div class="row">
            <div class="col-xs-12">
              <div class="table-gp">
                <table class="table">
                  <thead>
                  <tr>
                    <th>No.</th>
                    <th>Regulation Clause</th>
                    <th>Item</th>
                    <th>Yes</th>
                    <th>No</th>
                    <th>Na</th>
                  </tr>
                  </thead>

                  <tbody id="general">
                    <%@include file="/iais/selfdesc/Answer.jsp"%>
                  </tbody>

                </table>



              </div>
            </div>
          </div>
        </div>

        <div class="application-tab-footer">
          <td>
            <div class="text-right text-center-mobile">
              <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doNext();">Next</a>
              <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doCancel();">Cancel</a>
            </div>

          </td>
        </div>


      </div>


    </div>

  </c:otherwise>
</c:choose>


  <script type="text/javascript">
      function switchNextStep(index){
          $("[name='pageIndex']").val(index);
          $("[name='crud_action_type']").val("switchNextStep");
          var mainForm = document.getElementById('mainForm');
          mainForm.submit();;
      }

      function doNext(){
          SOP.Crud.cfxSubmit("mainForm", "doSave");
      }

  </script>

</>

