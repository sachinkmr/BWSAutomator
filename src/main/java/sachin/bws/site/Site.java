/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachin.bws.site;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import sachin.bws.helpers.HelperClass;
import sachin.bws.selenium.WebDriverBuilder;


/**
 *
 * @author sku202
 */
public abstract class Site {

	// private Featurable feature;
	protected String url, comments;
	protected String username = "", password = "", urlWithAuth, statusMsg, host, startTime;
	protected String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
	protected boolean authenticate = false, running,crawling;
	protected int statusCode, viewPortHeight, viewPortWidth;
	protected int timeout;
	protected String siteHTML;
	protected WebDriver driver;
	protected WebDriverBuilder builder;
	protected String brandName = "", culture = "", environment = "";
	private boolean doCrawling = true;
    protected JSONObject siteJSON;

	public abstract String setUrlWithAuth(String add);

	public int getViewPortHeight() {
		return viewPortHeight;
	}

	public int getViewPortWidth() {
		return viewPortWidth;
	}

	public WebDriverBuilder getWebDriverBuilder() {
		return builder;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setWebDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setWebDriverBuilder(WebDriverBuilder builder) {
		this.builder = builder;
	}

	protected Site(String url) {
		this.url = url;
	}

	/**
	 * to know if site is MOS or BWS-R or BWS-NR
	 *
	 * @return branch version as string
	 */
	public String getSiteType() {
		try {
			if (!this.isRunning()) {
				return "Site is not running";
			}
			Document doc = Jsoup.parse(getSiteHTML());
			if (doc.body().attr("id").equalsIgnoreCase("homepage") && doc.toString().contains("ym-wrapper")
					&& doc.select("div.ym-wrapper") != null && doc.select("div.ym-wrapper").size() > 0) {
				return "MOS site";
			}
			if (doc.body().attr("id").equalsIgnoreCase("homepage") && doc.toString().contains("fps-main-wrapper")
					&& doc.select("div.fps-main-wrapper") != null && doc.select("div.fps-main-wrapper").size() > 0) {
				return "BWS Feature Phone Site";
			}
			Elements elements = doc.getElementsByTag("meta");
			for (Element e : elements) {
				String name = e.attr("name");
				if (name.equalsIgnoreCase("viewport") && getSiteHTML().contains("<!DOCTYPE html>")
						&& doc.body().hasAttr("data-layout") && doc.select("div.bws-globalWrapper") != null
						&& doc.select("div.bws-globalWrapper").size() > 0) {
					return "BWS Responsive Site";
				}
			}
			if (doc.body().hasClass("HomePage") && doc.body().attr("id").trim().equalsIgnoreCase("homepage")
					&& doc.select("div.page") != null && doc.select("div.page").size() > 0) {
				return "BWS non-responsive site";
			}
		} catch (Exception ex) {
			Logger.getLogger(Site.class.getName()).log(Level.SEVERE, null, ex);
		}

		return "Non BWS Site";
	}

	public abstract boolean hasAuthentication();

	public abstract String getHost();

	public abstract String getUrl();

	public abstract String getUsername();

	public abstract String getUrlWithAuth();

	public abstract String getUserAgent();

	/**
	 * to get site running status
	 *
	 * @return true if site is running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * method to get status message of the site response
	 *
	 * @return status message of site as string
	 */
	public abstract String getStatusMsg();

	/**
	 * method to get status code of the site response
	 *
	 * @return status code of site as integer
	 */
	public abstract int getStatusCode();

	/**
	 *
	 *
	 * @return if true, site is responsive site
	 */
	public boolean isResponsive() {
		return this.getSiteType().equalsIgnoreCase("BWS Responsive Site");
	}

	/**
	 *
	 *
	 * @return if true, site is MOS site
	 */
	public boolean isMOS() {
		return this.getSiteType().equalsIgnoreCase("MOS site");
	}

	/**
	 *
	 *
	 * @return if true, site is non-responsive site
	 */
	public boolean isNonResponsive() {
		return this.getSiteType().equalsIgnoreCase("BWS non-responsive site");
	}

	public abstract String getSiteHTML();

	public abstract String getPassword();

	public int getTimeout() {
		return timeout;
	}
	 /**
     * method to get all links of the site after crawling
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getAllCrawledLinks() {
        List<UrlLink> list = null;
        if (!(new File(HelperClass.getCrawledDataRepository(this.getHost()), HelperClass.getCrawledDataFilename(this.getHost())).exists())) {
            crawling = true;
        }

        if (isCrawling() && doCrawling) {
            if (!this.isRunning()) {
                return Collections.synchronizedList(new ArrayList<>());
            }
            list = new Crawling(this).getUrlLinks();
            HelperClass.saveCrawlingData(list, this.getHost());
            doCrawling = false;
        } else {
            list = HelperClass.readCrawlingData(this.getHost());
        }
        return list;
    }




    /**
     * method to get all links of the site after crawling which gives 200 as
     * status code
     *
     * @return List of UrlLink of the site which gives 200 as status code
     */
    public List<UrlLink> getCrawledLinks() {
        List<UrlLink> urlLinks = new ArrayList<UrlLink>();
        List<UrlLink> list = getAllCrawledLinks();
        for (UrlLink link : list) {
            if (link.getStatusCode() == 200) {
                urlLinks.add(link);
            }
        }
        return urlLinks;
    }

    /**
     * method to get all links of the site after crawling from a saved location
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getSavedCrawledLinksFromFile() {
        return HelperClass.readCrawlingData(HelperClass.getCrawledDataFilename(this.getHost()));
    }

    /**
     * method to get all product detail pages links of the site
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getProductDetailPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("productdetail")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all product pages links of the site
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getProductPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("product")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all article detail pages links of the site
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getArticlePages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("article")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all recipe detail pages links of the site
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getRecipePages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("recipe")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all misc pages links of the site
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getMiscPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (!link.getTemplateName().toLowerCase().contains("product") && !link.getTemplateName().toLowerCase().contains("article") && !link.getTemplateName().toLowerCase().contains("recipe")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all misc pages with their template name of the site
     *
     * @return JSON Object
     */
    public JSONObject getMiscTemplates() {
        Map<String, String> map = new HashMap<String, String>();
        List<UrlLink> links = getMiscPages();
        for (UrlLink link : links) {
            if (map.containsKey(link.getTemplateName())) {
                String urls = map.get(link.getTemplateName()) + "," + link.getUrl();
                map.put(link.getTemplateName(), urls);
            } else {
                map.put(link.getTemplateName(), link.getUrl());
            }
        }
        return new JSONObject(map);
    }

    /**
     * method to get all recipe pages with their template name of the site
     *
     * @return JSON Object
     */
    public JSONObject getRecipeTemplates() {
        Map<String, String> map = new HashMap<String, String>();
        List<UrlLink> links = getRecipePages();
        for (UrlLink link : links) {
            if (map.containsKey(link.getTemplateName())) {
                String urls = map.get(link.getTemplateName()) + "," + link.getUrl();
                map.put(link.getTemplateName(), urls);
            } else {
                map.put(link.getTemplateName(), link.getUrl());
            }
        }
        return new JSONObject(map);
    }

    /**
     * method to get all product pages with their template name of the site
     *
     * @return JSON Object
     */
    public JSONObject getProductTemplates() {
        Map<String, String> map = new HashMap<String, String>();
        List<UrlLink> links = getProductPages();
        for (UrlLink link : links) {
            if (map.containsKey(link.getTemplateName())) {
                String urls = map.get(link.getTemplateName()).concat("," + link.getUrl());
                map.put(link.getTemplateName(), urls);
            } else {
                map.put(link.getTemplateName(), link.getUrl());
            }
        }
        return new JSONObject(map);
    }

    /**
     * method to get all pages with their template name of the site
     *
     * @return JSON Object
     */
    public JSONObject getAllTemplates() {
        List<UrlLink> urlLinks = getCrawledLinks();
        Map<String, String> map = new HashMap<String, String>();
        for (UrlLink link : urlLinks) {
            if (map.containsKey(link.getTemplateName())) {
                String urls = map.get(link.getTemplateName()) + "," + link.getUrl();
                map.put(link.getTemplateName(), urls);
            } else {
                map.put(link.getTemplateName(), link.getUrl());
            }
        }
        return new JSONObject(map);
    }

    /**
     * method to get all article pages with their template name of the site
     *
     * @return JSON Object
     */
    public JSONObject getArticleTemplates() {
        Map<String, String> map = new HashMap<String, String>();
        List<UrlLink> links = getArticlePages();
        for (UrlLink link : links) {
            if (map.containsKey(link.getTemplateName())) {
                String urls = map.get(link.getTemplateName()) + "," + link.getUrl();
                map.put(link.getTemplateName(), urls);
            } else {
                map.put(link.getTemplateName(), link.getUrl());
            }
        }
        return new JSONObject(map);
    }

    /**
     * method to get all article detail pages.
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getArticleDetailPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("articledetail")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all Recipe detail pages.
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getRecipeDetailPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("recipedetail") || link.getTemplateName().toLowerCase().contains("recipe-detail")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all article category pages.
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getArticleCategoryPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("articlecategory")) {
                links.add(link);
            }
        }
        return links;
    }

    /**
     * method to get all product category pages.
     *
     * @return List of UrlLink of the site
     */
    public List<UrlLink> getProductCategoryPages() {
        List<UrlLink> urlLinks = getCrawledLinks();
        List<UrlLink> links = new ArrayList<UrlLink>();
        for (UrlLink link : urlLinks) {
            if (link.getTemplateName().toLowerCase().contains("productcategory")) {
                links.add(link);
            }
        }
        return links;
    }
    public abstract boolean isCrawling();
}
