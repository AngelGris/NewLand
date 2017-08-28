NewLand
=======

This code randomly generates a terrain and a rover in the center of the map to explore it.

The rover has a limited amount of energy and needs to get back to the base (in the center of the map) to recharge before running out of energy.

The terrain has different levels making the rover use more or less energy to move from one point to another (going up to higher ground needs more energy than going down). Using the known part of the terrain the rover uses a Discover Shortest Path algorithm to explore as much terrain as possible using the less energy.