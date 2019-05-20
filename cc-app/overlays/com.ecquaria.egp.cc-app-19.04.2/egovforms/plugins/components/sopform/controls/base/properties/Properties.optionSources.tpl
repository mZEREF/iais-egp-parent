{var checkedStringDefault}
{var checkedStringServerLookup}
{eval}
	var dataArray = optionLookupManager.getDataArrayFor(id);

	var sourceType = dataArray['source_type'];
	checkedStringDefault = (sourceType == 'default')?"checked":"";
	checkedStringServerLookup = (sourceType == 'server_lookup')?"checked":"";
{/eval}
<div test_id="${id}" test_source_type="${source_type}">
	<input type="radio" name="source_radio_${id}" value="default" ${checkedStringDefault}>Default</radio>
	<br>
	<input type="radio" name="source_radio_${id}" value="server_lookup" ${checkedStringServerLookup}>Lookup</radio>
</div>
