# ApiPayMyTable
## _A Nice Url Shortener API_

![Spring Boot Icon](https://www.dariawan.com/media/images/tutorial-spring-logo.width-1024.png)


This API enables us to persist and use some shortened Urls. I used Spring Boot, Spring Data, MongoDB and MongoDB Atlas. See below for my choice justifications

## Features

- POST /short_url allows to create and persist a short URL
- GET /short_url/:short_url_id allows you to retrieve a short URL
- PUT /short_url/:short_url_id allows to modify a short URL (i.e. to be able to replace the old Url while keeping the Short Url)
- DELETE /short_url/:short_url_id allows you to delete a short URL

For MongoDB Atlas I would have prefered to use Redis but in this case it was much more efficient to setup a Mongo Database. In the case of Redis I would have setup a Linux server with Redis on.

For Spring Data, it wasn't my first choice. I wanted to use Morphia but due to an issue (https://github.com/MorphiaOrg/morphia/issues/1876 Conveniently opened the day before I started working on this project) I couldn't work with it. So after some time troubleshooting I went with Spring Data.

Another technical choice was the generation of the ShortUrl. I decided to go with a 5 character long Alphanumerical ID (36^5 possibilities) because it is a small project and even just this much possibilities minimizes the risk of duplication.

At first I wanted to make the URL the primary key of the ShortUrl Object but because we want to be able to change the Url of a ShortUrl object I went with a classic id for each Objects.

I also made sure that the URL we are given are working. But there is a limitation if the URL is accessible from a certain network because the server where the API is can't access this URL.

I also wanted to make sure we don't have dupplication in the URLs linked to shortUrls so if you want to create an already shortened URL you will get the previously generated ShortUrl.

I had trouble with the Complete ShortUrl Link. I thought it was important that the full link would be present in the response, but I didn't want to store it in the database for obvious migration reasons. So the best solution after experimenting with several solutions offered by Spring was to modify the ShortUrl just before sending it to adapt it to the right format. It was difficult finding a way of having a Dynamic Ip. So i think the best way would be to use Proven and make the ip variable change in the differents environnements.

Architecture :
I used a classic Model View Controller to separate the different component and services.

Possible improvements : 
 - The management of errors, at this time when a ShortUrl Object is missing in the database we only send a 404 error. But when we also send a 404 error if the URL used is not working. In the case where we modify a ShortUrl we don't know if there is a problem with the URL or with the ShortUrl Object.

- The way I am currently sending back the complete ShortUrl may not be the optimal way. It may be interesting to look into it.

- MongoDB also try to connect to localhost everytime we start the API but fail because there is no localhost database. It does not affect the rest of the application.

- ~~The way deletion and id are managed can cause new ShortUrl to Delete others (Id is based off the number of items in database, if we have item 0 and 1 and we delete item 0 the next time we create an item it will have id 1 and thus delete previous item 1)~~
