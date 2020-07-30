<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/3/27
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="wrms" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainCheckListViewForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">

              <div class="tab-gp dashboard-tab <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                  <li class="active" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
                  <li class="complete" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
                                                              data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
                </ul>

                <div class="tab-nav-mobile visible-xs visible-sm">
                  <div class="swiper-wrapper" role="tablist">
                    <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
                    <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab">ServiceInfo</a></div>
                    <div class="swiper-slide"><a href="#chkInfo" aria-controls="chkInfo" role="tab" data-toggle="tab">chkInfo</a></div>
                  </div>
                  <div class="swiper-button-prev"></div>
                  <div class="swiper-button-next"></div>
                </div>

                <div class="tab-content">
                  <div class="tab-pane active" id="General" role="tabpanel">
                    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.generalDo}"/>/<c:out value="${serListDto.generalTotal}"/><br>
                    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.generalNc}"/>
                    <h3>General</h3>
                    <div class="table-gp">
                      <c:forEach var ="section" items ="${commonDto.sectionDtoList}" varStatus="one">
                        <br/>
                        <h4><c:out value="${section.sectionName}"></c:out></h4>
                        <table class="table">
                          <thead>
                          <tr>
                            <th width="5%">No.</th>
                            <th width="25%">Regulation Clause Number</th>
                            <th width="30%">Item</th>
                            <th width="5%">Yes</th>
                            <th width="5%">No</th>
                            <th width="5%">N/A</th>
                            <th width="15%">Remarks</th>
                            <th width="10%">Rectified</th>
                          </tr>
                          </thead>
                          <tbody>
                          <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                            <tr>
                              <td class="row_no">${(status.index + 1) }</td>
                              <td>${item.incqDto.regClauseNo}</td>
                              <td>${item.incqDto.checklistItem}</td>
                              <c:set value = "${item.incqDto.sectionNameSub}${item.incqDto.itemId}" var = "ckkId"/>
                              <td><input disabled name="<c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                              <td>
                                <input disabled name="<c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No"/>
                              </td>
                              <td><input disabled name="<c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                              <td><input disabled name="<c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>comitemCheckboxRemark" type="text" value="<c:out value="${item.incqDto.remark}"/>"/></td>
                              <td>
                                <div id="<c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                  <input disabled name="<c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                </div>
                              </td>
                            </tr>
                          </c:forEach>
                          </tbody>
                        </table>
                      </c:forEach>
                    </div>
                  </div>
                  <div class="tab-pane" id="ServiceInfo" role="tabpanel">
                    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.serviceDo}"/>/<c:out value="${serListDto.serviceTotal}"/><br>
                    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.serviceNc}"/>
                    <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
                      <h3>${cdto.subType}</h3>
                      <div class="table-gp">
                        <c:forEach var ="section" items ="${cdto.sectionDtoList}" varStatus="two">
                          <br/>
                          <h4><c:out value="${section.sectionName}"></c:out></h4>
                          <table class="table">
                            <thead>
                            <tr>
                              <th width="5%">No.</th>
                              <th width="25%">Regulation Clause Number</th>
                              <th width="30%">Item</th>
                              <th width="5%">Yes</th>
                              <th width="5%">No</th>
                              <th width="5%">N/A</th>
                              <th width="15%">Remarks</th>
                              <th width="10%">Rectified</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                              <tr>
                                <td class="row_no">${(status.index + 1) }</td>
                                <td>${item.incqDto.regClauseNo}</td>
                                <td>${item.incqDto.checklistItem}</td>
                                <c:set value = "${cdto.subName}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                <td><input disabled name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                                <td>
                                  <input disabled name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No"/>
                                </td>
                                <td><input disabled name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                                <td><input disabled name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>itemCheckboxRemark" type="text" value="<c:out value="${item.incqDto.remark}"/>"/></td>
                                <td>
                                  <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                    <input disabled name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                  </div>
                                  <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameSub}${item.incqDto.itemId}" var = "err"/>
                                </td>
                              </tr>
                            </c:forEach>
                            </tbody>
                          </table>
                        </c:forEach>
                      </div>
                    </c:forEach>
                    <c:if test="${adchklDto.adItemList != null}">
                      <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.adhocDo}"/>/<c:out value="${serListDto.adhocTotal}"/><br>
                      <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.adhocNc}"/>
                      <div class="table-gp">
                        <h3>Adhoc</h3>
                        <br/>
                        <h4></h4>
                        <table class="table">
                          <thead>
                          <tr>
                            <th width="5%">No.</th>
                            <th width="55%">Item</th>
                            <th width="5%">Yes</th>
                            <th width="5%">No</th>
                            <th width="5%">N/A</th>
                            <th width="15%">Remarks</th>
                            <th width="10%">Rectified</th>
                          </tr>
                          </thead>
                          <tbody>

                          <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                            <tr>
                              <td class="row_no">${(status.index + 1) }</td>
                              <td><c:out value="${item.question}"/></td>
                              <c:set value = "${item.id}" var = "ckkId"/>
                              <td><input disabled name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                              <td>
                                <input disabled name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No"/>
                              </td>
                              <td><input disabled name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                              <td><input disabled name="<c:out value="${item.id}"/>adhocremark" id="<c:out value="${item.id}"/>adhocitemCheckboxRemark" type="text" value="<c:out value="${item.remark}"/>"/></td>
                              <td>
                                <div id="<c:out value="${item.id}"/>ck"<c:if test="${item.adAnswer != 'No'}">hidden</c:if>>
                                  <input disabled name="<c:out value="${item.id}"/>adhocrec" id="<c:out value="${item.id}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec"/>
                                </div>
                                <c:set value = "error_${item.id}adhoc" var = "err"/>
                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                              </td>
                            </tr>
                          </c:forEach>

                          </tbody>
                        </table>
                      </div>
                    </c:if>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-12">
                    <div align="left">
                      <a class="back" id="Back" onclick="javascript:doInspRecCheckListViewBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
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
    function doInspRecCheckListViewBack() {
        showWaiting();
        $("#actionValue").val('view');
        inspRecCheckListViewSubmit('view');
    }

    function inspRecCheckListViewSubmit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainCheckListViewForm');
        mainPoolForm.submit();
    }
</script>
