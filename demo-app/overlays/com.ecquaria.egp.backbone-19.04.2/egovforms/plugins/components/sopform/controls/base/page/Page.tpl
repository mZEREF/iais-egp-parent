{var id = control.getElementId()}
<div id="${id}" class="page control control-area  container-p-${control.properties.cols}">
	<div id="${id}--errorMsg_page_top" class="error_placements"></div>
	{if Form.Runtime.pageEditLinkText} 
		<div id="edit-link-${id}" class="page-edit-link" >
			<a onclick="Form._pageEditLinkClick()">${Form.Runtime.pageEditLinkText}</a>
		</div>
	{/if}
	<table class="control-grid columns${control.properties.cols}"></table>
	<div id="${id}--errorMsg_page_bottom" class="error_placements"></div>
</div>
