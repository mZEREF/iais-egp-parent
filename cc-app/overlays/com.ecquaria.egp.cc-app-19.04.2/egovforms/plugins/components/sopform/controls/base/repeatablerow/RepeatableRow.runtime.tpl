{var id = control.getElementId()}
<div id="${id}" class="repeatable-row control ${control.properties.developerclass} container-rr-id-${control.properties.id}">
	{if control.properties.border == true}
	<fieldset>
		<legend><span class="control-set-font control-font-header">${control.properties.title|escape}</span><span class="upload_controls"></span></legend>
	{/if}
	{if control.properties.border == false && control.properties.title}
		<div class="control-set-font control-font-header section-header"><label>${control.properties.title|escape}</label></div>
	{/if}
		<table class="control-grid control-set-font control-font-label">
		</table>
		<div class="repeatable-row-control-button">
			<input id="${id}--add-row" type="button" value="${control.properties.controlButton}" />
		</div>
	{if control.properties.border == true}
	</fieldset>
	{/if}
</div>
