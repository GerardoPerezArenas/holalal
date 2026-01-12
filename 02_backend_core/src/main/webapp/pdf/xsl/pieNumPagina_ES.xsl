<?xml version = "1.0" encoding = "ISO-8859-1"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]> <!--Esto declara &nbsp; como una entidad XML utilizando su valor Unicode &#160;.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="ROWSET">
<table width="255mm" align="center">
    <tr>
      <td align="center" height="10mm"></td>
    </tr>
    <tr><td height="2mm"></td></tr>
    <tr>
        <td align="center" colspan="3" >
            <xsl:element name="span">
                <xsl:attribute name="style">text-align: right; display: block;</xsl:attribute>
                    <xsl:text> Página&nbsp; </xsl:text><pagenumber/>
                </xsl:element>
            <xsl:element name="span">
                <xsl:attribute name="style">text-align: right; display: block;</xsl:attribute>
                <xsl:text> de &nbsp; </xsl:text><totalpages/>
            </xsl:element>
        </td>
    </tr>
</table>
</xsl:template>



<xsl:template match="ASUNTO">
		<td width="120mm" align="left" padding-top="1mm" padding-left="0mm" padding-right="15mm"><xsl:value-of select="."/> </td>
</xsl:template>

</xsl:stylesheet>



