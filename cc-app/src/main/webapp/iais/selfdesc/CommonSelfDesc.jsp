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
  <input type="hidden" name="tabIndex" value="">


<c:choose>
  <c:when test="${empty selfDeclQueryAttr}">
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

            <c:forEach var="declItem" items="${selfDeclQueryAttr}"  varStatus="status">
                  <c:choose>
                    <c:when test="${declItem.common eq true}">
                      <div class="dashboard-tile-item">
                        <div class="dashboard-tile" id="myBody">
                          <a data-tab="#tabInbox" href="javascript:switchNextStep('');">
                            <p class="dashboard-txt">General Regulation</p>
                          </a>
                        </div>
                      </div>
                    </c:when>
                    <c:otherwise>
                      <div class="dashboard-tile-item">
                        <div class="dashboard-tile">
                          <a data-tab="#tabInbox" href="javascript:switchNextStep('${declItem.svcId}');">
                            <p class="dashboard-txt">${declItem.svcName}</p>
                          </a>
                        </div>
                      </div>
                    </c:otherwise>
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
              <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: doSubmit();">Submit</a>
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
          $("[name='tabIndex']").val(index);
          $("[name='crud_action_type']").val("switchNextStep");
          var mainForm = document.getElementById('mainForm');
          mainForm.submit();;
      }

      function doSubmit(){
          SOP.Crud.cfxSubmit("mainForm", "submitSelfDesc");
      }

  </script>

</>

