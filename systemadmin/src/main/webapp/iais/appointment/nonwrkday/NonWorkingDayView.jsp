<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 1/6/2020
  Time: 5:30 PM
  To change this template use File | Settings | File Templates.
--%>
--%><%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>


<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">
    <div class="bg-title"><h2>Define Inspection Team's Weekly Non-Working Days</h2></div>

    <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  onchange="doSearch()" options = "wrlGrpNameOpt" firstOption="Please Select" value="${currentGroupId}" ></iais:select>

    <br><br><br>
    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"   field="index" value="No."></iais:sortableHeader>
                      <%--<iais:sortableHeader needSort="false"   field="year" value="Year"></iais:sortableHeader>--%>
                      <iais:sortableHeader needSort="false"   field="day" value="Day"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="wrkingDay" value="Working Day"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="amTime" value="Time From"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="pmTime" value="Time to"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="action" value="Action"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty nonWkrinDayListAttr}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="nonwkrDay" items="${nonWkrinDayListAttr}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                            <%--<td>
                               2020&lt;%&ndash;<fmt:formatDate value="${nonwkrDay.startDate}" pattern="yyyy"></fmt:formatDate>&ndash;%&gt;
                            </td>--%>
                            <td>${nonwkrDay.recursivceDate}</td>
                            <c:choose>
                                <c:when test="${nonwkrDay.nonWkrDay == true}">
                                  <td>No</td>
                              </c:when>
                              <c:otherwise>
                                <td>Yes</td>
                              </c:otherwise>
                              </c:choose>
                            <td>${nonwkrDay.startAt}</td>
                            <td>${nonwkrDay.endAt}</td>
                            <td>
                              <input type="hidden" id="nonWkrDayId" name="nonWkrDayId" value="">
                              <button type="button" name="nonWkrDayId"  onclick="doUpdate('<iais:mask name="nonWkrDayId" value="${nonwkrDay.id}"/>')" class="btn btn-default btn-sm" >Update</button>
                            </td>
                          </tr>
                        </c:forEach>


                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <div class="table-footnote">
                    <div class="row">
                      <div class="col-xs-6 col-md-8 text-right">
                        <br><br>


                      </div>
                    </div>
                  </div>


                </div>
              </div>
            </div>
          </div>
        </div>
      </div>


    </div>

  </form>
</div>

<script>
    function doSearch() {
        SOP.Crud.cfxSubmit("mainForm", "doSearch");
    }

    function doUpdate(val){
        $('#nonWkrDayId').val(val);
        SOP.Crud.cfxSubmit("mainForm", "preUpdate");
    }

</script>
