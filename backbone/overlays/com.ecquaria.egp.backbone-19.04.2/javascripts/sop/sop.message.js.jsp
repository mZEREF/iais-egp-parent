<%@page contentType="text/javascript" %>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>

<egov-smc:message id="clearQns" key="clearQns">Are you sure you want to clear?</egov-smc:message>
<egov-smc:message id="exportQns" key="exportQns">Are you sure you want to export?</egov-smc:message>
<egov-smc:message id="exportAllQns" key="exportAllQns">Are you sure you want to export all?</egov-smc:message>
<egov-smc:message id="resetQns" key="resetQns">Are you sure you want to reset?</egov-smc:message>
<egov-smc:message id="noMsg" key="noMsg">No</egov-smc:message>
<egov-smc:message id="selectedMsg" key="selectedMsg">selected.</egov-smc:message>
<egov-smc:message id="deleteMsg" key="deleteMsg">to delete.</egov-smc:message>
<egov-smc:message id="exportMsg" key="exportMsg">to export.</egov-smc:message>

<egov-smc:commonLabel id="okLabel">OK</egov-smc:commonLabel>
<egov-smc:commonLabel id="cancelLabel">Cancel</egov-smc:commonLabel>
<egov-smc:commonLabel id="confirmLabel">Confirm</egov-smc:commonLabel>
<egov-smc:commonLabel id="alertLabel">Alert</egov-smc:commonLabel>

if (!window.SOP) window.SOP = {};
if (!SOP.Message) SOP.Message = {};

(function(namespace) {
    namespace.clearQns = '<egov-core:escapeJavaScript value="${clearQns}"/>';
    namespace.exportQns = '<egov-core:escapeJavaScript value="${exportQns}"/>';
    namespace.exportAllQns = '<egov-core:escapeJavaScript value="${exportAllQns}"/>';
    namespace.resetQns = '<egov-core:escapeJavaScript value="${resetQns}"/>';
    namespace.noMsg = '<egov-core:escapeJavaScript value="${noMsg}"/>';
    namespace.selectedMsg = '<egov-core:escapeJavaScript value="${selectedMsg}"/>';
    namespace.deleteMsg = '<egov-core:escapeJavaScript value="${deleteMsg}"/>';
    namespace.exportMsg = '<egov-core:escapeJavaScript value="${exportMsg}"/>';
    namespace.okLabel = '<egov-core:escapeJavaScript value="${okLabel}"/>';
    namespace.cancelLabel = '<egov-core:escapeJavaScript value="${cancelLabel}"/>';
    namespace.confirmLabel = '<egov-core:escapeJavaScript value="${confirmLabel}"/>';
    namespace.alertLabel = '<egov-core:escapeJavaScript value="${alertLabel}"/>';
})(SOP.Message);