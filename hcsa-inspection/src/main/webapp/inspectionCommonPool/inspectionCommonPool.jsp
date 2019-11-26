<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  String webroot=IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">


<form method="post" id="mainPoolForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="inspectionPoolType" value="">
  <input type="hidden" id="applicationNo" name="applicationNo" value="">

  <iais:body >
    <div class="container">
      <div class="col-xs-12">
        <div class="components">
          <div class="table-gp">
            <table class="table">
              <thead>
              <tr align="center">
                <th>Application No.</th>
                <th>Service Name</th>
                <th>Action</th>
              </tr>
              </thead>
              <tbody>
                <c:forEach items="${inspectionTaskPoolListDtoList}" var="pool">
                  <tr>
                    <td><c:out value="${pool.applicationNo}"/></td>
                    <td><c:out value="${pool.serviceName}"/></td>
                    <td><button type="button"  class="btn btn-default" onclick="javascript:doAssign('<iais:mask name="applicationNo" value="${pool.applicationNo}"/>');">Assign</button></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
            <div class="table-footnote">
              <div class="row">
                <div class="col-xs-6 col-md-4">
                  <p class="count">5 out of 25</p>
                </div>
                <div class="col-xs-6 col-md-8 text-right">
                  <div class="nav">
                    <ul class="pagination">
                      <li class="hidden"><a href="#" aria-label="Previous"><span aria-hidden="true"><i class="fa fa-chevron-left"></i></span></a></li>
                      <li class="active"><a href="#">1</a></li>
                      <li><a href="#">2</a></li>
                      <li><a href="#">3</a></li>
                      <li><a href="#" aria-label="Next"><span aria-hidden="true"><i class="fa fa-chevron-right"></i></span></a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </iais:body>
</form>
</div>
<script type="text/javascript">

    /*function doAssign(applicationNo){
        SOP.Crud.cfxSubmit("mainPoolForm","inspectionPoolType","assign",applicationNo);
    }*/
    function doAssign(applicationNo) {
        submit('assign');
        $("#applicationNo").val(applicationNo);
        console.log("applicationNo");
    }

    function submit(action){
        $("[name='inspectionPoolType']").val(action);
        var mainPoolForm = document.getElementById('mainPoolForm');
        mainPoolForm.submit();
    }
</script>