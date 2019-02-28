package com.wx.decrypt.volley_request;

import java.util.List;

/**
 * Created by xt on 2018/01/26.
 */

public class BookDetailInfo {
    private RateData rating;
    private List<String> author;
    private String pubdate;
    private List<TagData> tags;
    private String publisher;
    private String title;
    private String price;
    private String author_intro;
    private ImageData images;
    private String origin_title;
    private String image;
    private String binding;
    private List<String> translator;
    private String catalog;
    private String pages;
    private String subtitle;
    private String alt;
    private String id;
    private String isbn10;
    private String isbn13;
    private String url;
    private String alt_title;
    private String summary;

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public List<String> getTranslator() {
        return translator;
    }

    public void setTranslator(List<String> translator) {
        this.translator = translator;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ImageData getImages() {
        return images;
    }

    public void setImages(ImageData images) {
        this.images = images;
    }

    public RateData getRating() {
        return rating;
    }

    public void setRating(RateData rating) {
        this.rating = rating;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public List<TagData> getTags() {
        return tags;
    }

    public void setTags(List<TagData> tags) {
        this.tags = tags;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public class TagData {
        private long count;
        private String name;
        private String title;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class RateData {
        private int max;
        private long numRaters;
        private String average;
        private int min;

        public void setMax(int max) {
            this.max = max;
        }

        public void setNumRaters(long numRaters) {
            this.numRaters = numRaters;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public long getNumRaters() {
            return numRaters;
        }

        public int getMin() {
            return min;
        }

        public String getAverage() {
            return average;
        }
    }

    public class ImageData {
        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    //通过Id获得的值
    /*
    {"rating":{"max":10,"numRaters":148323,"average":"7.1","min":0},
    "subtitle":"",
    "author":["郭敬明"],
    "pubdate":"2003-11",
    "tags":[{"count":27249,"name":"郭敬明","title":"郭敬明"},
        {"count":17303,"name":"梦里花落知多少","title":"梦里花落知多少"},
        {"count":15426,"name":"小说","title":"小说"},
        {"count":14661,"name":"青春","title":"青春"},
        {"count":6972,"name":"80后","title":"80后"},
        {"count":6565,"name":"爱情","title":"爱情"},
        {"count":4092,"name":"小四","title":"小四"},
        {"count":4086,"name":"中国","title":"中国"}],
    "origin_title":"",
    "image":"https://img1.doubanio.com\/mpic\/s1513378.jpg",
    "binding":"平装",
    "translator":[],
    "catalog":"\n      ",
    "pages":"252",
    "images":{"small":"https://img1.doubanio.com\/spic\/s1513378.jpg",
             "large":"https://img1.doubanio.com\/lpic\/s1513378.jpg",
             "medium":"https://img1.doubanio.com\/mpic\/s1513378.jpg"},
    "alt":"https:\/\/book.douban.com\/subject\/1016300\/",
    "id":"1016300",
    "publisher":"春风文艺出版社",
    "isbn10":"7531325098",
    "isbn13":"9787531325093",
    "title":"梦里花落知多少",
    "url":"https:\/\/api.douban.com\/v2\/book\/1016300",
    "alt_title":"",
    "author_intro":"郭敬明（1983- ），网名：第四至未至》，最小说》、《N世界》等。",
    "summary":"《梦里花落知多少》是郭敬明出版第二部小说，此作一改门的大轻读者的共鸣。",
    "price":"20.00元"}
    */

    //通过ISBN查找得到的返回值
    /*
    {"rating":{"max":10,"numRaters":58,"average":"6.9","min":0},
        "subtitle":"系统卷",
            "author":["杨丰盛"],
        "pubdate":"2011-6",
        "tags":[{"count":150, "name":"android", "title":"android"},
              {"count":40,"name":"技术内幕","title":"技术内幕"},
              {"count":38,"name":"源码分析","title":"源码分析"},
              {"count":35,"name":"Android","title":"Android"},
              {"count":25,"name":"软件开发","title":"软件开发"},
              {"count":25,"name":"编程","title":"编程"},
              {"count":25,"name":"移动平台","title":"移动平台"},
              {"count":22,"name":"计算机科学","title":"计算机科学"}],
        "origin_title":"",
        "image":"https://img1.doubanio.com\/mpic\/s6379378.jpg",
        "binding":"平装",
        "translator":[],
        "catalog":"前言\n第1章  准备工作 \/1\,
        "ebook_url":"https:\/\/read.douban.com\/ebook\/15161014\/",
        "pages":"548",
        "images":{"small":"https://img1.doubanio.com\/spic\/s6379378.jpg",
                  "large":"https://img1.doubanio.com\/lpic\/s6379378.jpg",
                  "medium":"https://img1.doubanio.com\/mpic\/s6379378.jpg"},
        "alt":"https:\/\/book.douban.com\/subject\/6047744\/",
        "id":"6047744",
        "publisher":"机械工业出版社",
        "isbn10":"7111337271",
        "isbn13":"9787111337270",
        "title":"Android技术内幕",
        "url":"https:\/\/api.douban.com\/v2\/book\/6047744",
        "alt_title":"",
        "author_intro":"杨丰盛，国内Android领域的先，在机顶盒软证会国际认证。",
        "summary":"《Andrdroid系统从构架上依次分为应用层、核层等5个4章分析了And要内容；oid运行库的读者。",
        "ebook_price":"25.00",
        "price":"69.00元"}
        */


}
