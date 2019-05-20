<select id="property::${id}::${name}" class="property-input">
{for row in options}
	<option value="${row[0]|escape}" {if row[0] == value} selected {/if} >${row[1]|escape}</option>
{/for}
</select>