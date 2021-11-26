<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#companyDetail">
                Company Details
            </a>
        </h4>
    </div>
    <div id="companyDetail" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>Company Details</h3>
                <iais:row>
                    <iais:field width="5" value="Who is the licensee"/>
                    <iais:value width="7" display="true">
                        <c:set var="entityType" value="${licensee.licenseeEntityDto.entityType}"/>
                        <c:if test="${empty entityType || '-' == entityType}" var="invalidType">
                            <iais:code code="${licensee.licenseeType}" />
                        </c:if>
                        <c:if test="${not invalidType}">
                            <iais:code code="${entityType}" />
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="UEN No."/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.uenNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Licensee Name"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Postal Code"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.postalCode}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Address Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.addrType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Block / House No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.blkNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Floor No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.floorNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Unit No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.unitNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Street Name"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.streetName}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Building Name"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.buildingName}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Office Telephone No." mandatory="true"/>
                    <iais:value width="7" display="true">
                        <iais:input maxLength="8" type="text" name="officeTelNo" id="officeTelNo" value="${licensee.officeTelNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Office Email Address" mandatory="true"/>
                    <iais:value width="7" display="true">
                        <iais:input maxLength="320" type="text" name="officeEmail" id="officeEmail" value="${licensee.emilAddr}"/>
                    </iais:value>
                </iais:row>
            </div>
            <c:forEach var="item" items="${person}" varStatus="status">
                <c:set var="index" value="${status.index + 1}" />
                <%@include file="boardMember.jsp" %>
            </c:forEach>
        </div>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>