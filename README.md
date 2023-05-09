# BlindToy-Project-App

This is a Java app to connect with a Microcontroller via Bluetooth. It's aimed to be part of a "Toy" for blind dogs who need audio-signals to find their toy for fetching. 
I developed this app, the php backend and the microcontroller-toy during my programming-training at Codersbay Linz. 

# Features:
* Registration / Login (see also BlindToy-Project-PHP)
* Internal user-area: 
  - Adding Pets
  - Editing Sound Settings
  - Bluetooth-Connection to microcontroller 
  - Controlling the Sounds of the microcontroller


# Work in progress:
* At the moment, the Bluetooth-Connection is still handled in the main thread which causes the UI to block (BtConnectionService class)
* the api token is for now sent in the body instead of the header (ServerAPI class)
* for the buttons MP3_1 and MP3_2 no OnclickListeners are set so far (PlayingFragment)
