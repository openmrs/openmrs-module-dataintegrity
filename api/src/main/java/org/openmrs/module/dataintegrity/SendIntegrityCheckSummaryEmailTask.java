package org.openmrs.module.dataintegrity;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.CommonsLogLogChute;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.VelocityExceptionHandler;

/**
 * This class is called by the scheduler whenever the time to send the Data
 * Integrity Summary checks is attained and sends out an e-mail message.
 */
public class SendIntegrityCheckSummaryEmailTask extends AbstractTask {

	private final Log log = LogFactory.getLog(getClass());

	private DataIntegrityService getDataIntegrityService() {
		return (DataIntegrityService) Context
				.getService(DataIntegrityService.class);
	}

	/**
	 * sends a summary of integrity checks in an email
	 * 
	 * @should send an email and not fail
	 */
	public void execute() {

		if (taskDefinition.getProperty("emailsVal") != null) {

			DataIntegrityService service = getDataIntegrityService();

			// get the most recent check runs
			List<IntegrityCheckRun> runs = service.getMostRecentRunsForAllChecks();

			// build the message body

			// TODO make this truly multipart (plain and html going out at the same time)
			Multipart mp = new MimeMultipart();

			try {
				if (OpenmrsUtil.nullSafeEquals(
						Context.getAdministrationService()
								.getGlobalProperty("dataintegrity.mail.format"),
						"plain")) {
					BodyPart plain = new MimeBodyPart();
					plain.setText(generateEmail(runs,
							DataIntegrityConstants.TEMPLATE_EMAIL_PLAIN));
					mp.addBodyPart(plain);
				} else {
					BodyPart html = new MimeBodyPart();
					html.setContent(
							generateEmail(
									runs,
									DataIntegrityConstants.TEMPLATE_EMAIL_HTML),
							"text/html");
					mp.addBodyPart(html);
				}

				// send the email
				sendEmail(mp, taskDefinition.getProperty("emailsVal")
						.split(";"));
				log.info("Data Integrity Checks summary email sent");
			} catch (MessagingException e) {
				log.error("An error occured while sending the Data Integrity summary email", e);
			}
		} else {
			log.error("There are no email addresses to be sent to on the scheduler.");
		}
	}

	/**
	 * sends an email comprised of the multipart body to the designated
	 * recipients, utilizing dataintegrity global properties for connection
	 * details
	 * 
	 * @param mp
	 * @param recipients
	 * @throws AddressException
	 * @throws MessagingException
	 */
	protected void sendEmail(Multipart mp, String[] recipients)
			throws AddressException, MessagingException {
		// get the email connectivity information
		AdministrationService as = Context.getAdministrationService();
		final String protocol = as
				.getGlobalProperty("dataintegrity.mail.protocol");
		final String from = as.getGlobalProperty("dataintegrity.mail.from");
		final String host = as.getGlobalProperty("dataintegrity.mail.host");
		final String pwd = as.getGlobalProperty("dataintegrity.mail.password");
		final String usr = as.getGlobalProperty("dataintegrity.mail.user");

		Integer port = 0;
		try {
			port = Integer.parseInt(as
					.getGlobalProperty("dataintegrity.mail.port"));
		} catch (NumberFormatException e) {
			throw new APIException("port number "
					+ as.getGlobalProperty("dataintegrity.mail.port")
					+ " is not valid", e);
		}

		// set up the mail properties
		Properties props = new Properties();
		props.put("mail.transport.protocol", protocol);
		props.put("mail." + protocol + ".host", host);
		props.put("mail." + protocol + ".port", port);
		props.put("mail." + protocol + ".from", from);
		props.put("mail." + protocol + ".starttls.enable",
				as.getGlobalProperty("dataintegrity.mail.tls"));
		props.put("mail." + protocol + ".auth",
				as.getGlobalProperty("dataintegrity.mail.auth"));

		// get a session
		Session session = Session.getDefaultInstance(props);

		// create email message
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setSubject(as.getGlobalProperty("dataintegrity.mail.subject"));
		for (String rpt : recipients)
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(rpt));

		// set the message content
		message.setContent(mp);

		// send the message
		Transport t = session.getTransport();
		t.connect(host, port, usr, pwd);
		t.sendMessage(message, message.getAllRecipients());
	}

	/**
	 * generates an email from a set of results and a given template by applying
	 * the template to the results via velocity
	 * 
	 * @param results
	 * @param templateName
	 * @return
	 */
	protected String generateEmail(List<IntegrityCheckRun> runs, String templateName) {
		AdministrationService as = Context.getAdministrationService();

		// set up the engine
		VelocityEngine velocityEngine = new VelocityEngine();

		velocityEngine.setProperty(
				RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.CommonsLogLogChute");
		velocityEngine.setProperty(
				CommonsLogLogChute.LOGCHUTE_COMMONS_LOG_NAME,
				"dataintegrity_velocity");

		try {
			velocityEngine.init();
		} catch (Exception e) {
			log.error("Error initializing Velocity engine", e);
		}

		VelocityContext velocityContext = new VelocityContext();

		// add the error handler
		EventCartridge ec = new EventCartridge();
		ec.addEventHandler(new VelocityExceptionHandler());
		velocityContext.attachEventCartridge(ec);

		// Set up velocity context
		velocityContext.put("runs", runs);
		velocityContext.put("serverPath", 
				as.getGlobalProperty("dataintegrity.mail.serverpath"));

		// get template
		String template = "";

		// grab resource folder using Class.getResource()
		Class<SendIntegrityCheckSummaryEmailTask> c = SendIntegrityCheckSummaryEmailTask.class;
		URL url = c.getResource(DataIntegrityConstants.TEMPLATE_FOLDER);
		if (url == null) {
			String err = "Could not open resource folder directory: "
					+ DataIntegrityConstants.TEMPLATE_FOLDER;
			log.error(err);
			throw new APIException(err);
		}
		File templateFolder = OpenmrsUtil.url2file(url);

		// look for the template and load it
		if (OpenmrsUtil.folderContains(templateFolder, templateName)) {
			File templateFile = new File(templateFolder, templateName);
			try {
				template = OpenmrsUtil.getFileAsString(templateFile);
			} catch (IOException e) {
				log.error("could not read velocity template: " + templateName,
						e);
			}
		}

		// process the velocity script
		StringWriter email = new StringWriter();
		PrintWriter writer = new PrintWriter(email);
		try {
			velocityEngine.evaluate(velocityContext, writer,
					"Integrity Check Summary Email", template);
		} catch (Exception e) {
			log.error(e);
		} finally {
			writer.close();
			velocityContext.remove("checks");
			velocityContext = null;
			velocityEngine = null;
			template = null;
		}

		// send back the evaluation
		return email.toString();
	}
}
