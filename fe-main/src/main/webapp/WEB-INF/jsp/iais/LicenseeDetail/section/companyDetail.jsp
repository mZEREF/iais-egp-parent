<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#companyDetail">
                Company Details
            </a>
        </h4>
    </div>
    <div id="companyDetail" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>Company Details</h3>
                <iais:row>
                    <iais:field width="5" value="Who is the licensee"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.licenseeType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="UEN No."/>
                    <iais:value width="7" display="true">
                        <iais:code code="${licensee.uenNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Licensee"/>
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
                    <iais:value width="7">
                        <c:out value="${licensee.floorNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Unit No."/>
                    <iais:value width="7">
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
                    <iais:field width="5" value="Office Telephone No."/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.officeTelNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Office Email Address"/>
                    <iais:value width="7" display="true">
                        <c:out value="${licensee.emilAddr}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>