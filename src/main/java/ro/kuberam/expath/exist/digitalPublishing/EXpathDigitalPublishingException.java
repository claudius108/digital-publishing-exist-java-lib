package ro.kuberam.expath.exist.digitalPublishing;

import org.exist.xquery.ErrorCodes.ErrorCode;
import org.exist.xquery.Expression;
import org.exist.xquery.XPathException;

import ro.kuberam.libs.java.digitalPublishing.DigitalPublishingError;

/**
 * eXist-db EXPath Cryptographic library eXist-db wrapper for EXPath
 * Cryptographic Java library Copyright (C) 2018 Claudius Teodorescu
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

public class EXpathDigitalPublishingException extends XPathException {

	private static final long serialVersionUID = -6789727720893604433L;

	public EXpathDigitalPublishingException(Expression expr, DigitalPublishingError error) {
		super(expr, new ExpathDigitalPublishingErrorCode(error), error.getDescription());
	}

	public EXpathDigitalPublishingException(Expression expr, Exception exception) {
		super(expr, new ExpathDigitalPublishingErrorCode(exception.getClass().getCanonicalName(), exception.toString()),
				exception.toString());
	}

	public EXpathDigitalPublishingException(Expression expr, ErrorCode errorCode, String description) {
		super(expr, errorCode, description);
	}
}
