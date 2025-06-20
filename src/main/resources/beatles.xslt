<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sparql="http://www.w3.org/2005/sparql-results#">

    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Beatles Albums</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    table { border-collapse: collapse; width: 100%; margin-top: 20px; }
                    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                    th { background-color: #f2f2f2; font-weight: bold; }
                    tr:nth-child(even) { background-color: #f9f9f9; }
                    h1 { color: #333; }
                </style>
            </head>
            <body>
                <h1>Beatles Albums</h1>
                <table>
                    <thead>
                        <tr>
                            <th>Album Name</th>
                            <th>Release Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="//sparql:result">
                            <tr>
                                <td><xsl:value-of select="sparql:binding[@name='name']/sparql:literal"/></td>
                                <td><xsl:value-of select="sparql:binding[@name='release']/sparql:literal"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>