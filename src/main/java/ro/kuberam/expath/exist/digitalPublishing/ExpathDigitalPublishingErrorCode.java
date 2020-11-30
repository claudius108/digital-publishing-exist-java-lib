package ro.kuberam.expath.exist.digitalPublishing;

import org.exist.dom.QName;
import org.exist.xquery.ErrorCodes.ErrorCode;

import ro.kuberam.libs.java.digitalPublishing.DigitalPublishingError;

public class ExpathDigitalPublishingErrorCode extends ErrorCode {
	public ExpathDigitalPublishingErrorCode(String code, String description) {
		super(new QName(code, ro.kuberam.libs.java.digitalPublishing.ModuleDescription.NAMESPACE_URI,
				ro.kuberam.libs.java.digitalPublishing.ModuleDescription.PREFIX), description);
	}

	public ExpathDigitalPublishingErrorCode(DigitalPublishingError error) {
		super(new QName(error.getCode(), ro.kuberam.libs.java.digitalPublishing.ModuleDescription.NAMESPACE_URI,
				ro.kuberam.libs.java.digitalPublishing.ModuleDescription.PREFIX), error.getDescription());
	}
}
