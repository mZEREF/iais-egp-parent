<%@tag description="Other application info tag's supporting doc part of facility registration, also used by preview page" %>
<%-- Attention! This tag will not be used standalone, it will also be used by other tags or jsps. This is the
     reason why master code contants is declared as an attribute here, it should be provided by outer tag/jsp --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="classification" required="true" type="java.lang.String" %>
<%@attribute name="activities" required="false" type="java.util.List<java.lang.String>" %>
<%@attribute name="masterCodeConstants" required="true" type="java.util.Map<java.lang.String, java.lang.Object>"%>

<p>The following is a non-exhaustive list of supporting documents that the facility is required to provide for the application. Some of these may not be available at point of application submission but must be provided subsequently, when available. Please note that incomplete submissions may result in delays to processing or rejection of the application.</p>
<span style="text-decoration: underline; font-weight: bold">Supporting Documents</span>
<c:choose>
    <c:when test="${classification eq masterCodeConstants.FAC_CLASSIFICATION_BSL3}">
        <ol class="no-margin-list" style="padding-left: 20px">
            <li>Facility Administrative Oversight Plan.</li>
            <li>Documentation of successful facility certification.</li>
            <li>Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</li>
            <li>Documentation of approval from relevant ministry or statutory board, where applicable.</li>
            <li>Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</li>
        </ol>
    </c:when>
    <c:when test="${classification eq masterCodeConstants.FAC_CLASSIFICATION_BSL4}">
        <ol class="no-margin-list" style="padding-left: 20px">
            <li>Documentation of approval from relevant ministry or statutory board, where applicable.</li>
            <li>Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</li>
            <li>Documentation of successful facility certification.</li>
            <li>Facility Administrative Oversight Plan.</li>
            <li>Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</li>
        </ol>
    </c:when>
    <c:when test="${classification eq masterCodeConstants.FAC_CLASSIFICATION_UF or classification eq masterCodeConstants.FAC_CLASSIFICATION_LSPF}">
        <ol class="no-margin-list" style="padding-left: 20px">
            <li>Application letter containing the following information:
                <ul class="no-margin-list">
                    <li>The name of the <a href="https://www.moh.gov.sg/biosafety/common/facility-operator">Facility Operator</a> designee;</li>
                    <li>Address of the facility where the intended work will be conducted;</li>
                    <li>The reason for the application; and</li>
                    <li>The justification of how and why the work involving the biological agent and/or toxin can be carried out safely and securely in the intended facility. This may include facility design, the use of laboratory safety equipment, personal protective equipment, good microbiological practices and procedures, as well as reliable and competent personnel.</li>
                </ul>
            </li>
            <c:if test="${classification eq masterCodeConstants.FAC_CLASSIFICATION_LSPF}">
                <li>Description of the biosafety and biosecurity engineering controls at the facility.</li>
            </c:if>
            <li>Details of the facility's biorisk management programme.</li>
            <li>Documentation of approval from the Biosafety Committee for the intended work.</li>
            <c:if test="${classification eq masterCodeConstants.FAC_CLASSIFICATION_LSPF}">
                <li>Documentation of approval from the Health Sciences Authority and any other relevant ministry or statutory board, where applicable.</li>
            </c:if>
            <li>Documentation of endorsement from the Genetic Modification Advisory Committee (if the intended work involves genetic modification of microorganism(s) or handling of genetically modified microorganism(s).</li>
            <li>Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</li>
            <li>Facility Administrative Oversight Plan.</li>
            <li>Facility layout/floorplan.</li>
            <li>Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</li>
            <li>List of all location(s) in the facility where the biological agent(s)/toxin(s) will be handled (including storage) and specify the corresponding work activities that will be carried out at each location (mapped to facility floorplan, as provided in #7). The information can be provided in a table format.</li>
            <li>Risk assessments for the intended work conducted/reviewed/endorsed by the Biosafety Committee.</li>
            <li>Safety and security records related to facility certification, inspection, accreditation, if any.</li>
        </ol>
    </c:when>
    <c:when test="${classification eq masterCodeConstants.FAC_CLASSIFICATION_RF}">
        <c:if test="${activities.get(0) eq masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}">
            <ol class="no-margin-list" style="padding-left: 20px">
                <li>Details related to the biosafety and biosecurity measures at the facility.</li>
                <li>List of facility personnel authorized to handle the toxin(s) for exempted purposes.</li>
            </ol>
        </c:if>
        <c:if test="${activities.get(0) eq masterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL}">
            <ol class="no-margin-list" style="padding-left: 20px">
                <li>A signed inventory reporting form for each facility with poliovirus potentially infectious materials.</li>
                <li>Risk mitigation plan for the handling of poliovirus potentially infectious materials detailing the safety and security measures that are in place.</li>
            </ol>
        </c:if>
    </c:when>
    <c:otherwise>
    </c:otherwise>
</c:choose>