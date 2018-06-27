#!/bin/bash

source /etc/profile
source $HOME/.bash_profile

cd /Users/seki/git/projects/opn/content/crawler/opn/opn
scrapy crawl laonian
echo "lao nian end....."
scrapy crawl people
echo "people end....."
scrapy crawl sina
echo "sina end....."

echo "app start....."
java -jar /Users/seki/git/projects/opn/content/target/opn-1.0-SNAPSHOT-jar-with-dependencies.jar
echo "app end....."