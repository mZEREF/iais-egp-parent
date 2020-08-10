<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
          String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <div class="tab-content">
                        </div>
                        <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li class="complete ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
                                <li class="complete ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
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
                            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                            <div class="tab-content">
                                <div class="tab-pane ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" id="General" role="tabpanel">
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
                                                    <th>No.</th>
                                                    <th>Regulation Clause Number</th>
                                                    <th>Item</th>
                                                    <th>Yes</th>
                                                    <th>No</th>
                                                    <th>N/A</th>
                                                    <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                                                    <th>Self-assessment Answer</th>
                                                    </c:if>
                                                    <th>Remarks</th>
                                                    <th>Rectified</th>
                                                    <th></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                                                    <tr>
                                                        <td class="row_no"><span>${(two.index + 1) }</span></td>
                                                        <td> <a data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}">${item.incqDto.regClauseNo}</a></td>
                                                        <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                                            <div class="modal-dialog" role="document">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                        <h5 class="modal-title"></h5>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <div class="row">
                                                                            <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span  style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <td><span >${item.incqDto.checklistItem}</span></td>
                                                        <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                        <td class="text-center"><input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                        <td class="text-center">
                                                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No"  />
                                                        </td>
                                                        <td class="text-center"><input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" /></td>
                                                        <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                                                       <td>${item.incqDto.selfAnswer}</td>
                                                        </c:if>
                                                        <td>
                                                            <textarea cols="35" rows="4" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                                        </td>
                                                        <td class="text-center">
                                                            <div id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}com" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                          </td>
                                                      </tr>
                                                  </c:forEach>
                                                  </tbody>
                                              </table>
                                          </c:forEach>
                                      </div>
                                  </div>
                                  <div class="tab-pane ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" id="ServiceInfo" role="tabpanel">
                                      <c:if test="${not empty serListDto.fdtoList}">
                                      <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.serviceDo}"/>/<c:out value="${serListDto.serviceTotal}"/><br>
                                      <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.serviceNc}"/>
                                      </c:if>
                                      <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
                                          <h3>${cdto.subType}</h3>
                                          <div class="table-gp">
                                              <c:forEach var ="section" items ="${cdto.sectionDtoList}" varStatus="two">
                                                  <br/>
                                                  <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                  <table class="table">
                                                      <thead>
                                                      <tr>
                                                          <th>No.</th>
                                                          <th>Regulation Clause Number</th>
                                                          <th>Item</th>
                                                          <th>Yes</th>
                                                          <th>No</th>
                                                          <th>N/A</th>
                                                          <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                                                              <th >Self-assessment Answer</th>
                                                          </c:if>
                                                          <th>Remarks</th>
                                                          <th>Rectified</th>
                                                          <th></th>
                                                      </tr>
                                                      </thead>
                                                      <tbody>
                                                      <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                          <tr>
                                                              <td class="row_no"><span>${(status.index + 1) }</span></td>
                                                              <td><a data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}">${item.incqDto.regClauseNo}</a> </td>
                                                              <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                                                  <div class="modal-dialog" role="document">
                                                                      <div class="modal-content">
                                                                          <div class="modal-header">
                                                                              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                              <h5 class="modal-title"></h5>
                                                                          </div>
                                                                          <div class="modal-body">
                                                                              <div class="row">
                                                                                  <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                                                              </div>
                                                                          </div>
                                                                          <div class="modal-footer">
                                                                              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                          </div>
                                                                      </div>
                                                                  </div>
                                                              </div>
                                                              <td><span>${item.incqDto.checklistItem}</span></td>
                                                              <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                              <td class="text-center"><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                              <td class="text-center">
                                                                  <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No"  />
                                                              </td>
                                                              <td class="text-center"><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A"  /></td>
                                                              <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                                                              <td>${item.incqDto.selfAnswer}</td>
                                                              </c:if>
                                                              <td>
                                                                  <textarea cols="35" rows="4" name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxRemark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                                              </td>
                                                              <td class="text-center">
                                                                  <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                      <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                                  </div>
                                                              </td>
                                                              <td>
                                                                  <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
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
                                                          <th>No.</th>
                                                          <th>Item</th>
                                                          <th>Yes</th>
                                                          <th>No</th>
                                                          <th>N/A</th>
                                                          <th>Remarks</th>
                                                          <th>Rectified</th>
                                                          <th></th>
                                                      </tr>
                                                      </thead>
                                                      <tbody>

                                                      <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                                                          <tr>
                                                              <td class="row_no">${(status.index + 1) }</td>
                                                              <td><c:out value="${item.question}"/></td>
                                                              <c:set value = "${item.id}" var = "ckkId"/>
                                                               <td class="text-center"><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                                                              <td class="text-center">
                                                                  <input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No"/>
                                                              </td>
                                                              <td class="text-center"><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                                                              <td>
                                                                  <textarea cols="35" rows="4" name="<c:out value="${item.id}"/>adhocremark" id="<c:out value="${item.id}"/>adhocitemCheckboxRemark" id="" maxlength="500"><c:out value="${item.remark}"/></textarea>
                                                              </td>
                                                              <td class="text-center">
                                                                  <div id="<c:out value="${item.id}"/>ck"<c:if test="${item.adAnswer != 'No'}">hidden</c:if>>
                                                                      <input name="<c:out value="${item.id}"/>adhocrec" id="<c:out value="${item.id}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec"/>
                                                                  </div>
                                                              </td>
                                                              <td>
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
                                        <a class="back" href="#" onclick="doBack()"><em class="fa fa-angle-left"></em> Back</a>
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
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function doBack(){
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();
    }
</script>
