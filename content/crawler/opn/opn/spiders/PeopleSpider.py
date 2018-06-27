# -*- coding: utf-8 -*-
import logging
import os
import scrapy
import sys
import time
from scrapy.http import Request
import codecs

reload(sys)
sys.setdefaultencoding('utf-8')

class PeopleSpider(scrapy.Spider):
    name = "people"

    allowed_domains = []

    sourceId = "1"

    baseUrl_junshi = "http://military.people.com.cn/"

    baseUrl_society = 'http://society.people.com.cn/'

    baseUrl_healty = 'http://health.people.com.cn/'

    start_urls = ['http://military.people.com.cn/']

    fromTime = time.strftime("%Y年%m月%d日 %H:%M", time.localtime(int(time.time()) - 60*60*24))

    pageSize = 2

    def parse(self, response):
        # # 军事
        urlTemp = 'http://military.people.com.cn/GB/367527/index{n}.html'
        topic = '0'
        logging.debug("--STEP 1----topic--url-----")
        for i in range(1,self.pageSize):
            url = urlTemp.replace("{n}", str(i))
            yield Request(url=url,callback=self.doTopicUrlParse,meta={"topic":topic,"subTopic":"1001"})
        pass
        # 社会
        urlTemp = 'http://society.people.com.cn/index{n}.html'
        topic = '2'
        for i in range(1,self.pageSize):
            url = urlTemp.replace("{n}", str(i))
            yield Request(url=url,callback=self.doSocietyUrlParse,meta={"topic":topic,"subTopic":"201"})
        pass
        # 健康
        urlTemps =['http://health.people.com.cn/GB/408572/index{n}.html','http://health.people.com.cn/GB/408580/index{n}.html']
        topic = '1'
        for i in range(0,len(urlTemps)):
            for j in range(1,self.pageSize):
                url = urlTemps[i].replace("{n}", str(j))
                subTopic = "101"
                if i == 1:
                    subTopic= "301"
                yield Request(url=url,callback=self.doHealthUrlParse,meta={"topic":topic,"subTopic":subTopic})
            pass
        pass




    def doTopicUrlParse(self, response):

        logging.debug("--STEP 2----topic--url-----")
        lineNews = response.xpath('//div[@class="ej_list_box clear"]/ul/li/a')
        linkNum = len(lineNews)
        for j in range(0,linkNum):
            newsLink = lineNews[j]
            linkUrl = newsLink.css('a::attr(href)').extract_first()
            if(not linkUrl.startswith('/n1')):
                continue
            day = linkUrl[4:13]
            logging.debug(day)
            linkUrl = (self.baseUrl_junshi +linkUrl)
            logging.debug(linkUrl)
            yield Request(url=linkUrl,callback=self.doTopicNewsParse,meta={"day":day ,"url":linkUrl,"topic":response.meta['topic'],"subTopic":response.meta['subTopic'] ,"baseUrl":self.baseUrl_junshi})#,meta={"day":day ,"url":linkUrl}
        pass


    def doSocietyUrlParse(self, response):
        logging.debug("--STEP 2----topic--url-----")

        lineNews = response.xpath('//div[@class="headingNews qiehuan1_c"]/div[@class="hdNews clearfix"]/div')
        linkNum = len(lineNews)
        for j in range(0,linkNum):
            newsLink = lineNews[j]
            linkUrl = newsLink.xpath('./a').css('a::attr(href)').extract_first()
            if(not linkUrl.startswith('/n1')):
                continue
            day = linkUrl[4:13]
            logging.debug(day)
            linkUrl = (self.baseUrl_society +linkUrl)
            logging.debug(linkUrl)
            yield Request(url=linkUrl,callback=self.doTopicNewsParse,meta={"day":day ,"url":linkUrl,"topic":response.meta['topic'],"subTopic":response.meta['subTopic'],"baseUrl":self.baseUrl_society})#,meta={"day":day ,"url":linkUrl}
        pass

    def doHealthUrlParse(self, response):
        logging.debug("--STEP 2----topic--url-----")

        lineNews = response.xpath('//div[@class="newsItems"]')
        linkNum = len(lineNews)
        for j in range(0,linkNum):
            newsLink = lineNews[j]
            linkUrl = newsLink.xpath('./a').css('a::attr(href)').extract_first()
            if(not linkUrl.startswith('/n1')):
                continue
            day = linkUrl[4:13]
            logging.debug(day)
            linkUrl = (self.baseUrl_healty +linkUrl)
            logging.debug(linkUrl)
            yield Request(url=linkUrl,callback=self.doTopicNewsParse2,meta={"day":day ,"url":linkUrl,"topic":response.meta['topic'],"subTopic":response.meta['subTopic'],"baseUrl":self.baseUrl_society})#,meta={"day":day ,"url":linkUrl}
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
        title = response.xpath('//div[@class="clearfix w1000_320 text_title"]/h1/text()').extract_first()
        dateTime = response.xpath('//div[@class="clearfix w1000_320 text_title"]/div[@class="box01"]/div/text()').extract_first()
        dateTime = dateTime[0:10]+' '+dateTime[11:16]
        if  dateTime < self.fromTime :
            return
        author = response.xpath('//div[@class="clearfix w1000_320 text_title"]/div[@class="box01"]/div/a/text()').extract_first()
        author = author if (author) else ""
        logging.debug(str(title) + " " + str(author) )
        contentList =  response.xpath('//div[@class="box_con"]/p')
        content =[]
        if len(contentList) > 0 :
            for j in range(0,len(contentList)):
                imgPargraph = contentList[j].xpath('img')
                if len(imgPargraph) > 0:
                    srcUrl = contentList[j].xpath('img/@src').extract_first()
                    srcUrl = response.meta['baseUrl'] + srcUrl
                    content.append(tmpImgDiv.replace("{srcUrl}",str(srcUrl)))
                else:
                    pargraph = '<p>' + contentList[j].xpath('./text()').extract_first().replace("\n","") + '</p>'
                    content.append(pargraph)
        logging.debug("\n".join(content))
        needExclude = False
        for key in excludeKeyWords:
            if key in content:
                needExclude = True
                break
        if not needExclude and len(content) > 0:
            writer = codecs.open( filePath  +"/"+title+".txt", "a", "utf-8")
            writer.write(self.sourceId+":"+topic +":"+response.meta['subTopic']+ "\n")
            writer.write(response.meta['url'] + "\n")
            writer.write(dateTime + "\n")
            writer.write(author + "\n")
            writer.write("\n".join(content) + "\n")
            writer.close()
        else:
            logging.debug("------"+response.meta['url'] )

    def doTopicNewsParse2(self, response):
        logging.debug("--STEP 3----news---")
        topic = response.meta['topic']
        day = str(time.strftime("%Y%m%d", time.localtime()))
        filePath = "data/"+day+"/"+self.name+"/"
        if(not os.path.exists(filePath)):
            os.makedirs(filePath)
        excludeKeyWords = [""]#未经本网或作者授权不得转载
        tmpImgDiv = '<div class="img-responsive"><img src="{srcUrl}" width="100%"></div>'
        day = response.meta['day']
        title = response.xpath('//div[@class="articleCont"]/div[@class="title"]/h2/text()').extract_first()
        dateTime = response.xpath('//div[@class="articleCont"]/div[@class="artOri"]/text()').extract_first()
        dateTime = dateTime[0:10]+' '+dateTime[11:16]
        if  dateTime < self.fromTime :
            return
        author = response.xpath('//div[@class="articleCont"]/div[@class="artOri"]/a/text()').extract_first()
        author = author if (author) else ""
        logging.debug(str(title) + " " + str(author) )
        contentList =  response.xpath('//div[@class="articleCont"]/div[@class="artDet"]/p')
        content =[]
        if len(contentList) > 0 :
            for j in range(0,len(contentList)):
                imgPargraph = contentList[j].xpath('img')
                if len(imgPargraph) > 0:
                    srcUrl = contentList[j].xpath('img/@src').extract_first()
                    srcUrl = response.meta['baseUrl'] + srcUrl
                    content.append(tmpImgDiv.replace("{srcUrl}",str(srcUrl)))
                else:
                    pargraph = '<p>' + contentList[j].xpath('./text()').extract_first().replace("\n","") + '</p>'
                    content.append(pargraph)
        logging.debug("\n".join(content))
        needExclude = False
        for key in excludeKeyWords:
            if key in content:
                needExclude = True
                break
        if not needExclude and len(content) > 0:
            writer = codecs.open( filePath  +"/"+title+".txt", "a", "utf-8")
            writer.write(self.sourceId+":"+topic +":"+response.meta['subTopic'] + "\n")
            writer.write(response.meta['url'] + "\n")
            writer.write(dateTime + "\n")
            writer.write(author + "\n")
            writer.write("\n".join(content) + "\n")
            writer.close()
        else:
            logging.debug("------"+response.meta['url'] )



