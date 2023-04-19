package com.house.everything_house_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.News;
import com.house.everything_house_backend.mapper.NewsMapper;
import com.house.everything_house_backend.service.INewsService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> implements INewsService {
    @Autowired
    private NewsMapper newsMapper;
    private static final String NEWS_URL = "https://news.sina.com.cn/china/";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<News> getNews() {
        List<News> newsList = new ArrayList<>();

        EdgeOptions options = new EdgeOptions();
        options.setCapability("ms:edgeChromium", true);  // 确保使用Chromium内核的Edge浏览器
        options.setCapability("ms:inPrivate", true);  // 可选：使用隐私模式
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");  // 添加无头参数
        System.setProperty("webdriver.edge.driver", "everything_house_backend/src/main/resources/edge/msedgedriver.exe");  // 设置EdgeDriver的路径

        EdgeDriver edgeDriver = new EdgeDriver(options);
        edgeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        edgeDriver.get(NEWS_URL);

        // 等待页面加载完成
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // Execute JavaScript to get the first 10 elements with the specified CSS selector
        String cssSelector = "div.feed-card-item";
        String script = "return Array.from(document.querySelectorAll(arguments[0])).slice(0, 10);";
        JavascriptExecutor jsExecutor = (JavascriptExecutor) edgeDriver;
        List<WebElement> firstTenNewsElements = (List<WebElement>) jsExecutor.executeScript(script, cssSelector);
        System.out.println(firstTenNewsElements.size());
        int i=0;
        for (WebElement newsElement : firstTenNewsElements) {
            //int id=i;
            String title = newsElement.findElement(By.cssSelector("h2 > a")).getText();
            String url = newsElement.findElement(By.cssSelector("h2 > a")).getAttribute("href");

            String imageUrl = newsElement.findElement(By.cssSelector("div.feed-card-img > a > img")).getAttribute("src");

            String summary = newsElement.findElement(By.cssSelector("div.feed-card-txt > a.feed-card-txt-summary")).getText();

            String timeStr = newsElement.findElement(By.cssSelector("div.feed-card-time")).getText();
            //System.out.println(title + url + summary + timeStr);
            Date time;
            try {
                time = dateFormat.parse(timeStr);
            } catch (ParseException e) {
                time = new Date();
            }

            News news = new News();
            //news.setId(id);
            news.setTitle(title);
            news.setUrl(url);
            news.setImgUrl(imageUrl);
            news.setContent(summary);
            news.setCreateTime(String.valueOf(time));
            newsList.add(news);
            i++;
            if(i>=10){
                break;
            }
        }

        edgeDriver.quit();

        return newsList;
    }
    public boolean deleteTopTen(){
        return newsMapper.deleteTopTen();
    }
    public boolean deleteAll(){
        return newsMapper.deleteAll();
    }
}
