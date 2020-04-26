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

<style>
  table{text-align: center;}
  table th{text-align: center;}
</style>
<div class="main-content">
  <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="currentValidateId" value="">


<c:choose>
  <c:when test="${empty wrlGrpNameOpt}">
    Your current group cannot be found or you are not an inspection lead !
  </c:when>
  <c:otherwise>

    <div class="bg-title"><h2>Inspection Team's Weekly Non-Working Days</h2></div>
    <div class="col-md-3">
      <iais:select name="wrlGrpNameOpt" id="wrlGrpNameOpt"  onchange="doSearch()" options = "wrlGrpNameOpt" firstOption="Please Select" value="${currentGroupId}" ></iais:select>
    </div>
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
                      <iais:sortableHeader needSort="false"   field="amTime" value="AM Non-availability"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false"   field="pmTime" value="PM Non-availability"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty nonWkrinDayListAttr}">
                        <tr>
                          <td colspan="6">
                            <iais:message key="ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="nonwkrDay" items="${nonWkrinDayListAttr}" varStatus="status">
                          <tr>
                            <td>${status.index + 1}</td>
                            <%--<td>+
                               2020&lt;%&ndash;<fmt:formatDate value="${nonwkrDay.startDate}" pattern="yyyy"></fmt:formatDate>&ndash;%&gt;
                            </td>--%>
                            <td>${nonwkrDay.recursivceDate}</td>
                            <td>
                              <c:if test="${nonwkrDay.nonWkrDay == false}">
                                <input type="radio" name="nonWkrDay${status.index + 1}" id="yradio${status.index + 1}" value="Y" checked>&nbsp;Yes
                                <input type="radio" name="nonWkrDay${status.index + 1}" id="nradio${status.index + 1}" value="N" >&nbsp;No
                              </c:if>
                              <c:if test="${nonwkrDay.nonWkrDay == true}">
                                <input type="radio" name="nonWkrDay${status.index + 1}" id="yradio${status.index + 1}" value="Y" >&nbsp;Yes
                                <input type="radio" name="nonWkrDay${status.index + 1}" id="nradio${status.index + 1}" value="N" checked>&nbsp;No
                              </c:if>
                            </td>
                            <td>
                              <input class="form-check-input" type="checkbox"  aria-invalid="false"
                              <c:if test="${nonwkrDay.am == true}">
                              checked="checked"
                              </c:if>
                                     id="am${status.index + 1}"
                                      value="<iais:mask name="nonWkrDayId" value="${nonwkrDay.id}"/>">
                            </td>
                            <td>
                              <input class="form-check-input" type="checkbox"  aria-invalid="false"
                              <c:if test="${nonwkrDay.pm == true}">
                                      checked="checked"
                              </c:if>
                                      id="pm${status.index + 1}" />
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

  </c:otherwise>
</c:choose>

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

    $("input[type=radio]").click(function(){
      var action = $(this).val();
      var id = $(this).attr('id').substring(6);
      console.log(action == 'N')
      if(action == 'N'){
        $('#am'+id).prop('checked',true);
        $('#pm'+id).prop('checked',true);
      }else{
        $('#am'+id).prop("checked",false);
        $('#pm'+id).prop("checked",false);
      }
      change(id);
    });

    $("input[type=checkbox]").click(function(){
      var action = $(this).val();
      var id = $(this).attr('id').substring(2);
      if($('#am'+id).attr('checked') && $('#pm'+id).attr('checked')){
        $('#nradio'+id).prop('checked',true);
      }else{
        $('#yradio'+id).prop('checked',true);
      }
      change(id);
    });

    function change(id) {
      console.log('change')
      showWaiting();
      var dayid = $('#am' + id).val();
      console.log(dayid)
      var pm ;
      var am ;
      if($('#am'+id).attr('checked')){
        am = 'Y';
      }else{
        am = 'N';
      }
      if($('#pm'+id).attr('checked')){
        pm = 'Y';
      }else{
        pm = 'N';
      }
      $.ajax({
        data:{
          amAvailability: am,
          pmAvailability: pm,
          nonWkrDayId: dayid,
        },
        async: false,
        type:"POST",
        dataType: 'json',
        url:'/system-admin-web/nonWorkingDayAjax/change.do',
        error:function(data){

        },
        success:function(data){
          var nonWorkingDateId = data.nonWorkingDateId;
          console.log(nonWorkingDateId)
          $('#am' + id).val(nonWorkingDateId);
          dismissWaiting();
        }
      });
    }
</script>
