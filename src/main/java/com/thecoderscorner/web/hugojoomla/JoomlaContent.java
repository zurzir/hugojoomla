package com.thecoderscorner.web.hugojoomla;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class JoomlaContent {
    private final static Logger LOGGER = LoggerFactory.getLogger(JoomlaContent.class);
    private final static Pattern HYPERLINK_PATTERN = Pattern.compile("\\<a[^\\>]*\\>([^\\<]*)\\<\\/a\\>");
    private final static Pattern CODE_BLOCK_PATTERN = Pattern
            .compile("(\\<pre\\>[^\\<]*\\<code\\>)|(\\</code\\>[^\\<]*\\</pre\\>)");

    private final static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final int id;
    private final String author;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;
    private final LocalDateTime publishUp;
    private final String intro;
    private final String body;
    private final String category;
    private final String title;
    private final String metadesc;
    private final String alias;
    private final int status;
    private final String parent;
    private final String createdByAlias;
    private JoomlaImage introImage = JoomlaImage.EMPTY;
    private JoomlaImage bodyImage = JoomlaImage.EMPTY;

    public JoomlaContent(int id, int status, String author, LocalDateTime createdDate, LocalDateTime modifiedDate,
    LocalDateTime publishUp,
            String intro,
            String body, String category, String title, String metadesc, String alias,
            String images, String parent, String createdByAlias) {
        this.id = id;
        this.status = status;
        this.author = author;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.publishUp = publishUp;
        this.intro = intro;
        this.body = body;
        this.category = category;
        this.title = title;
        this.metadesc = metadesc;
        this.alias = alias;
        this.parent = parent;
        this.createdByAlias = createdByAlias;
        try {
            introImage = new JoomlaImage("intro", images);
            bodyImage = new JoomlaImage("fulltext", images);
        } catch (ParseException e) {
            LOGGER.warn("Images for " + id + " not processed. Error from JSON Parser was " + e.getLocalizedMessage());
        }
    }

    public String getPublishUpAsText() {
        return publishUp != null ? formatter.format(publishUp) : "";
    }

    public String getCreatedByAlias() {
        return createdByAlias;
    }

    public String getAlias() {
        return alias;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author != null ? author : "";
    }

    public String getCreatedDateAsText() {
        return formatter.format(createdDate);
    }

    public String getModifiedDateAsText() {
        return formatter.format(modifiedDate);
    }

    public String getIntro() {
        return intro;
    }

    public String getIntroAsSingleLine() {
        return escapeIt(intro);
    }

    public String escapeIt(String s) {
        s = s.replaceAll("[\\n\\r]*", "");
        s = s.replace("\"", "\\\"");
        return escapeAllCode(removeAllHyperlinks(s.replace("\'", "\\\"")));
    }

    public String escapeAllCode(String s) {
        return CODE_BLOCK_PATTERN.matcher(s).replaceAll("\n```\n");
    }

    public String removeAllHyperlinks(String s) {
        return HYPERLINK_PATTERN.matcher(s).replaceAll("\\1");
    }

    public String getBody() {
        return escapeAllCode(body);
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return escapeIt(title);
    }

    public String getMetadesc() {
        return escapeIt(metadesc);
    }

    public JoomlaImage getIntroImage() {
        return introImage;
    }

    public JoomlaImage getBodyImage() {
        return bodyImage;
    }

    public boolean isPublished() {
        return status == 0 || status == 1;
    }

    public String getParent() {
        return parent;
    }
}
