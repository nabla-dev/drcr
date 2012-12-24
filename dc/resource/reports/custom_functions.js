function currencyWithoutZero(value)
{
	return value == 0 ? null : value;
}
function currencyWithZero(value)
{
	return value == null ? 0 : value;
}
function booleanAsString(value)
{
	return value == 0 ? "false" : "true";
}
function exportOutput(format)
{
	return ((format == "csv") || (format == "xml"))
}
function exportCurrentOutput()
{
	return exportOutput(reportContext.getOutputFormat( ))
}