/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachin.bws.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import sachin.bws.site.UrlLink;


/**
 *
 * @author sku202
 */
public class HelperClass {


    /**
     * Method to read results.
     *
     * @param host brand name of the BWS file
     * @return JSON data as JSON object
     */
    public synchronized static JSONObject readAllResults(String host) {
        File file = new File(HelperClass.getCrawledDataRepository(host), "AllResults.json");
        return readJsonFromFile(file.getAbsolutePath());
    }

    /**
     * Method to read JSON from a file.
     *
     * @param file JSON file path(Absolute path)
     * @return JSON data in file
     */
    public synchronized static JSONObject readJsonFromFile(String file) {
        String data = null;
        JSONObject json = null;
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            data = IOUtils.toString(reader);
            json = new JSONObject(data);
            reader.close();
        } catch (Exception ex) {
        	Logger.getLogger(HelperClass.class.getName()).log(Level.WARN, null, ex);
        }
        return json;
    }

    /**
     * Method to get latest modified siteConfig JSON file.
     *
     * @param siteURL Address of site
     * @return JSON of site config for a site as File object
     */
    public static File getLatestSiteConfigFile(String siteURL) {
        File file = new File("Data" + File.separator + HelperClass.getModifiedHostName(siteURL));
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file, "siteConfig.json");
        return file;
    }


    /**
     * To save crawled data to a JSON file for a site. Site host name is
     * provided as input
     *
     * @param links List of UrlLink of the site after crawling
     * @param host name of host of the site.
     */
    public static synchronized void saveCrawlingData(List<UrlLink> links, String host) {
        host=HelperClass.getCrawledDataFilename(host);
        try {
            File f = new File(getCrawledDataRepository(host), host);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(links);
                oos.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HelperClass.class.getName()).log(Level.WARN, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HelperClass.class.getName()).log(Level.WARN, null, ex);
        }
    }

    /**
     * Method is used to read crawled data from a JSON file for a site. Site
     * host name is provided as input
     *
     * @param host name of host of the site.
     * @return list of UrlLinks from JSON file of host
     */
    @SuppressWarnings("unchecked")
	public synchronized static List<UrlLink> readCrawlingData(String host) {
        List<UrlLink> links = null;
        host=HelperClass.getCrawledDataFilename(host);
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(HelperClass.getCrawledDataRepository(host), host)))) {
                links = (List<UrlLink>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(HelperClass.class.getName()).log(Level.WARN, null, ex);
        }
        return links;
    }


	/**
	 * Method to get modified host name. return host does not contain .com, www.
	 * and http://, or https://
	 *
	 * @param url
	 *            site address as string
	 * @return String of host name
	 */
	public static String getModifiedHostName(String url) {
		try {
			url = new URL(url).getHost().toLowerCase();
		} catch (MalformedURLException ex) {
			Logger.getLogger(HelperClass.class.getName()).log(Level.WARN, null, ex);
		}
		url = url.contains("www.") ? url.replaceAll("www.", "") : url;
		url = url.contains(".com") ? url.replaceAll(".com", "") : url;
		return url;
	}

	/**
	 * Method to get modified host name. return host does not contain .com, www.
	 * and http://, or https://
	 *
	 * @param address
	 *            URL object of site address
	 * @return String of host name
	 */
	public static String getModifiedHostName(URL address) {
		String url = address.getHost().toLowerCase();
		url = url.contains("www.") ? url.replaceAll("www.", "") : url;
		url = url.contains(".com") ? url.replaceAll(".com", "") : url;
		return url;
	}

	/**
	 * Method returns the unique name for png image based on time stamp
	 *
	 *
	 * @return name of the unique png image
	 */
	public static String generateUniqueName() {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		DateFormat df1 = new SimpleDateFormat("hh-mm-ss-SSaa");
		Calendar calobj = Calendar.getInstance();
		String time = df1.format(calobj.getTime());
		String date = df.format(calobj.getTime());
		return date + " " + time + ".png";
	}

	/**
	 * Method returns the unique string based on time stamp
	 *
	 *
	 * @return unique string
	 */
	public static String generateUniqueString() {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		DateFormat df1 = new SimpleDateFormat("hh-mm-ss-SSaa");
		Calendar calobj = Calendar.getInstance();
		String time = df1.format(calobj.getTime());
		String date = df.format(calobj.getTime());
		return date + " " + time;
	}

	/**
	 * Method returns the file name of the screen shot of the site
	 *
	 * @param host
	 *            host name of the site
	 * @return name of the crawler data file for a site
	 */
	public static String getCrawledDataFilename(String host) {
		return host.contains(".com") ? host.substring(0, host.indexOf(".com")) : host;
	}

	/**
	 * Method returns the directory of the saved crawled data for a site
	 *
	 * @param host
	 *            host name of the site
	 * @return Absolute path of the crawler data directory for a site
	 */
	public static String getCrawledDataRepository(String host) {
		host = HelperClass.getCrawledDataFilename(host);
		File f = new File(HelperClass.getAppPath() + File.separator + "Data" + File.separator + "crawledData"
				+ File.separator + host);
		f.mkdirs();
		return f.getAbsolutePath();
	}

	/**
	 * Method is used to get absolute path of app
	 *
	 *
	 * @return path as string
	 */
	public static String getAppPath() {
		return System.getProperty("user.dir");
	}

	/**
	 *
	 * This methods returns the modified mobile site name. it replaces 'm.' with
	 * 'm-'
	 *
	 * @param site
	 * @return site name as String
	 */
	public static String getModifiedMobileSiteName(String site) {
		try {
			site = new URL(site).getHost();
		} catch (MalformedURLException ex) {
			Logger.getLogger(HelperClass.class.getName()).log(Level.WARN, null, ex);
		}
		site = site.contains("m.") ? site.replace("m.", "m-") : site;
		site = site.substring(0, site.indexOf("."));
		return site;
	}




}
