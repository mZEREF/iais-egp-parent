{var id = control.getElementId()}
<div id="${id}" class="section control ${control.properties.developerclass} container-s-${control.properties.cols}">
	{if control.properties.border == true}
	<fieldset>
		<legend class="control-set-font control-font-header">${control.properties.title} <span class="upload_controls"></span></legend>
	{/if}
	{if control.properties.border == false && control.properties.title}
		<div class="control-set-font control-font-header section-header"><label>${control.properties.title|escape}</label></div>
	{/if}
		<span class="upload_controls"></span>
		<div id="${id}--errorMsg_section_top" class="error_placements"></div>
		<table class="control-grid">
		</table>
		<div id="${id}--errorMsg_section_bottom" class="error_placements"></div>
	{if control.properties.border == true}
	</fieldset>
	{/if}
</div>
