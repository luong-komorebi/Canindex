## Disclaimer 

I pack all our work in 2 folder. One is working, one is not. We built a social network function for sharing dog photos in the latter, but it has bugs that we can't fix. It is there to show that we made a lot of effort although we did not succeed. 

The rest of this readme shows how to run a web server to make dog breeed recognization possible.

Also, we would like to apologize for not having host this somewhere (because we don't have credit card) and the recognization is not working at its best (because I couldn't train all the database with my local machine, I could only train for about 20 most popular dogs).

## How does dog breed recognition work

> Besides recognizing dog breed, the apps also has various functionalities, but I would not focus on them here.

### Concept 

The concept of recognizing dog breed is taken from the movie Sillicon Valley. They simply built an app that can recognize whether an object is a hotdog or not. All the technical aspect of the app is well writen and discussed here : https://medium.com/@timanglade/how-hbos-silicon-valley-built-not-hotdog-with-mobile-tensorflow-keras-react-native-ef03260747f3. 

In short, **a neural architecture trained with mobile TensorFlow, Keras & app built with React Native** is what they used. 

But it is too hard that I tried their approach and not succeed. Weeks later, I try to invent my own way, which is not an optimized, accurate way, but achievable at my level (I am a sophomore) : **we will be using transfer learning, which means we are starting with a model that has been already trained on another problem**. Or in details, we did use Tensorflow and **retrain Inception's final layer for new categories**, which is the dog.

### How we did this  

At the time we did this, I could not find any image dataset for dog breeds, so I decide to make them myself. 

1. Go to wikipedia and scrape the list of all dogs available on this world. I wrote a scraper for this : https://github.com/LewisVo/dog-breed-scraper
2. Use the list of dogs we just scraped, go to bing and scrape about 30 images for each dog. I also wrote a scraper for this https://github.com/LewisVo/dog-images-data-downloader. 
> I could have used Bing's official API but I have no money + credit card. I could also have used Google image API but they are very limited. Scraping from Google is also harder than Bing. 

3. Use Tensorflow's docker (https://hub.docker.com/r/tensorflow/tensorflow/) and link folder with dog images to it. Start training: 
```python
python tensorflow/examples/image_retraining/retrain.py \
  --bottleneck_dir=/tf_files/bottlenecks \
  --how_many_training_steps=500 \
  --model_dir=/tf_files/inception \
  --output_graph=/tf_files/retrained_graph.pb \
  --output_labels=/tf_files/retrained_labels.txt \
  --image_dir=/ # put your directory here
``` 
> Because there is not enough data, accuracy is low.

4. I write a backend server. My mobile app allows people to push image to the server, then the server process with pretrained models and return the dog breed they recognized : https://github.com/LewisVo/CanindexBackend

However, later (13/9/2017), there is one better dataset that contains images from 120 breeds of dogs from around the world by Standford: http://vision.stanford.edu/aditya86/ImageNetDogs/ . So dont use my way if you are gonna reproduce this. 

## How to run

### Prequisite 
This tutorial assumes that you are on Linux. If you are using another Operating System, you have to figure out the equivalent way.

1. Git  https://git-scm.com/
2. Docker https://www.docker.com/ 
3. NodeJS https://nodejs.org/en/ (Current version)
4. Ngrok https://ngrok.com/ 


### Steps

- Step 1: Pull my docker image : `docker pull luongvo/dog-classifying`.
- Step 2: Run image: `docker run -it -d luongvo/dog-classifying:latest`.
- Step 3: Find the ID of the container: `docker ps -a`. Like the one we found here is d17fe61fe4ea  ![](http://i.imgur.com/vLG5Lim.png) 
- Step 4: Clone the backend. `git clone https://github.com/LewisVo/CanindexBackend`. Change to its directory `cd CanindexBackend`. Open detect.js file `touch detect.js`. Replace the old ID `d1e9db568fb6` with the ID you just found. ![](http://i.imgur.com/G0eLJuL.png)
- Step 5: Expose your local 3030 port with ngrok `ngrok http 3030`. Then get the link out ![](http://i.imgur.com/BWQ5nKY.png).
- Step 6: Jump into android source code, file `app/src/main/res/values/strings.xml` and change `<string name="server_url">` to the newly acquired link. 
- Step 7: Run the backend. `node app.js`
- Step 8: run the android app and enjoy.







