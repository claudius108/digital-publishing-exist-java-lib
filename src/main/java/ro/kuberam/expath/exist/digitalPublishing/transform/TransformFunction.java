/**
 * eXist-db EXPath Digital Publishing library
 * eXist-db wrapper for EXPath Digital Publishing Java library
 * Copyright (C) 2018 Claudius Teodorescu
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ro.kuberam.expath.exist.digitalPublishing.transform;

/**
 * Implements the digi-pub:transform() function for eXist.
 *
 * @author <a href="mailto:claudius.teodorescu@gmail.com">Claudius Teodorescu</a>
 */
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.exist.xquery.FunctionDSL.arities;
import static org.exist.xquery.FunctionDSL.arity;
import static org.exist.xquery.FunctionDSL.functionSignatures;
import static org.exist.xquery.FunctionDSL.optParam;
import static org.exist.xquery.FunctionDSL.param;
import static org.exist.xquery.FunctionDSL.returnsOptMany;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exist.Namespaces;
import org.exist.dom.QName;
import org.exist.dom.memtree.SAXAdapter;
import org.exist.storage.serializers.Serializer;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ro.kuberam.expath.exist.digitalPublishing.ModuleDescription;
import ro.kuberam.libs.java.digitalPublishing.transform.Operation;

public class TransformFunction extends BasicFunction {

	private static Logger LOG = LogManager.getLogger(TransformFunction.class);

	private static QName FS_TRANSFORM_NAME = new QName("transform", ModuleDescription.NAMESPACE_URI);

	private static String FS_TRANSFORM_DESCRIPTION = "Transforms a document from one digital publishing format to another";

	private static FunctionParameterSequenceType FS_TRANSFORM_PARAM_INPUT_DOCUMENT = param("input-document",
			Type.ANY_TYPE, "The document to be transformed from source format to target format.");
	private static FunctionParameterSequenceType FS_TRANSFORM_PARAM_INPUT_FORMAT = param("input-format", Type.STRING,
			"The format of the source document.");
	private static FunctionParameterSequenceType FS_TRANSFORM_PARAM_OUTPUT_FORMAT = param("output-format", Type.STRING,
			"The format of the target document.");
	private static FunctionParameterSequenceType FS_TRANSFORM_PARAM_OPTIONS = optParam("options", Type.STRING,
			"The options for the current operation.");

	public final static FunctionSignature FS_TRANSFORM[] = functionSignatures(FS_TRANSFORM_NAME,
			FS_TRANSFORM_DESCRIPTION, returnsOptMany(Type.NODE, "the output document as node()"),
			arities(arity(FS_TRANSFORM_PARAM_INPUT_DOCUMENT, FS_TRANSFORM_PARAM_INPUT_FORMAT,
					FS_TRANSFORM_PARAM_OUTPUT_FORMAT),
					arity(FS_TRANSFORM_PARAM_INPUT_DOCUMENT, FS_TRANSFORM_PARAM_INPUT_FORMAT,
							FS_TRANSFORM_PARAM_OUTPUT_FORMAT, FS_TRANSFORM_PARAM_OPTIONS)));

	public TransformFunction(final XQueryContext context, final FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(final Sequence[] args, final Sequence contextSequence) throws XPathException {

		Sequence inputDocumentSequence = args[0];
		if (inputDocumentSequence.isEmpty()) {
			return Sequence.EMPTY_SEQUENCE;
		}

		Item inputDocumentItem = inputDocumentSequence.itemAt(0);
		int inputType = inputDocumentItem.getType();
		LOG.debug("inputType = {}", () -> inputType);
		String inputFormat = args[1].getStringValue();
		LOG.debug("inputFormat = {}", () -> inputFormat);
		String outputFormat = args[2].getStringValue();
		LOG.debug("outputFormat = {}", () -> outputFormat);

		Sequence result = new ValueSequence();
		Document libResult = null;

		switch (inputType) {
		case Type.STRING:
		case Type.ELEMENT:
		case Type.DOCUMENT:
			Serializer serializer = context.getBroker().getSerializer();
			NodeValue inputDocumentNode = (NodeValue) inputDocumentItem;

			String inputDocumentStringValue = null;
			try {
				String serializationResult = serializer.serialize(inputDocumentNode);
				LOG.debug("inputDocument = {}", () -> serializationResult);

				inputDocumentStringValue = serializationResult;
			} catch (SAXException e) {
				// TODO Add meaningfull error
				e.printStackTrace();
			}

			String operationResult = new String(Operation.run(inputDocumentStringValue.getBytes(UTF_8), inputFormat, outputFormat),
					UTF_8);
			LOG.debug("operationResult = {}", operationResult);
			
			try {
				result = (Sequence) stringToNode(operationResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//
			// case Type.BASE64_BINARY:
			// case Type.HEX_BINARY:
			// InputStream inputDocumentBinaryValue = ((BinaryValue)
			// inputDocumentItem).getInputStream();
			//
			// libResult =
			// Operation.run(InputStreamToByteArray.run(inputDocumentBinaryValue),
			// inputFormat, outputFormat);
		}

		return result;
	}

	private Document stringToNode(String signatureString) throws Exception {
		// process the output (signed) document from string to node()
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			SAXParser parser = factory.newSAXParser();
			XMLReader xr = parser.getXMLReader();
			SAXAdapter adapter = new SAXAdapter(context);
			xr.setContentHandler(adapter);
			xr.setProperty(Namespaces.SAX_LEXICAL_HANDLER, adapter);
			xr.parse(new InputSource(new StringReader(signatureString)));

			return adapter.getDocument();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Exception(e.getMessage());
		}
	}
}