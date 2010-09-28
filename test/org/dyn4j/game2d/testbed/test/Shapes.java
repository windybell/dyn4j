/*
 * Copyright (c) 2010 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.dyn4j.game2d.testbed.test;

import org.dyn4j.game2d.collision.Bounds;
import org.dyn4j.game2d.collision.RectangularBounds;
import org.dyn4j.game2d.dynamics.BodyFixture;
import org.dyn4j.game2d.dynamics.World;
import org.dyn4j.game2d.geometry.Circle;
import org.dyn4j.game2d.geometry.Geometry;
import org.dyn4j.game2d.geometry.Mass;
import org.dyn4j.game2d.geometry.Polygon;
import org.dyn4j.game2d.geometry.Rectangle;
import org.dyn4j.game2d.geometry.Segment;
import org.dyn4j.game2d.geometry.Triangle;
import org.dyn4j.game2d.geometry.Vector2;
import org.dyn4j.game2d.testbed.ContactCounter;
import org.dyn4j.game2d.testbed.Entity;
import org.dyn4j.game2d.testbed.Test;

/**
 * Tests circle and polygon shapes in collision deteciton and resolution.
 * @author William Bittle
 * @version 2.0.0
 * @since 1.0.0
 */
public class Shapes extends Test {
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.testbed.Test#getName()
	 */
	@Override
	public String getName() {
		return "Shapes";
	}
	
	/* (non-Javadoc)
	 * @see test.Test#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Tests the various shapes supported.  This test ensures that all " +
			   "shapes supported are caught by collision detection and resolved accordingly.";
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.Test#initialize()
	 */
	@Override
	public void initialize() {
		// call the super method
		super.initialize();
		
		// setup the camera
		this.home();
		
		// create the world
		Bounds bounds = new RectangularBounds(Geometry.createRectangle(16.0, 15.0));
		this.world = new World(bounds);
		
		// setup the contact counter
		ContactCounter cc = new ContactCounter();
		this.world.setContactListener(cc);
		this.world.setStepListener(cc);
		
		// setup the bodies
		this.setup();
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.Test#setup()
	 */
	@Override
	protected void setup() {
		// create the floor
		Rectangle floorRect = new Rectangle(15.0, 1.0);
		Entity floor = new Entity();
		floor.addFixture(new BodyFixture(floorRect));
		floor.setMass(Mass.Type.INFINITE);
		this.world.add(floor);
		
		// create a triangle object
		Triangle triShape = new Triangle(
				new Vector2(0.0, 0.5), 
				new Vector2(-0.5, -0.5), 
				new Vector2(0.5, -0.5));
		Entity triangle = new Entity();
		triangle.addFixture(new BodyFixture(triShape));
		triangle.setMass();
		triangle.translate(-1.0, 2.0);
		// test having a velocity
		triangle.getVelocity().set(5.0, 0.0);
		this.world.add(triangle);
		
		// create a circle
		Circle cirShape = new Circle(0.5);
		Entity circle = new Entity();
		circle.addFixture(new BodyFixture(cirShape));
		circle.setMass();
		circle.translate(2.0, 2.0);
		// test adding some force
		circle.apply(new Vector2(-100.0, 0.0));
		// set some linear damping to simulate rolling friction
		circle.setLinearDamping(0.05);
		this.world.add(circle);
		
		// create a line segment
		Segment segShape = new Segment(new Vector2(0.5, 0.5), new Vector2(-0.5, -0.5));
		Entity segment1 = new Entity();
		segment1.addFixture(new BodyFixture(segShape));
		segment1.setMass();
		segment1.translate(1.0, 6.0);
		this.world.add(segment1);
		
		// try a segment parallel to the floor
		Entity segment2 = new Entity();
		segment2.addFixture(new BodyFixture(segShape));
		segment2.setMass();
		segment2.rotateAboutCenter(Math.toRadians(-45.0));
		segment2.translate(-4.5, 1.0);
		this.world.add(segment2);
		
		// try a rectangle
		Rectangle rectShape = new Rectangle(1.0, 1.0);
		Entity rectangle = new Entity();
		rectangle.addFixture(new BodyFixture(rectShape));
		rectangle.setMass();
		rectangle.translate(0.0, 2.0);
		rectangle.getVelocity().set(-5.0, 0.0);
		this.world.add(rectangle);
		
		// try a polygon with lots of vertices
		Polygon polyShape = Geometry.createUnitCirclePolygon(10, 1.0);
		Entity polygon = new Entity();
		polygon.addFixture(new BodyFixture(polyShape));
		polygon.setMass();
		polygon.translate(-2.5, 2.0);
		// set the angular velocity
		polygon.setAngularVelocity(Math.toRadians(-20.0));
		this.world.add(polygon);
		
		// try a compound object (Capsule)
		Circle c1 = new Circle(0.5);
		
		BodyFixture c1Fixture = new BodyFixture(c1);
		c1Fixture.setDensity(0.5);
		
		Circle c2 = new Circle(0.5);
		
		BodyFixture c2Fixture = new BodyFixture(c2);
		c2Fixture.setDensity(0.5);
		
		Rectangle rm = new Rectangle(2.0, 1.0);
		// translate the circles in local coordinates
		c1.translate(-1.0, 0.0);
		c2.translate(1.0, 0.0);
		Entity capsule = new Entity();
		capsule.addFixture(c1Fixture);
		capsule.addFixture(c2Fixture);
		capsule.addFixture(new BodyFixture(rm));
		capsule.setMass();
		capsule.translate(0.0, 4.0);
		this.world.add(capsule);
		
		Entity issTri = new Entity();
		issTri.addFixture(Geometry.createIsoscelesTriangle(1.0, 3.0));
		issTri.setMass();
		issTri.translate(2.0, 3.0);
		this.world.add(issTri);
		
		Entity equTri = new Entity();
		equTri.addFixture(Geometry.createEquilateralTriangle(2.0));
		equTri.setMass();
		equTri.translate(3.0, 3.0);
		this.world.add(equTri);
		
		Entity rightTri = new Entity();
		rightTri.addFixture(Geometry.createRightTriangle(2.0, 1.0));
		rightTri.setMass();
		rightTri.translate(4.0, 3.0);
		this.world.add(rightTri);
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.Test#home()
	 */
	@Override
	public void home() {
		// set the scale
		this.scale = 64.0;
		// set the camera offset
		this.offset.set(0.0, -2.0);
	}
}
