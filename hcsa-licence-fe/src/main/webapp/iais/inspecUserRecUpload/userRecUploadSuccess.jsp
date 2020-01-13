<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/23
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>User Rectification Upload</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
                      <iais:section title="" id = "upload_Rectification">
                        <iais:row>
                          <iais:field value="Checklist Question"/>
                          <iais:value width="7">
                            <label><c:out value = "${checklistItemDto.checklistItem}"/></label>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="NC Clause"/>
                          <iais:value width="300">
                            <label><c:out value = "${checklistItemDto.regulationClause}"></c:out></label>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Attachment"/>
                          <iais:value width="7">
                            <label><c:out value = "${inspectionPreTaskDto.reMarks}"></c:out></label>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Remarks"/>
                          <iais:value width="300">
                            <label><c:out value="${uploadRemarks}"></c:out></label>
                          </iais:value>
                        </iais:row>
                      </iais:section>
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
</div>