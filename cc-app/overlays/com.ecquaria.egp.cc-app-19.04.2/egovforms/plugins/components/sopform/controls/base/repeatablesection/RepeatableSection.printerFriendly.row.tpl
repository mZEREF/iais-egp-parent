{var idx = index.actual}
{var id = control.getElementId(idx)}
{if control.properties.border == true}
<fieldset>
	<legend class="control-set-font control-font-header">${control.properties.title}</legend>
{/if}
{if control.properties.border == false}
	<span class="control-set-font control-font-header">${control.properties.title|escape}</span>
{/if}
	<div id="${id}**errorMsg_section_top" class="error_placements"></div>
	<table class="control-grid">
	</table>
	<div id="${id}**errorMsg_section_bottom" class="error_placements"></div>
{if control.properties.border == true}
</fieldset>
{/if}
