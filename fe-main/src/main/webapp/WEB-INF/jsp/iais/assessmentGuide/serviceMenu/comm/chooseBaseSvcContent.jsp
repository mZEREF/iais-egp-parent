<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<style>
    .align-lic-table{
        margin-left: -30px;
    }
</style>
<c:if test="${!noExistBaseLic}">
    <div class="row">
        <div class="col-xs-12 col-md-6">
            <h3>
                You may choose to align to one of the following licences.
            </h3>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-md-6">
            <div class="table-responsive">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr style="font-size: 16px;">
                        <th scope="col"></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Licence No.</strong></label></div></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Type</strong></label></div></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Mode of Service Delivery</strong></label></div></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Expires On</strong></label></div></th>
                    </tr>
                    </thead>
                    <tbody id="licBodyDiv"></tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-6 col-md-6">
            <div id="licPagDiv"></div>
        </div>
    </div>
</c:if>

<c:if test="${noExistBaseLic&&!noExistBaseApp}">
    <div class="row">
        <div class="col-xs-12 col-md-6">
            <h3>
                You may choose to align to one of the following applications.
            </h3>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 col-md-6">
            <div class="table-responsive">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr style="font-size: 16px;">
                        <th scope="col"></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Application No.</strong></label></div></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Type</strong></label></div></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Mode of Service Delivery</strong></label></div></th>
                        <th scope="col"><div class="form-check align-lic-table"><label class="form-check-label"><strong>Licensee</strong></label></div></th>
                    </tr>
                    </thead>
                    <tbody id="appBodyDiv"></tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-6 col-md-6">
            <div id="appPagDiv"></div>
        </div>
    </div>
</c:if>

<div class="row">
    <div class="col-xs-12 col-md-6">
        <c:if test="${!empty chooseBaseErr}">
            <span class="error-msg">${chooseBaseErr}</span>
        </c:if>
    </div>
</div>
