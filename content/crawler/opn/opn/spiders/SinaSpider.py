# -*- coding: utf-8 -*-
import codecs
import logging
import os
import re
import scrapy
import sys
import time
from scrapy.http import Request
import time

reload(sys)
sys.setdefaultencoding('utf-8')

class SinaSpider(scrapy.Spider):
    name = "sina"

    allowed_domains = []

    sourceId = "0"

    basePageSize= 3

    fromTime = time.strftime("%Y年%m月%d日 %H:%M", time.localtime(int(time.time()) - 60*60*24))

    start_urls = ['http://www.sina.com.cn/']
    def parse(self, response):
        #军事
        urlTemp = 'http://roll.mil.news.sina.com.cn/col/zgjq/index_{n}.shtml'
        topic = '0'
        logging.debug("--STEP 1----topic--url-----")
        for i in range(1,self.basePageSize):
            url = urlTemp.replace("{n}", str(i))
            yield Request(url=url,callback=self.doTopicUrlParse,meta={"topic":topic,"subTopic":"1001"})
        pass
        # 健康
        healthBaseUrl = 'http://feed.mix.sina.com.cn/api/roll/get?pageid=39&lid=561&num=20&versionNumber=1.2.8&page={n}&encode=utf-8&callback=feedCardJsonpCallback&_=1529298525258'
        topic = '3'
        for i in range(0,self.basePageSize):
            url = healthBaseUrl.replace("{n}", str(i))
            yield Request(url=url
                          ,callback=self.doBodyParse,meta={"topic":topic,"subTopic":"301"})
        pass
        #社会
        socityBaseUrl = 'https://feed.sina.com.cn/api/roll/get?pageid=123&lid=1367&num=20&versionNumber=1.2.4&page={n}&encode=utf-8&callback=feedCardJsonpCallback&_=1529810301976'
        topic = '2'
        for i in range(0,self.basePageSize):
            url = socityBaseUrl.replace("{n}", str(i))
            logging.debug(url)
            yield Request(url=url
                          ,callback=self.doBodyParse,meta={"topic":topic,"subTopic":"201"})
        pass
        logging.debug("--STEP end---------")

    def doBodyParse(self,response):
        logging.debug("---doHealthUrlParse------step 1----")
        body=response.body.decode("utf-8","ignore")
        rr = re.compile(r'"url":"(.*?)"')
        urlList = rr.findall(body)
        for i in range(0,len(urlList)):
            linkUrl = urlList[i].replace("\\","")
            logging.debug("---doHealthUrlParse--" + linkUrl)
            end = linkUrl.rfind("/")
            day = linkUrl[(end -10): end]
            if linkUrl.startswith("http://news.sina.com.cn") or linkUrl.startswith("http://health.sina.com.cn/"):
                yield Request(url=linkUrl,callback=self.doTopicNewsParse,meta={"day":day ,"url":linkUrl,"topic":response.meta["topic"],"subTopic":response.meta["subTopic"]})#,meta={"day":day ,"url":linkUrl}
        pass

    def doTopicUrlParse(self, response):
        logging.debug("--STEP 2----topic--url-----")
        lineNews = response.xpath('//ul[@class="linkNews"]/li/a')
        linkNum = len(lineNews)
        for j in range(0,linkNum):
            newsLink = lineNews[j]
            linkUrl = newsLink.css('a::attr(href)').extract_first()
            end = linkUrl.rfind("/")
            day = linkUrl[(end -10): end]
            logging.debug(day)
            logging.debug(linkUrl)

            yield Request(url=linkUrl,callback=self.doTopicNewsParse,meta={"day":day ,"url":linkUrl,"topic":response.meta['topic'],"subTopic":response.meta["subTopic"]})#,meta={"day":day ,"url":linkUrl}
        pass

    def doTopicNewsParse(self, response):
        logging.debug("--STEP 3----news---")
        topic = response.meta['topic']
        day = str(time.strftime("%Y%m%d", time.localtime()))
        filePath = "data/"+day+"/"+self.name+"/"
        if(not os.path.exists(filePath)):
            os.makedirs(filePath)
        excludeKeyWords = [""]#未经本网或作者授权不得转载
        tmpImgDiv = '<div class="img-responsive"><img src="{srcUrl}" width="100%"></div>'
        day = response.meta['day']
        title = response.xpath('//h1[@class="main-title"]/text()').extract_first()
        dateTime = response.xpath('//div[@class="date-source"]/span[@class="date"]/text()').extract_first()
        logging.debug("dateTIme :" +dateTime)
        if  dateTime < self.fromTime :
            return
        author = response.xpath('//div[@class="date-source"]/a[@class="source"]').xpath('string(.)').extract_first()
        author2 = response.xpath('//div[@class="date-source"]/span[@class="source content-color"]').xpath('string(.)').extract_first()
        author = author if (author) else author2
        author = author if (author) else ""
        logging.debug(str(title) + " " + str(author) )
        contentList =  response.xpath('//div[@class="article"]/p | //div[@class="article"]/div[@class="img_wrapper"]')
        content =[]
        if len(contentList) > 0 :
            for j in range(0,len(contentList)):
                logging.debug("----"+str(j))
                pargraph = contentList[j].extract()
                if "img_wrapper" in pargraph:
                    srcUrl = contentList[j].xpath('img/@src').extract_first()
                    content.append(tmpImgDiv.replace("{srcUrl}",str(srcUrl)))
                else:
                    content.append(pargraph)
        # logging.debug("\n".join(content))
        needExclude = False
        for key in excludeKeyWords:
            if key in content:
                needExclude = True
                break
        if not needExclude and len(content) > 0:
            targetFile = filePath  +"/"+title+".txt"
            if os.path.exists(targetFile) :
                return
            writer = codecs.open( filePath  +"/"+title+".txt", "a", "utf-8")
            writer.write(self.sourceId+":"+topic+":"+ response.meta["subTopic"] + "\n")
            writer.write(response.meta['url'] + "\n")
            writer.write(dateTime + "\n")
            writer.write(author + "\n")
            writer.write("\n".join(content) + "\n")
            writer.close()
        else:
            logging.debug("------"+response.meta['url'] )





