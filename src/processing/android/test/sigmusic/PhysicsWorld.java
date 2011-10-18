package processing.android.test.sigmusic;


import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
//    final private int MAXBALLS = 20;
//    final private float FRAMERATE = 30f;
//
//    private float timeStep = (1f / FRAMERATE);
//    private int iterations = 5;
//
////    private Body[] bodies;
//    private int count = 0;
//
//    private AABB worldAABB;
//    private World world;
//    
//    public void createWorld()
//    {
//        // Step 1: Create Physics World Boundaries
//        worldAABB = new AABB();
//        worldAABB.lowerBound.set(new Vec2(-100f, -100f));
//        worldAABB.upperBound.set(new Vec2(100f, 100f));
//
//        // Step 2: Create Physics World with Gravity
//        Vec2 gravity = new Vec2(0f, -10f);
//        boolean doSleep = false;
//        world = new World(worldAABB, gravity, doSleep);
////        bodies = new Body[MAXBALLS];
//    }
//
//    public void setGrav(float x, float y)
//    {
//      world.setGravity(new Vec2(x,y));
//    }
//
//    // addbox and addball are so close that they should be merged
//    public void addBox(float x, float y, float xr, float yr, float angle, boolean dynamic)
//    {
//      if (count < (MAXBALLS-1))
//      {
//          BodyDef groundBodyDef;
//          groundBodyDef = new BodyDef();
//          groundBodyDef.position.set(new Vec2(x, y));
//          groundBodyDef.angle = angle;
//          Body groundBody = world.createBody(groundBodyDef);
//  
//          PolygonDef groundShapeDef;
//          groundShapeDef = new PolygonDef();
//          groundShapeDef.setAsBox(xr, yr);
//          groundShapeDef.density = 1.0f;
//          groundBody.createShape(groundShapeDef);
//          if (dynamic)
//          {
//            groundBody.setMassFromShapes();
//          }
//          // only increment the count when dynamic
//          if (dynamic)
//          {
//            count++;
//          }
//      }
//    }
//
//    public void addBall(float x, float y, float r, boolean dynamic)
//    {
//      if (count < (MAXBALLS-1))
//      {
//          BodyDef groundBodyDef2;
//          CircleDef groundShapeDef2;
//          groundBodyDef2 = new BodyDef();
//          groundBodyDef2.position.set(new Vec2(x,y));
//          Body groundBody2 = world.createBody(groundBodyDef2);
//          groundShapeDef2 = new CircleDef();
//          groundShapeDef2.radius = r;
//          groundShapeDef2.density = 1.0f;
//          groundBody2.createShape(groundShapeDef2);
//          if (dynamic)
//          {
//            groundBody2.setMassFromShapes();
//          }
//          // only increment the count when dynamic
//          if (dynamic)
//          {
//            count++;
//          }
//      }
//    }
//
//    public void update()
//    {
//        world.step(timeStep, iterations);
//    }
//
//    public int getCount()
//    {
//      return count;
//    }
//
//    public Body getBodyList()
//    {
//      return world.getBodyList();
//    }
}
