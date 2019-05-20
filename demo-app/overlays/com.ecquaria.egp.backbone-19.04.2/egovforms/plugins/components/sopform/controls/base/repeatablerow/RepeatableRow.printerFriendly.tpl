{var id = control.getElementId()}
<div id="${id}" class="repeatable-row control">
	{if instructions}
		<div class="pf_instructions">
			{for instruction in instructions}
				<span class="control-input control-set-font control-font-normal">${instruction}</span><br/>
			{/for}
		</div>
	{/if}
	{if control.properties.border == true}
	<fieldset>
		<legend><span class="control-set-font control-font-header">${control.properties.title|escape}</span></legend>
	{/if}
		<table class="control-grid control-set-font control-font-label">
		</table>
	{if control.properties.border == true}
	</fieldset>
	{/if}
</div>
