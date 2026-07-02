package beginner;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;
// For Collection interface
import java.util.*;

public class AboutInheritance {

    abstract class Animal {
        abstract public String makeSomeNoise();
    }

    class Cow extends Animal {
        @Override
        public String makeSomeNoise() {
            return "Moo!";
        }
    }

    class Dog extends Animal {
        @Override
        public String makeSomeNoise() {
            return "Woof!";
        }

        public boolean canFetch() {
            return true;
        }
    }

    class Puppy extends Dog {
        @Override
        public String makeSomeNoise() {
            return "Squeak!";
        }
        public boolean canFetch() {
            return false;
        }
    }

    @Koan
    public void methodOverloading() {
        Cow bob = new Cow();
        Dog max = new Dog();
        Puppy barney = new Puppy();
        assertEquals(bob.makeSomeNoise(), "Moo!");
        assertEquals(max.makeSomeNoise(), "Woof!");
        assertEquals(barney.makeSomeNoise(), "Squeak!");

        assertEquals(max.canFetch(), true);
        assertEquals(barney.canFetch(), false);
        // but can Bob the Cow fetch?
    }

    @Koan
    public void methodOverloadingUsingPolymorphism() {
        Animal bob = new Cow();
        Animal max = new Dog();
        Animal barney = new Puppy();
        assertEquals(bob.makeSomeNoise(), "Moo!");
        assertEquals(max.makeSomeNoise(), "Woof!");
        assertEquals(barney.makeSomeNoise(), "Squeak!");
        // but can max or barney (here as an Animal) fetch?
        // try to write it down here
        // Answer: No, since the canFech method is not in the Animal base class
    }

    @Koan
    public void inheritanceHierarchy() {
        Animal someAnimal = new Cow();
        Animal bob = new Cow();
        assertEquals(someAnimal.makeSomeNoise().equals(bob.makeSomeNoise()), true);
        // cow is a Cow, but it can also be an animal
        assertEquals(bob instanceof Animal, true);
        assertEquals(bob instanceof Cow, true);
        // but is it a Puppy?
        assertEquals(bob instanceof Puppy, false);
    }

    @Koan
    public void deeperInheritanceHierarchy() {
        Dog max = new Dog();
        Puppy barney = new Puppy();
        assertEquals(max instanceof Puppy, false);
        assertEquals(max instanceof Dog, true);
        assertEquals(barney instanceof Puppy, true);
        assertEquals(barney instanceof Dog, true);
    }

    // TODO overriding

   abstract class ParentTwo {
       abstract public List<ParentTwo> doStuff();
   }

   class ChildTwo extends ParentTwo {
       public List<ParentTwo> doStuff() {
           return Collections.emptyList();
       }

       ;
   }

   @Koan
   public void overriddenMethodsMayReturnSubtype() {
       // What do you need to change in order to get rid of the type cast?
       // Collection on line 96 and 100 to a List
       // Why does this work?
       // Since line 113 is declaring a List object, changing Collection to a List removes the need to type cast
       List<ParentTwo> list = (List<ParentTwo>) new ChildTwo().doStuff();
       assertEquals(list instanceof List, true);
   }
}
