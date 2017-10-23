package com.sendgrid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Mail builds an object that sends an email through SendGrid. 
 * Note that this object is not thread safe.
 */
@JsonInclude(Include.NON_DEFAULT)
public class Mail {

    @JsonProperty("from")
    private Email from;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("personalizations")
    private List<Personalization> personalization;

    @JsonProperty("content")
    private List<Content> content;

    @JsonProperty("attachments")
    private List<Attachment> attachments;

    @JsonProperty("template_id")
    private String templateId;

    @JsonProperty("sections")
    private Map<String, String> sections;

    @JsonProperty("headers")
    private Map<String, String> headers;

    @JsonProperty("categories")
    private List<String> categories;

    @JsonProperty("custom_args")
    private Map<String, String> customArgs;

    @JsonProperty("send_at")
    private long sendAt;

    @JsonProperty("batch_id")
    private String batchId;

    @JsonProperty("asm")
    private ASM asm;

    @JsonProperty("ip_pool_name")
    private String ipPoolId;

    @JsonProperty("mail_settings")
    private MailSettings mailSettings;

    @JsonProperty("tracking_settings")
    private TrackingSettings trackingSettings;

    @JsonProperty("reply_to")
    private Email replyTo;

    private static final ObjectMapper SORTED_MAPPER = new ObjectMapper();
    static {
        SORTED_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    /** Construct a new Mail object. */
    public Mail() {
        
    }

    /**
     * Get the email's from address.
     * @return the email's from address.
     */
    @JsonProperty("from")
    public Email getFrom() {
        return this.from;
    }

    /**
     * Set the email's from address.
     * @param from the email's from address.
     * @return this object.
     */
    public Mail from(Email from) {
        this.from = from;
        return this;
    }

    /**
     * Get the email's to address list.
     * @return all the to addresses.
     */
    @JsonIgnore
    public List<Email> getTos() {
        List<Email> l = new ArrayList<>();
        if(this.getPersonalization() == null) {
            return l;
        }
        for(Personalization p : this.getPersonalization()) {
            l.addAll(p.getTos());
        }
        return l;
    }

    /**
     * Add a to address. This is a convenience method for adding
     * an address to the a personalization object. To addresses 
     * are located in the Personalization object. This method adds 
     * the address to the first personalization, creating it if 
     * necessary. If you would like to add the address to a different
     * personalization object, please do so directly.
     * @param to the to address
     * @return this object.
     */
    public Mail to(Email to) {
        Personalization p;

        if(this.personalization == null) {
            this.personalization = new ArrayList<>();
        }

        if(this.personalization.size() == 0) {
            p = new Personalization();
            this.personalization.add(p);
        } else {
            p = this.personalization.get(0);
        }

        p.to(to);
        return this;
    }

    /**
     * Get the email's subject line.
     * @return the email's subject line.
     */
    @JsonProperty("subject")
    public String getSubject() {
        return subject;
    }

    /**
     * Set the email's subject line. This is the global, or 
     * “message level”, subject of your email. This may 
     * be overridden by personalizations[x].subject. 
     * @param subject the email's subject line.
     * @return this object.
     */
    public Mail subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Get the email's unsubscribe handling object (ASM).
     * @return the email's ASM.
     */
    @JsonProperty("asm")
    public ASM getASM() {
        return asm;
    }

    /**
     * Set the email's unsubscribe handling object (ASM).
     * @param asm the email's ASM.
     * @return this object.
     */
    public Mail asm(ASM asm) {
        this.asm = asm;
        return this;
    }

    /**
     * Get the email's personalizations. Content added to the returned
     * list will be included when sent.
     * @return the email's personalizations.
     */
    @JsonProperty("personalizations")
    public List<Personalization> getPersonalization() {
        return personalization;
    }

    /**
     * Add a personalization to the email. Each object within 
     * personalizations can be thought of as an envelope 
     * - it defines who should receive an individual message 
     * and how that message should be handled. 
     * @param personalization a personalization.
     * @return this object.
     */
    public Mail personalization(Personalization personalization) {
        if (this.personalization == null) {
            this.personalization = new ArrayList<>();
            this.personalization.add(personalization);
        } else {
            this.personalization.add(personalization);
        }
        return this;
    }

    /**
     * Get the email's content. Content added to the returned list
     * will be included when sent.
     * @return the email's content.
     */
    @JsonProperty("content")
    public List<Content> getContent() {
        return content;
    }

    /**
     * Add content to this email.
     * @param content content to add to this email.
     * @return this object.
     */
    public Mail content(Content content) {
        Content newContent = new Content()
            .type(content.getType())
            .value(content.getValue());

        if (this.content == null) {
            this.content = new ArrayList<Content>();
            this.content.add(newContent);
        } else {
            this.content.add(newContent);
        }
        return this;
    }

    /**
     * Get the email's attachments. Attachments added to the returned
     * list will be included when sent.
     * @return the email's attachments.
     */
    @JsonProperty("attachments")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Add attachments to the email.
     * @param attachment attachment to add.
     * @return this object.
     */
    public Mail attachment(Attachment attachment) {
        Attachment newAttachment = new Attachment()
            .content(attachment.getContent())
            .type(attachment.getType())
            .filename(attachment.getFilename())
            .disposition(attachment.getDisposition())
            .contentId(attachment.getContentId());

        if (this.attachments == null) {
            this.attachments = new ArrayList<Attachment>();
            this.attachments.add(newAttachment);
        } else {
            this.attachments.add(newAttachment);
        }
        return this;
    }

    /**
     * Get the email's template ID.
     * @return the email's template ID.
     */
    @JsonProperty("template_id")
    public String getTemplateId() {
        return this.templateId;
    }

    /**
     * Set the email's template ID.
     * @param templateId the email's template ID.
     * @return this object.
     */
    public Mail templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    /**
     * Get the email's sections. Sections added to the returned list
     * will be included when sent.
     * @return the email's sections.
     */
    @JsonProperty("sections")
    public Map<String, String> getSections() {
        return sections;
    }

    /**
     * Add a section to the email. A section is an object of key/value 
     * pairs that define block sections of code to be used as substitutions. 
     * @param key the section's key.
     * @param value the section's value.
     * @return this object.
     */
    public Mail section(String key, String value) {
        if (sections == null) {
            sections = new HashMap<String, String>();
            sections.put(key, value);
        } else {
            sections.put(key, value);
        }
        return this;
    }

    /**
     * Get the email's headers. Headers added to the returned list
     * will be included when sent.
     * @return the email's headers.
     */
    @JsonProperty("headers")
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Add a header to the email.
     * @param key the header's key.
     * @param value the header's value.
     * @return this object.
     */
    public Mail header(String key, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
            headers.put(key, value);
        } else {
            headers.put(key, value);
        }
        return this;
    }

    /**
     * Get the email's categories. Categories added to the returned list
     * will be included when sent.
     * @return the email's categories.
     */
    @JsonProperty("categories")
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Add a category to the email.
     * @param category the category.
     * @return this object.
     */
    public Mail category(String category) {
        if (categories == null) {
            categories = new ArrayList<String>();
            categories.add(category);
        } else {
            categories.add(category);
        }
        return this;
    }

    /**
     * Get the email's custom arguments. Custom arguments added to the returned list
     * will be included when sent.
     * @return the email's custom arguments.
     */
    @JsonProperty("custom_args")
    public Map<String, String> getCustomArgs() {
        return customArgs;
    }

    /**
     * Add a custom argument to the email. An email's custom args
     * are values that are specific to the entire send that will 
     * be carried along with the email and its activity data. 
     * Substitutions will not be made on custom arguments, so any 
     * string that is entered into this parameter will be assumed 
     * to be the custom argument that you would like to be used. 
     * This parameter is overridden by personalizations[x].custom_args 
     * if that parameter has been defined. Total custom args size 
     * may not exceed 10,000 bytes.
     * @param key argument's key.
     * @param value the argument's value.
     * @return this object.
     */
    public Mail customArg(String key, String value) {
        if (customArgs == null) {
            customArgs = new HashMap<String, String>();
            customArgs.put(key, value);
        } else {
            customArgs.put(key, value);
        }
        return this;
    }

    /**
     * Get the email's send at time (Unix timestamp).
     * @return the email's send at time.
     */
    @JsonProperty("send_at")
    public long sendAt() {
        return sendAt;
    }

    /**
     * Set the email's send at time.
     * A unix timestamp allowing you to specify when you want 
     * your email to be delivered. This may be overridden by 
     * the personalizations[x].send_at parameter. Scheduling 
     * more than 72 hours in advance is forbidden. 
     * @param sendAt the send at time.
     * @return this object.
     */
    public Mail sendAt(long sendAt) {
        this.sendAt = sendAt;
        return this;
    }

    /**
     * Get the email's batch ID.
     * @return the batch ID.
     */
    @JsonProperty("batch_id")
    public String getBatchId() {
        return batchId;
    }

    /**
     * Set the email's batch ID.
     * This ID represents a batch of emails to be sent at the 
     * same time. Including a batch_id in your request allows 
     * you include this email in that batch, and also enables 
     * you to cancel or pause the delivery of that batch. For 
     * more information, see https://sendgrid.com/docs/API_Reference/Web_API_v3/cancel_schedule_send.
     * @param batchId the batch ID.
     * @return this object.
     */
    public Mail batchId(String batchId) {
        this.batchId = batchId;
        return this;
    }

    /**
     * Get the email's IP pool ID.
     * @return the IP pool ID.
     */
    @JsonProperty("ip_pool_name")
    public String getIpPoolId() {
        return ipPoolId;
    }

    /**
     * Set the email's IP pool ID.
     * @param ipPoolId the IP pool ID.
     * @return this object.
     */
    public Mail ipPoolId(String ipPoolId) {
        this.ipPoolId = ipPoolId;
        return this;
    }

    /**
     * Get the email's settings.
     * @return the settings.
     */
    @JsonProperty("mail_settings")
    public MailSettings getMailSettings() {
        return mailSettings;
    }

    /**
     * Set the email's settings.
     * @param mailSettings the settings.
     * @return this object.
     */
    public Mail mailSettings(MailSettings mailSettings) {
        this.mailSettings = mailSettings;
        return this;
    }

    /**
     * Get the email's tracking settings.
     * @return the tracking settings.
     */
    @JsonProperty("tracking_settings")
    public TrackingSettings getTrackingSettings() {
        return trackingSettings;
    }

    /**
     * Set the email's tracking settings.
     * @param trackingSettings the tracking settings.
     * @return this object.
     */
    public Mail trackingSettings(TrackingSettings trackingSettings) {
        this.trackingSettings = trackingSettings;
        return this;
    }

    /**
     * Get the email's reply to address.
     * @return the reply to address.
     */
    @JsonProperty("reply_to")
    public Email getReplyto() {
        return replyTo;
    }

    /**
     * Set the email's reply to address.
     * @param replyTo the reply to address.
     * @return this object.
     */
    public Mail replyTo(Email replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    /**
     * Create a string represenation of the Mail object JSON.
     * @return a JSON string.
     * @throws IOException in case of a JSON marshal error.
     */
    protected String build() throws IOException {
        try {
            //ObjectMapper mapper = new ObjectMapper();
            return SORTED_MAPPER.writeValueAsString(this);
        } catch (IOException ex) {
            throw ex;
        }
    }

    /**
     * Create a string represenation of the Mail object JSON and pretty print it.
     * @return a pretty JSON string.
     * @throws IOException in case of a JSON marshal error.
     */
    public String buildPretty() throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (IOException ex) {
            throw ex;
        }
    }

    /**
     * Send the email.
     * @param sg the SendGrid instance to use.
     * @return the response object.
     * @throws IOException in case of a marshal, or network error.
     */
    public Response send(SendGrid sg) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(this.build());
        return sg.send(request);
    }
}
