<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="row form-horizontal normal-label">
    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </iais:row>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
    <iais:row>
        <div class="col-xs-12">
            <h2 class="app-title">Outsourced Service(s)</h2>
            <p><h4><iais:message key="NEW_ACK41"/></h4></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>

    <div class="searchService">
        <div class="col-md-12 col-xs-12">
            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5" mandatory="true" value="Service"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="outsourcedServiceSel" name="outsourcedServiceSelect" options="outsourcedServiceSelectOpts"/>
                    </iais:value>
                </iais:row>
            </div>

            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Business Name"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" cssClass="name" name="name"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>

        <div class="col-md-12 col-xs-12">
            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Licence No. "/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="20" type="text" cssClass="liNo" name="liNo"/>
                    </iais:value>
                </iais:row>
            </div>

            <div class="col-xs-6 col-md-6">
                <iais:row>
                    <iais:field width="5"  value="Postal Code"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="6" type="number" cssClass="postalCode" name="postalCode"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>

        <div class="col-xs-12 col-md-12">
            <div class="col-xs-6 col-md-6"></div>
            <div class="col-xs-6 col-md-6" style="padding-left: 200px;!important;">
                <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
            </div>
        </div>
    </div>

    <div class="components">
<%--        <iais:pagination  param="msgSearchParam" result="msgSearchResult"/>--%>
        <br><br>
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr><th scope="col" style="display: none"></th>
                    <iais:sortableHeader style="width:5%" needSort="true"  field="" value="Service" ></iais:sortableHeader>
                    <iais:sortableHeader style="width:10%" needSort="true"   field="" value="Licence No."></iais:sortableHeader>
                    <iais:sortableHeader style="width:10%" needSort="true"   field="" value="Business Name"></iais:sortableHeader>
                    <iais:sortableHeader style="width:5%" needSort="true"   field="" value="Address"></iais:sortableHeader>
                    <iais:sortableHeader style="width:5%" needSort="true"   field="" value="Licence Tenure"></iais:sortableHeader>
                    <iais:sortableHeader style="width:5%" needSort="false"   field="" value="Date of Agreement"></iais:sortableHeader>
                    <iais:sortableHeader style="width:5%" needSort="false"   field="" value="End Date of Agreement"></iais:sortableHeader>
                    <iais:sortableHeader style="width:1%" needSort="false"   field="" value="action"></iais:sortableHeader>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <iais:row>
        <div class="col-xs-12">
            <hr>
            <p>Outsourced Service Provider(s)</p>
        </div>
    </iais:row>

</div>