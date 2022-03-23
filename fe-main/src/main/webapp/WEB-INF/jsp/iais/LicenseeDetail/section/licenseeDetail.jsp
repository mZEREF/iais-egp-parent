<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#licenseeDetail${index}">
                Licensee ${index}: <c:out value="${licensee.licenseeName}" />
            </a>
        </h4>
    </div>
    <div id="licenseeDetail${index}" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>Licensee Details</h3>
                <iais:row>
                    <iais:field width="5" value="Licensee Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.licenseeType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID Type"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.idNumber}" />
                    </iais:value>
                </iais:row>
                <c:if test="${licensee.idType == 'IDTYPE003'}">
                <iais:row>
                    <iais:field width="5" value="Country of issuance"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.nationality}" />
                    </iais:value>
                </iais:row>
                </c:if>
                <iais:row>
                    <iais:field width="5" value="Licensee Name"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.licenseeName}" />
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
                    <iais:field width="5" value="Mobile No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.telephoneNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Email Address"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.emailAddr}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>