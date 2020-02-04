<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/4/2020
  Time: 11:00 AM
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
    <div class="bg-title"><h2>Inspector Calendar Function</h2></div>



          <iais:section title="" id = "demoList">
            <iais:row>
              <iais:field value="Name."/>
              <iais:value width="18">
                <input type="text" name="userName" value=""${userName}" />
              </iais:value>
            </iais:row>


            <iais:row>
              <iais:field value="Year."/>
              <iais:value width="18">
                <iais:select name="dropYearOpt" id="dropYearOpt"
                             onchange="doSearch()" options = "dropYearOpt" firstOption="Please Select" value="${dropYear}" ></iais:select>
              </iais:value>
            </iais:row>

            <iais:action style="text-align:center;">
              <button class="btn btn-lg btn-login-submit" type="button" style="background:#2199E8; color: white" onclick="javascript:doSearch()">Search</button>
              <button class="btn btn-lg btn-login-clear" type="button" style="background:#2199E8; color: white" onclick="javascript:doClear()">Clear</button>
            </iais:action>
          </iais:section>



    <br><br><br>
    <div>
      <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="tab-content">
          <div class="row">
            <br><br>
            <div class="col-xs-12">
              <div class="components">
                <iais:pagination  param="inspectorCalenDarQueryAttr" result="inspectorCalenDarResultAttr"/>
                <div class="table-gp">
                  <table class="table">
                    <thead>
                    <tr>
                      <iais:sortableHeader needSort="false"   field="index" value="No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="YEAR" value="Year"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_START" value="User Block Date Start"></iais:sortableHeader>
                      <iais:sortableHeader needSort="true"   field="BLOCK_OUT_END" value="User Block Date End"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="REMARKS" value="User Block Date Description"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty inspectorCalenDarResultAttr}">
                        <tr>
                          <td colspan="6">
                            No Record!!
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="calendar" items="${inspectorCalenDarResultAttr.rows}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                              <%--<td>
                                 2020&lt;%&ndash;<fmt:formatDate value="${nonwkrDay.startDate}" pattern="yyyy"></fmt:formatDate>&ndash;%&gt;
                              </td>--%>
                            <td>${calendar.userName}</td>
                            <td>${calendar.year}</td>

                            <td><fmt:formatDate value="${calendar.userBlockDateStart}" pattern="MM/dd/yyyy"/></td>
                            <td><fmt:formatDate value="${calendar.userBlockDateEnd}" pattern="MM/dd/yyyy"/></td>

                            <td>${calendar.description}</td>
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
        SOP.Crud.cfxSubmit("mainForm", "doQuery");
    }
    
    function doClear() {
        $('input[name="userName"]').val("");


        $("#dropYearOpt option[text = 'Please select']").val("selected", "selected");
        $(".current").text("Please select");
        $("#dropYearOpt").val("");
    }

    function jumpToPagechangePage(){
        SOP.Crud.cfxSubmit("mainForm", "doPaging");
    }

    function sortRecords(sortFieldName,sortType){
        SOP.Crud.cfxSubmit("mainForm","doSorting",sortFieldName,sortType);
    }


</script>
