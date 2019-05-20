{var id = control.getElementId()}
<div id="${id}" class="repeatable-row control ${control.properties.developerclass}"
		{if control.properties.border == false} style="border: 1px solid #e0e0e0;" {/if}>

	{if control.properties.border == true}
	<fieldset>
		<legend class="control-set-font control-font-header">${control.properties.title|escape}</legend>
	{/if}
	{if control.properties.border == false && control.properties.title}
		<div class="control-set-font control-font-header section-header"><label>${control.properties.title|escape}</label></div>
	{/if}
		<table class="control-grid control-set-font control-font-label"
			{if control.properties.fieldset == false} style="margin-top: 15px;" {/if}>
		</table>
		<div class="repeatable-row-control-button">
			<input id="${id}--add-row" type="button" value="${control.properties.controlButton}" />
		</div>
	{if control.properties.border == true}
	</fieldset>
	{/if}
</div>