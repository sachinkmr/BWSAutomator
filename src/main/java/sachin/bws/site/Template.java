/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachin.bws.site;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author sku202
 */
public class Template {

    private List<String> templates;
    private static Template template;

    public static Template getInstance() {
        if (template == null) {
            template = new Template();
        }
        return template;
    }

    private Template() {
        try {
            templates = FileUtils.readLines(new File("Resources", "templates.txt"),"UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTemplate(Document doc) {
        String bodyClass[] = doc.body().attr("class").split("\\u0020");
        List<String> classes = Arrays.asList(bodyClass);
        Collections.reverse(classes);
        for (String t : classes) {
            if (templates.contains(t)) {
                return t;
            }
        }
        return "****";
    }

    public String getTemplate(String pageSource) {
        Document doc = Jsoup.parse(pageSource);
        String bodyClass[] = doc.body().attr("class").split("\\u0020");
        List<String> classes = Arrays.asList(bodyClass);
        Collections.reverse(classes);
        for (String t : classes) {
            if (templates.contains(t)) {
                return t;
            }
        }
        return "****";
    }

    public String getTemplate(URL url) {
        String url1 = url.toExternalForm();
        try {
            Document doc = Jsoup.connect(url1).ignoreHttpErrors(true).ignoreContentType(true).timeout(100000).followRedirects(true).get();
            return getTemplate(doc);
        } catch (IOException ex) {
            Logger.getLogger(Template.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "****";
    }

    public List<String> getTemplates() {
        return templates;
    }
}
