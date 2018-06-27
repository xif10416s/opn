# -*- coding: utf-8 -*-
import codecs
import logging
import os
import re
import scrapy
import sys
import time
from scrapy.http import Request

reload(sys)
sys.setdefaultencoding('utf-8')

class LaoNianWangSpider(scrapy.Spider):
    name = "laonian"

    allowed_domains = []

    sourceId = "2"

    basePageSize= 1

    fromTime = time.strftime("%Y年%m月%d日 %H:%M", time.localtime(int(time.time()) - 60*60*24))

    baseUrl = "http://www.zhln.org"

    start_urls = ['http://www.zhln.org']
    def parse(self, response):
        # 健康
        urlTemps =['http://www.zhln.org/ys/wh/index_{n}.html'
            ,'http://www.zhln.org/ys/ys/index_{n}.html'
            ,'http://www.zhln.org/ys/qj/index_{n}.html'
            ,'http://www.zhln.org/ys/yy/index_{n}.html'
            ,'http://www.zhln.org/ys/yd/index_{n}.html'
            ,'http://www.zhln.org/ys/ss/index_{n}.html'
                   ]
        topic = '1'
        for i in range(0,len(urlTemps)):
            for j in range(1,self.basePageSize):
                url = urlTemps[i].replace("{n}", str(j))
                logging.debug(url)
                subTopic = 101 + i
                yield Request(url=url,callback=self.doHealthUrlParse,meta={"topic":topic,"subTopic":subTopic})
            pass
        pass
        # 常见病
        urlTemps =['http://www.zhln.org/jb/zf/index_{n}.html'
            ,'http://www.zhln.org/jb/ngs/index_{n}.html'
            ,'http://www.zhln.org/jb/zqgy/index_{n}.html'
            ,'http://www.zhln.org/jb/nlgs/index_{n}.html'
            ,'http://www.zhln.org/jb/sgnsj/index_{n}.html'
            ,'http://www.zhln.org/jb/bm/index_{n}.html'
            ,'http://www.zhln.org/jb/bnz/index_{n}.html'
            ,'http://www.zhln.org/jb/gzss/index_{n}.html'
            ,'http://www.zhln.org/jb/gxb/index_{n}.html'
            ,'http://www.zhln.org/jb/tnb/index_{n}.html'
            ,'http://www.zhln.org/jb/tf/index_{n}.html'
            ,'http://www.zhln.org/jb/fs/index_{n}.html']
        topic = '3'
        for i in range(0,len(urlTemps)):
            for j in range(1,self.basePageSize):
                url = urlTemps[i].replace("{n}", str(j))
                subTopic = 302 + i
                yield Request(url=url,callback=self.doHealthUrlParse,meta={"topic":topic,"subTopic":subTopic})
            pass
        pass

    logging.debug("--STEP end---------")

    def doHealthUrlParse(self, response):
        logging.debug("--STEP 2----topic--url-----")
        lineNews = response.xpath('//div[@class="zw"]/ul/li')
        linkNum = len(lineNews)
        for j in range(0,linkNum):
            newsLink = lineNews[j]
            linkUrl =self.baseUrl + newsLink.css('a::attr(href)').extract_first()
            day = newsLink.xpath('./span/text()').extract_first()
            logging.debug(day)
            logging.debug(linkUrl)
            yield Request(url=linkUrl,  callback=self.doTopicNewsParse,meta={"day":day ,"url":linkUrl,"topic":response.meta['topic'],"subTopic":response.meta['subTopic']})#,meta={"day":day ,"url":linkUrl}
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
        title = response.xpath('//div[@class="zw"]/h1/text()').extract_first()
        dateTime = response.xpath('//div[@class="zw"]/div/text()').extract_first()
        dateTime = re.search(r"(\d{4}-\d{1,2}-\d{1,2})",dateTime).group(0)
        dateTime = dateTime[0:4]+'年'+dateTime[5:7]+'月'+dateTime[8:10]+"日 00:00"
        logging.debug("date : " + dateTime)
        if  dateTime < self.fromTime :
            return
        author = '老年网'
        logging.debug(str(title) + " " + str(author) )
        contentList =  response.xpath('//div[@class="zw"]/p/text() | //div[@class="zw"]/p/strong/text()')
        content =[]
        if len(contentList) > 0 :
            for j in range(0,len(contentList)):
                logging.debug("----"+str(j))
                pargraph = contentList[j].extract()
                content.append(pargraph)
        # logging.debug("\n".join(content))
        needExclude = False
        for key in excludeKeyWords:
            if key in content:
                needExclude = True
                break
        if not needExclude and len(content) > 0:
            writer = codecs.open( filePath  +"/"+title+".txt", "a", "utf-8")
            writer.write(self.sourceId+":"+topic +":" + response.meta['subTopic'] + "\n")
            writer.write(response.meta['url'] + "\n")
            writer.write(dateTime + "\n")
            writer.write(author + "\n")
            writer.write("\n".join(content) + "\n")
            writer.close()
        else:
            logging.debug("------"+response.meta['url'] )





