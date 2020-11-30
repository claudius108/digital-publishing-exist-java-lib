xquery version "3.1";

import module namespace digi-pub = "http://expath.org/ns/digital-publishing/";

declare namespace xslfo="http://exist-db.org/xquery/xslfo";

let $html :=
    <html xmlns="http://www.w3.org/1999/xhtml">
    	<head>
    		<title />
    		<style type="text/css" xml:space="preserve">
    			body {{
    				font-family: arial;
    				font-size: 12px;
    				text-align: center;
    			}}
    			table {{
    				border-collapse: collapse;
    				 width: 100%;
    				 border: solid black 1px;
    			}}
    			table th, td {{
    				border: solid black 1px;
    			}}
    			@media screen {{
    				body {{
    					width: 570px;
    				}}
    			}}
    			@media print {{
    				table th {{
    					font-weight: bold;
    				}}
    			}}
    		</style>
    	</head>
    	<body>
    		<h2>Invoices summary</h2>
    		<table>
    			<thead>
    				<tr>
    					<th style="width: 30px;">
    						Crt.
    						<br />
    						no.
    					</th>
    					<th style="width: 80px;">
    						Invoice
    						<br />
    						no.
    					</th>
    					<th style="width: 80px;">Issue date</th>
    					<th style="width: 80px;">Amount</th>
    					<th style="width: 80px;">VAT</th>
    					<th style="width: 80px;">VAT base</th>
    					<th style="width: 80px;">Customer ID</th>
    				</tr>
    			</thead>
    			<tbody>
    				<tr>
    					<td>1. </td>
    					<td>00001</td>
    					<td>2011-10-17</td>
    					<td>EURO 108</td>
    					<td>19.47</td>
    					<td>22</td>
    					<td>0001008</td>
    				</tr>
    				<tr>
    					<td>2. </td>
    					<td>00002</td>
    					<td>2011-10-17</td>
    					<td>EURO 40</td>
    					<td>7.21</td>
    					<td>22</td>
    					<td>0000017</td>
    				</tr>
    				<tr>
    					<td>3. </td>
    					<td>00003</td>
    					<td>2011-10-17</td>
    					<td>EURO 1700</td>
    					<td>306.56</td>
    					<td>22</td>
    					<td>0000040</td>
    				</tr>
    			</tbody>
    		</table>
    	</body>
    </html>

let $xsl-fo := digi-pub:transform($html, "html", "xsl-fo")
let $pdf := xslfo:render($xsl-fo, "application/pdf", ())

return response:stream-binary( $pdf, "application/pdf", "output.pdf" ) 