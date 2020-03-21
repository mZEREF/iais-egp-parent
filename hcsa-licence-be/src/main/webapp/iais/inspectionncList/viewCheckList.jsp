<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
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
                                                    <th>No.</th>
                                                    <th>Regulation Clause Number</th>
                                                    <th>Item</th>
                                                    <th>Yes</th>
                                                    <th>No</th>
                                                    <th>N/A</th>
                                                    <th>Remark</th>
                                                    <th>Rectified</th>
                                                    <th></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                                                    <tr>
                                                        <td class="row_no">${(two.index + 1) }</td>
                                                        <td>${item.incqDto.regClauseNo}</td>
                                                        <td>${item.incqDto.checklistItem}</td>
                                                        <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                        <td><input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                                                        <td>
                                                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No"/>
                                                        </td>
                                                        <td><input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                                                        <td>
                                                            <textarea cols="70" rows="7" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                                        </td>
                                                        <td>
                                                            <div id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                            </div>
                                                            <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}com" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                        </td>
                                                        <td>
                                                            <c:set var="fir" value="${one.index}"></c:set>
                                                            <c:set var="sec" value="${two.index}"></c:set>
                                                            <c:forEach var="oldcom" items="${allComChkDtoList}">
                                                                <wrms:value width="7">
                                                                    <span class="oldVal compareTdStyle" attr="${oldcom.sectionDtoList[fir].itemDtoList[sec].incqDto.chkanswer}"><label><c:out value="${oldcom.sectionDtoList[fir].itemDtoList[sec].incqDto.chkanswer}"/></label></span>
                                                                </wrms:value>
                                                            </c:forEach>
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
                                                        <th>No.</th>
                                                        <th>Regulation Clause Number</th>
                                                        <th>Item</th>
                                                        <th>Yes</th>
                                                        <th>No</th>
                                                        <th>N/A</th>
                                                        <th>Remark</th>
                                                        <th>Rectified</th>
                                                        <th></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no">${(status.index + 1) }</td>
                                                            <td>${item.incqDto.regClauseNo}</td>
                                                            <td>${item.incqDto.checklistItem}</td>
                                                            <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                            <td><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                                                            <td>
                                                                <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No"/>
                                                            </td>
                                                            <td><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                                                            <td>
                                                                <textarea cols="70" rows="7" name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxRemark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                                            </td>
                                                            <td>
                                                                <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                    <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                                </div>
                                                                <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "err"/>
                                                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                            </td>
                                                            <td>
                                                                <c:set var="fir" value="${one.index}"></c:set>
                                                                <c:set var="sec" value="${two.index}"></c:set>
                                                                <c:set var="thr" value="${status.index}"></c:set>
                                                                <c:forEach var="oldser" items="${otherVersionfdtos}">
                                                                    <wrms:value width="7">
                                                                        <span class="oldVal compareTdStyle" attr="${oldser.fdtoList[fir].sectionDtoList[sec].itemDtoList[thr].incqDto.chkanswer}"><label><c:out value="${oldser.fdtoList[fir].sectionDtoList[sec].itemDtoList[thr].incqDto.chkanswer}"/></label></span>
                                                                    </wrms:value>
                                                                </c:forEach>

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
                                                    <th>Regulation Clause Number</th>
                                                    <th>Item</th>
                                                    <th>Yes</th>
                                                    <th>No</th>
                                                    <th>N/A</th>
                                                    <th>Remark</th>
                                                    <th>Rectified</th>
                                                    <th></th>
                                                </tr>
                                                </thead>
                                                <tbody>

                                                <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                                                    <tr>
                                                        <td class="row_no">${(status.index + 1) }</td>
                                                        <td></td>
                                                        <td><c:out value="${item.question}"/></td>
                                                        <c:set value = "${item.id}" var = "ckkId"/>
                                                        <td><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes"/></td>
                                                        <td>
                                                            <input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No"/>
                                                        </td>
                                                        <td><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A"/></td>
                                                        <td>
                                                            <textarea cols="70" rows="7" name="<c:out value="${item.id}"/>adhocremark" id="<c:out value="${item.id}"/>adhocitemCheckboxRemark" id="" maxlength="500"><c:out value="${item.remark}"/></textarea>
                                                        </td>
                                                        <td>
                                                            <div id="<c:out value="${item.id}"/>ck"<c:if test="${item.adAnswer != 'No'}">hidden</c:if>>
                                                                <input name="<c:out value="${item.id}"/>adhocrec" id="<c:out value="${item.id}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec"/>
                                                            </div>
                                                            <c:set value = "error_${item.id}adhoc" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                        </td>
                                                        <td>
                                                            <%--<c:set var="fir" value="${status.index}"></c:set>
                                                            <c:forEach var="adhocdraft" items="${otherVersionAdhocDraftList}">
                                                                <wrms:value width="7">
                                                                    <span class="oldVal compareTdStyle" attr="${adhocdraft.adItemList[fir].adAnswer}"><label><c:out value="${adhocdraft.adItemList[fir].adAnswer}"/></label></span>
                                                                </wrms:value>
                                                            </c:forEach>--%>
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
    </div>
</form>
<%@ include file="/include/validation.jsp" %>
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
