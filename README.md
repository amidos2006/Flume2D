Flume2D
========================

This engine is available for anyone to use in their game. The code is in Java and uses AWT for rendering sprites. It is influenced from flash game engines like FlashPunk and Flixel.

Currently in Alpha
------------------------
Flume is currently in an alpha state. This means that there are likely to be bugs and the code I push will probably break past versions. With that said if you still would like to help develop or use the engine feel free to do so and post bug reports.

Features
------------------------
* Image, Spritemap, Tilemap, Backdrop, Text, BitmapText and Canvas rendering
	* Rotated image calculation
	* Layered graphic lists
* Input handling with key-mapping
* World Management/Sorting
	* Layer based rendering (z-index)
	* Type based collision handling (ex. player VS enemy)
* SAT collision
	* Similar to FlashPunk masks
	* AABB, Circle, Grid and Polygon collision
* Basic UDP Networking
* Tweens similar to FlashPunk
	* Alarm
	* VarTween
	* MotionTween

Todo
------------------------
* Particle System
* Collision Polygon editor
* GUI

License
------------------------
This engine is covered under the MIT license. Feel free to use the code in any project. I would love to hear what you do with it though!
