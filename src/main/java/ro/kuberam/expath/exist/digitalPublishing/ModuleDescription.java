/**
 * eXist-db EXPath Cryptographic library
 * eXist-db wrapper for EXPath Cryptographic Java library
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
package ro.kuberam.expath.exist.digitalPublishing;

import java.util.List;
import java.util.Map;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

import ro.kuberam.expath.exist.digitalPublishing.transform.TransformFunction;

import static org.exist.xquery.FunctionDSL.functionDefs;

/**
 * Implements the module definition.
 *
 * @author <a href="mailto:claudius.teodorescu@gmail.com">Claudius
 *         Teodorescu</a>
 */
public class ModuleDescription extends AbstractInternalModule {

	public static final String NAMESPACE_URI = ro.kuberam.libs.java.digitalPublishing.ModuleDescription.NAMESPACE_URI;
	public static final String PREFIX = ro.kuberam.libs.java.digitalPublishing.ModuleDescription.PREFIX;

	public final static String INCLUSION_DATE = "2018-11-30";
	public final static String RELEASED_IN_VERSION = "eXist-4.4.0";

	private final static FunctionDef[] functions = functionDefs(
			functionDefs(TransformFunction.class, TransformFunction.FS_TRANSFORM));

	public ModuleDescription(final Map<String, List<? extends Object>> parameters) throws Exception {
		super(functions, parameters);
	}

	@Override
	public String getNamespaceURI() {
		return NAMESPACE_URI;
	}

	@Override
	public String getDefaultPrefix() {
		return PREFIX;
	}

	@Override
	public String getDescription() {
		return ro.kuberam.libs.java.digitalPublishing.ModuleDescription.MODULE_DESCRIPTION;
	}

	@Override
	public String getReleaseVersion() {
		return RELEASED_IN_VERSION;
	}
}
